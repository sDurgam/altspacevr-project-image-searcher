package testsample.altvr.com.testsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.vo.PhotoDBVo;

/**
 * Created by root on 8/21/16.
 */
@RunWith(RobolectricTestRunner.class)
public class SqliteDatabaseTest
{
    DatabaseUtil mDbUtil;
    Context mConetxt;

    @Before
    public void setUp()
    {
        mConetxt = RuntimeEnvironment.application;
        mDbUtil = new DatabaseUtil(mConetxt);
    }

    @Test
    public void testReadableSQLConnection()
    {
        Assert.assertNotNull(mDbUtil);
        SQLiteDatabase db = mDbUtil.getReadableDatabase();
        Assert.assertNotNull(db);
        db.close();;
    }

    @Test
    public void testWriteableSQLConnection()
    {
        Assert.assertNotNull(mDbUtil);
        SQLiteDatabase db = mDbUtil.getWritableDatabase();
        Assert.assertNotNull(db);
    }

    @Test
    public void testInsertImage()
    {
        SQLiteDatabase db = mDbUtil.getWritableDatabase();
        PhotoDBVo photo = new PhotoDBVo("1", null, "apple, plums");
        Assert.assertNotSame(mDbUtil.insertImage(photo), -1);
    }

    @Test
    public void testUpdateImage()
    {
        SQLiteDatabase db = mDbUtil.getWritableDatabase();
        PhotoDBVo photo = new PhotoDBVo("1", null, "apple, plums");
        Assert.assertNotSame(mDbUtil.insertImage(photo), -1);
        photo = new PhotoDBVo("1", null, "apple, plums, peaches");
        Assert.assertNotSame(mDbUtil.insertImage(photo), -1);
    }

    @Test
    public void testDeleteImage()
    {
        SQLiteDatabase db = mDbUtil.getWritableDatabase();
        PhotoDBVo photo = new PhotoDBVo("1", null, "apple, plums");
        Assert.assertNotSame(mDbUtil.deleteImage("1"), -1);
    }

    @Test
    public void testGetImage()
    {
        mDbUtil.insertImage(new PhotoDBVo("3", null, null));
        Assert.assertNotSame(mDbUtil.getAllImages(), 0);
    }

    @Test
    public void testSQLCloseConnection()
    {
        SQLiteDatabase db = mDbUtil.getWritableDatabase();
        Assert.assertTrue(db.isOpen());
        db.close();
        Assert.assertFalse( db.isOpen());
    }

    @After
    public void tearDown()
    {
        mDbUtil = null;
    }
}
