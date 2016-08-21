package testsample.altvr.com.testsample;

import retrofit.Callback;

import retrofit.http.GET;
import retrofit.http.Query;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.vo.PhotoResponseVo;

public interface PixabayRetrofitService
{

    @GET("/api")
    void getPopularPhotos(@Query("key") String key, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, @Query("page") int page, Callback<PhotoResponseVo> callback);

    @GET("/api")
    void getLatestPhotos(@Query("key") String key, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, @Query("order") String order, @Query("page") int page, Callback<PhotoResponseVo> callback);

    @GET("/api")
    void getEditorsChoicePhotos(@Query("key") String key, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, @Query("editors_choice") boolean editoreschoice, @Query("page") int page, Callback<PhotoResponseVo> callback);

    @GET("/api")
    void searchPhotos(@Query("key") String key, @Query("q") String searchString, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, @Query("page") int page, Callback<PhotoResponseVo> callback);

    @GET("/api")
    void searchLatestPhotos(@Query("key") String key, @Query("q") String searchString, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType, @Query("order") String order, @Query("page") int page, Callback<PhotoResponseVo> callback);

    @GET("/api")
    void searchEditorsChoicePhotos(@Query("key") String key, @Query("q") String searchString, @Query("min_width") int minWidth, @Query("min_height") int minHeight, @Query("image_type") String imageType,  @Query("editors_choice") boolean editoreschoice, @Query("page") int page, Callback<PhotoResponseVo> callback);
}
