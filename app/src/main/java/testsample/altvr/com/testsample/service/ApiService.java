package testsample.altvr.com.testsample.service;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;
import testsample.altvr.com.testsample.RetrofitAdapter;
import testsample.altvr.com.testsample.PixabayRetrofitService;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.util.Constants;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoResponseVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class ApiService {
    private LogUtil log = new LogUtil(ApiService.class);
    private static String PIXABAY_API_KEY = "2387134-2e9952af7d840c1d7abc947b1";
    private static int MIN_IMAGE_WIDTH = 1000;
    private static int MIN_IMAGE_HEIGHT = 1000;
    private static String IMAGE_TYPE = "photo";

    private PixabayRetrofitService mService;
    private EventBus mEventBus;

    public ApiService(Context context) {
        mService = RetrofitAdapter.getRestService(context);
        mEventBus = EventBus.getDefault();
    }

    /**
     * YOUR CODE HERE
     *
     * For part 1a, you should implement getDefaultPhotos and searchPhotos. These calls should make the proper
     * API calls to Pixabay and post PhotosEvents to the event bus for the fragments to fill themselves in.
     *
     * We provide a Retrofit API adapter here you can use, or you can roll your own using the HTTP library
     * of your choice.
     */

    //Get Default photos using Pixabay api
    //getDefaultPhotos(@Query("key") String key, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, Callback<PhotoResponseVo> callback)
     public void getDefaultPhotos()
     {
        mService.getDefaultPhotos(RetrofitAdapter.getAPIKey(), 0, 0, Constants.photoTag, new Callback<PhotoResponseVo>()
        {
            @Override
            public void success(PhotoResponseVo photoResponseVo, Response response)
            {
                LogUtil.log("success in fetching default photos");
                //check if response is empty or not
                if(photoResponseVo != null && photoResponseVo.hits != null)
                {
                    List<PhotoVo> photosList = (List<PhotoVo>) photoResponseVo.hits;
                    PhotosEvent photosevent = new PhotosEvent(photosList);
                    mEventBus.post(photosevent);
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                LogUtil.log("failure in fetching default photos");
                mEventBus.post(new ApiErrorEvent(error.toString()));
            }
        });
     }
}
