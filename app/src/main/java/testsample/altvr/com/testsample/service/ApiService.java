package testsample.altvr.com.testsample.service;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.RetrofitAdapter;
import testsample.altvr.com.testsample.PixabayRetrofitService;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.util.Constants;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoResponseVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class ApiService
{
    private LogUtil log = new LogUtil(ApiService.class);
    private static String PIXABAY_API_KEY = "2387134-2e9952af7d840c1d7abc947b1";
    private static int MIN_IMAGE_WIDTH = 1000;
    private static int MIN_IMAGE_HEIGHT = 1000;
    private static String IMAGE_TYPE = "photo";

    private PixabayRetrofitService mService;
    private EventBus mEventBus;
    private Context mContext;

    public ApiService(Context context)
    {
        mContext = context;
        mService = RetrofitAdapter.getRestService(context);
        mEventBus = EventBus.getDefault();
    }

    /**
     * YOUR CODE HERE
     * <p/>
     * For part 1a, you should implement getPopularPhotos and searchPhotos. These calls should make the proper
     * API calls to Pixabay and post PhotosEvents to the event bus for the fragments to fill themselves in.
     * <p/>
     * We provide a Retrofit API adapter here you can use, or you can roll your own using the HTTP library
     * of your choice.
     */

    //Get Default photos using Pixabay api
    //getPopularPhotos(@Query("key") String key, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, Callback<PhotoResponseVo> callback)
    public void getPopularPhotos(final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        mService.getPopularPhotos(RetrofitAdapter.getAPIKey(), 0, 0, phototag, pagenumber, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.getpopularphotos_success));
                String noresponsestr = mContext.getResources().getString(R.string.getpopularphotos_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.popular));
            }
            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }

    public void getLatestPhotos(final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        String order = mContext.getResources().getString(R.string.latest);
        mService.getLatestPhotos(RetrofitAdapter.getAPIKey(), 0, 0, phototag, order, pagenumber,  new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.getlatestphotos_success));
                String noresponsestr = mContext.getResources().getString(R.string.getlatestphotos_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.latest));

            }
            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }


    public void getEditorsChoicePhotos(final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        boolean editorschoice = true;
        mService.getEditorsChoicePhotos(RetrofitAdapter.getAPIKey(), 0, 0, phototag, editorschoice, pagenumber, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.geteditorschoicephotos_success));
                String noresponsestr = mContext.getResources().getString(R.string.geteditorschoicephotos_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.editorschoice));
            }
            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }



    public void searchPhotos(final String searchString, final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        mService.searchPhotos(RetrofitAdapter.getAPIKey(), searchString, 0, 0, phototag, pagenumber, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.getsearchpopularphotos_success) + searchString);
                String noresponsestr = mContext.getResources().getString(R.string.getsearchpopularphotos_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.popular));
            }

            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }

    public void searchLatestPhotos(final String searchString, final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        String order = mContext.getResources().getString(R.string.latest);
        mService.searchLatestPhotos(RetrofitAdapter.getAPIKey(), searchString, 0, 0, phototag, order, pagenumber, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.getsearchlatestphotos_success) + searchString);
                String noresponsestr = mContext.getResources().getString(R.string.getsearchlatestphotos_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.latest));
            }

            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }

    public void searchEditorsChoicePhotos(final String searchString, final int pagenumber)
    {
        String phototag = mContext.getResources().getString(R.string.photo);
        boolean iseditorschoice = true;
        mService.searchEditorsChoicePhotos(RetrofitAdapter.getAPIKey(), searchString, 0, 0, phototag, iseditorschoice, pagenumber, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log(mContext.getResources().getString(R.string.getsearcheditorschoice_success) + searchString);
                String noresponsestr = mContext.getResources().getString(R.string.getsearcheditorschoice_noresponse);
                getphotosSuccessHandler(photoResponseVo, response, pagenumber, noresponsestr, mContext.getResources().getString(R.string.editorschoice));
            }

            @Override
            public void failure(RetrofitError error)
            {
                errorHandler(error);
            }
        });
    }

    //send photos list to the fragment
    private void getphotosSuccessHandler(PhotoResponseVo photoResponseVo, Response response, int pagenumber, String emptyresponsemsg, String eventType)
    {
        int totalphotoscount = (int) (Math.ceil(pagenumber) * Constants.photosperpageCount);
        if (photoResponseVo == null || photoResponseVo.hits == null || photoResponseVo.hits.size() == 0) //check if response is empty or not
        {
            ApiErrorEvent errorEvent = new ApiErrorEvent(emptyresponsemsg);
            mEventBus.post(errorEvent);
        }
        else if (photoResponseVo.total > totalphotoscount)   //check to see if you have any photos to load
        {
            List<PhotoVo> photosList = (List<PhotoVo>) photoResponseVo.hits;
            PhotosEvent photosevent = new PhotosEvent(photosList, eventType, pagenumber);
            mEventBus.post(photosevent);
        }
        else if (photoResponseVo.total == totalphotoscount)   //check to see if you have any photos to load
        {
            LogUtil.log(emptyresponsemsg);
            ApiErrorEvent errorEvent = new ApiErrorEvent(mContext.getResources().getString(R.string.nomorephotosstring));
            mEventBus.post(errorEvent);
        }
        else
        {
            //clear and whole list
        }
    }

    private void errorHandler(RetrofitError error)
    {
        String errorStr = error.getCause().getMessage();
        LogUtil.log(errorStr);
        mEventBus.post(new ApiErrorEvent(errorStr));
    }
}