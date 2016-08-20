package testsample.altvr.com.testsample.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.vo.PhotoDBVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by tejus on 4/14/2016.
 */
public class DatabaseUtil extends SQLiteOpenHelper {
    private LogUtil log = new LogUtil(DatabaseUtil.class);

    private static final int DATABASE_VERSION = 3;
    //DB and tables
    private static final String DATABASE_NAME = "imagesearcher";
    private static final String TABLE_PHOTOS = "photos";

    //Columns for Images table
    private static final String KEY_PHOTO_ID = "id";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_PHOTO_TAGS = "tags";

    SQLiteDatabase mDb;

    public DatabaseUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + KEY_PHOTO_ID + " STRING PRIMARY KEY," + KEY_PHOTO + " BLOB, " + KEY_PHOTO_TAGS + " TEXT " + ")";
        db.execSQL(CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }

    /**
     * YOUR CODE HERE
     *
     * For part 1b, you should fill in the various CRUD operations below to manipulate the db
     * returned by getWritableDatabase() to store/load photos.
     */
    public List<String> getImagesId()
    {
        List<String> imagesIdList = new ArrayList<>();
        String[] columns = new String[] { KEY_PHOTO_ID};
        Cursor cursor = mDb.query(TABLE_PHOTOS, columns, null, null, null, null, null);
        cursor.moveToFirst();
        do
        {
            imagesIdList.add(cursor.getString(0));
        }while(cursor.moveToNext());
        cursor.close();
        return imagesIdList;
    }


    public PhotoDBVo getImage(String id)
    {
        PhotoDBVo photoObj = null;
        String[] columns = new String[] { KEY_PHOTO_ID, KEY_PHOTO, KEY_PHOTO_TAGS};
        String selection = KEY_PHOTO_ID + " =" + id;
        Cursor cursor =  mDb.query(TABLE_PHOTOS, columns, selection, null, null, null, null);
        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            photoObj = new PhotoDBVo();
            photoObj.setId(cursor.getString(0));
            photoObj.setPhoto(cursor.getBlob(1));
            photoObj.setTags(cursor.getString(2));
        }
        cursor.close();
        return photoObj;
    }

    public boolean insertImage(PhotoDBVo photo)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PHOTO_ID, photo.getId());
        cv.put(KEY_PHOTO, photo.getPhoto());
        cv.put(KEY_PHOTO_TAGS, photo.getTags());
        long insert = mDb.insertWithOnConflict(TABLE_PHOTOS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return insert != -1 ? true : false;
    }

    public boolean deleteImage(String photoId)
    {
        String whereCaluse = KEY_PHOTO_ID + " = ?";
        String[] whereArgs = new String[] { photoId };
        int delete = mDb.delete(TABLE_PHOTOS, whereCaluse, whereArgs);
        return delete != -1 ? true : false;
    }
}
