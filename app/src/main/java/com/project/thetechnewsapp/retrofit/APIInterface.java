package com.project.thetechnewsapp.retrofit;


import com.project.thetechnewsapp.models.Root;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("userLogin.php")
    Call<Root> LOGINAPI(@Query("phone") String phone,
                        @Query("password") String password,
                        @Query("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("userRegister.php")
    Call<Root> REG_API(@Field("name") String name,
                       @Field("email") String email,
                       @Field("phone") String phone,
                       @Field("password") String password);

    @GET("viewAllNews.php")
    Call<Root> VIEWALLNEWSAPI();


    @GET("viewFav.php")
    Call<Root> VIEWFAVORITE(@Query("uid") String userId);

    @GET("userProfile.php")
    Call<Root> VIEWPROFILE(@Query("user_id") String userId);


    @Multipart
    @POST("editProfile.php")
    Call<Root> UPDATE_USER_DETAILS(@Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("phone") RequestBody phone,
                                   @Part MultipartBody.Part image,
                                   @Part("uid") RequestBody userId);

    @FormUrlEncoded
    @POST("favoriteNews.php")
    Call<Root> ADD_TO_FAVORITE_API(@Field("uid") String userId,
                                   @Field("nid") String newsId);

    @GET("viewNews.php")
    Call<Root> VIEW_DETAILED_NEWS(@Query("news_id") String newsId);

    @GET("categoryNews.php")
    Call<Root> VIEW_NEWS_WITH_CATEGORY(@Query("category_name") String category);

    @GET("search.php")
    Call<Root> SEARCHAPI(@Query("word") String searchData);

    @GET("viewcomments.php")
    Call<Root> VIEW_COMMENTS(@Query("nid") String newsId);

    @FormUrlEncoded
    @POST("comments.php")
    Call<Root> ADD_COMMENT_API(@Field("uid") String userId,
                               @Field("nid") String newsId,
                               @Field("comments") String comments);

}
