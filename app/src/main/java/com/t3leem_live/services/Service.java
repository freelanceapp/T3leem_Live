package com.t3leem_live.services;

import com.t3leem_live.models.PlaceGeocodeData;
import com.t3leem_live.models.PlaceMapDetailsData;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.UserModel;

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

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @GET("api/get-stages")
    Call<StageDataModel> getStage();

    @GET("api/get-classes-by-stage_id")
    Call<StageDataModel> getClassByStageId(@Query(value = "stage_id") int stage_id);

    @GET("api/get-department-by-class-id")
    Call<StageDataModel> getDepartmentByClassId(@Query(value = "class_id") int class_id);

    @GET("api/get-libraries")
    Call<StageDataModel> getLibraries();

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone


    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUpStudentWithoutImage(@Field("name") String name,
                                              @Field("email") String email,
                                              @Field("phone_code") String phone_code,
                                              @Field("phone") String phone,
                                              @Field("password") String password,
                                              @Field("parent_phone") String parent_phone,
                                              @Field("address") String address,
                                              @Field("school_name") String school_name,
                                              @Field("stage_id") int stage_id,
                                              @Field("class_id") int class_id,
                                              @Field("department_id") int department_id,
                                              @Field("software_type") String software_type,
                                              @Field("user_type") String user_type


    );

    @Multipart
    @POST("api/register")
    Call<UserModel> signUpStudentWithImage(@Part("name") RequestBody name,
                                           @Part("email") RequestBody email,
                                           @Part("phone_code") RequestBody phone_code,
                                           @Part("phone") RequestBody phone,
                                           @Part("password") RequestBody password,
                                           @Part("parent_phone") RequestBody parent_phone,
                                           @Part("address") RequestBody address,
                                           @Part("school_name") RequestBody school_name,
                                           @Part("stage_id") RequestBody stage_id,
                                           @Part("class_id") RequestBody class_id,
                                           @Part("department_id") RequestBody department_id,
                                           @Part("software_type") RequestBody software_type,
                                           @Part("user_type") RequestBody user_type,
                                           @Part MultipartBody.Part logo
    );

    @GET("api/get-subjects-by-class-id-or-department-id")
    Call<StageDataModel> getSubject(@Query(value = "stage_id") int stage_id,
                                    @Query(value = "class_id") int class_id,
                                    @Query(value = "department_id") String department_id
    );


}