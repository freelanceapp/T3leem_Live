package com.t3leem_live.services;

import com.t3leem_live.models.MessageDataModel;
import com.t3leem_live.models.MySonsDataModel;
import com.t3leem_live.models.NotificationCountModel;
import com.t3leem_live.models.NotificationDataModel;
import com.t3leem_live.models.PlaceGeocodeData;
import com.t3leem_live.models.PlaceMapDetailsData;
import com.t3leem_live.models.RoomDataModel;
import com.t3leem_live.models.SettingDataModel;
import com.t3leem_live.models.SingleMessageDataModel;
import com.t3leem_live.models.SliderDataModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.StreamDataModel;
import com.t3leem_live.models.StreamModel;
import com.t3leem_live.models.StudentRateDataModel;
import com.t3leem_live.models.SummaryDataModel;
import com.t3leem_live.models.TeacherExamDataModel;
import com.t3leem_live.models.TeacherGroupDataModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.models.TeacherStudentsDataModel;
import com.t3leem_live.models.TeachersDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.VideoLessonsDataModel;

import org.androidannotations.annotations.rest.Get;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @GET("api/get-subjects-by-class-id")
    Call<StageDataModel> getSubjectByClassId(@Query(value = "class_id") int class_id,
                                             @Query(value = "stage_id") int stage_id
    );


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
                                              @Field("department_id") String department_id,
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

    @FormUrlEncoded
    @POST("api/register-parent")
    Call<UserModel> signUpParentWithoutImage(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("phone_code") String phone_code,
                                             @Field("phone") String phone,
                                             @Field("password") String password,
                                             @Field("address") String address,
                                             @Field("software_type") String software_type,
                                             @Field("user_type") String user_type


    );

    @Multipart
    @POST("api/register-parent")
    Call<UserModel> signUpParentWithImage(@Part("name") RequestBody name,
                                          @Part("email") RequestBody email,
                                          @Part("phone_code") RequestBody phone_code,
                                          @Part("phone") RequestBody phone,
                                          @Part("password") RequestBody password,
                                          @Part("address") RequestBody address,
                                          @Part("software_type") RequestBody software_type,
                                          @Part("user_type") RequestBody user_type,
                                          @Part MultipartBody.Part logo
    );


    @Multipart
    @POST("api/register-teacher")
    Call<UserModel> signUpTeacherWithImage(@Part("name") RequestBody name,
                                           @Part("email") RequestBody email,
                                           @Part("phone_code") RequestBody phone_code,
                                           @Part("phone") RequestBody phone,
                                           @Part("password") RequestBody password,
                                           @Part("address") RequestBody address,
                                           @Part("school_name") RequestBody school_name,
                                           @Part("stage_id") RequestBody stage_id,
                                           @Part("teacher_degree") RequestBody teacher_degree,
                                           @Part("software_type") RequestBody software_type,
                                           @Part("user_type") RequestBody user_type,
                                           @Part MultipartBody.Part logo,
                                           @Part MultipartBody.Part video
    );

    @Multipart
    @POST("api/register-teacher")
    Call<UserModel> signUpTeacherWithoutImage(@Part("name") RequestBody name,
                                              @Part("email") RequestBody email,
                                              @Part("phone_code") RequestBody phone_code,
                                              @Part("phone") RequestBody phone,
                                              @Part("password") RequestBody password,
                                              @Part("address") RequestBody address,
                                              @Part("school_name") RequestBody school_name,
                                              @Part("stage_id") RequestBody stage_id,
                                              @Part("teacher_degree") RequestBody teacher_degree,
                                              @Part("software_type") RequestBody software_type,
                                              @Part("user_type") RequestBody user_type,
                                              @Part MultipartBody.Part video
    );


    @Multipart
    @POST("api/teacher-update-profile")
    Call<UserModel> updateTeacherWithImageWithVideo(@Header("Authorization") String bearer_token,
                                                    @Part("id") RequestBody id,
                                                    @Part("name") RequestBody name,
                                                    @Part("email") RequestBody email,
                                                    @Part("phone_code") RequestBody phone_code,
                                                    @Part("phone") RequestBody phone,
                                                    @Part("address") RequestBody address,
                                                    @Part("school_name") RequestBody school_name,
                                                    @Part("stage_id") RequestBody stage_id,
                                                    @Part("teacher_degree") RequestBody teacher_degree,
                                                    @Part("software_type") RequestBody software_type,
                                                    @Part("user_type") RequestBody user_type,
                                                    @Part MultipartBody.Part logo,
                                                    @Part MultipartBody.Part video
    );

    @Multipart
    @POST("api/teacher-update-profile")
    Call<UserModel> updateTeacherWithoutImageWithVideo(@Header("Authorization") String bearer_token,
                                                       @Part("id") RequestBody id,
                                                       @Part("name") RequestBody name,
                                                       @Part("email") RequestBody email,
                                                       @Part("phone_code") RequestBody phone_code,
                                                       @Part("phone") RequestBody phone,
                                                       @Part("address") RequestBody address,
                                                       @Part("school_name") RequestBody school_name,
                                                       @Part("stage_id") RequestBody stage_id,
                                                       @Part("teacher_degree") RequestBody teacher_degree,
                                                       @Part("software_type") RequestBody software_type,
                                                       @Part("user_type") RequestBody user_type,
                                                       @Part MultipartBody.Part video
    );

    @Multipart
    @POST("api/teacher-update-profile")
    Call<UserModel> updateTeacherWithImageWithoutVideo(@Header("Authorization") String bearer_token,
                                                       @Part("id") RequestBody id,
                                                       @Part("name") RequestBody name,
                                                       @Part("email") RequestBody email,
                                                       @Part("phone_code") RequestBody phone_code,
                                                       @Part("phone") RequestBody phone,
                                                       @Part("password") RequestBody password,
                                                       @Part("school_name") RequestBody school_name,
                                                       @Part("stage_id") RequestBody stage_id,
                                                       @Part("teacher_degree") RequestBody teacher_degree,
                                                       @Part("software_type") RequestBody software_type,
                                                       @Part("user_type") RequestBody user_type,
                                                       @Part MultipartBody.Part logo
    );

    @Multipart
    @POST("api/teacher-update-profile")
    Call<UserModel> updateTeacherWithoutImageWithoutVideo(@Header("Authorization") String bearer_token,
                                                          @Part("id") RequestBody id,
                                                          @Part("name") RequestBody name,
                                                          @Part("email") RequestBody email,
                                                          @Part("phone_code") RequestBody phone_code,
                                                          @Part("phone") RequestBody phone,
                                                          @Part("address") RequestBody address,
                                                          @Part("school_name") RequestBody school_name,
                                                          @Part("stage_id") RequestBody stage_id,
                                                          @Part("teacher_degree") RequestBody teacher_degree,
                                                          @Part("software_type") RequestBody software_type,
                                                          @Part("user_type") RequestBody user_type
    );


    @GET("api/get-subjects-by-class-id-or-department-id")
    Call<StageDataModel> getSubject(@Query(value = "stage_id") int stage_id,
                                    @Query(value = "class_id") int class_id,
                                    @Query(value = "department_id") String department_id
    );

    @GET("api/get-summary-by-subject-id")
    Call<SummaryDataModel> getSummary(@Query(value = "stage_id") int stage_id,
                                      @Query(value = "class_id") int class_id,
                                      @Query(value = "department_id") String department_id,
                                      @Query(value = "subject_id") int subject_id
    );


    @GET("api/get-tutorials-details-by-subject-id")
    Call<VideoLessonsDataModel> getVideos(@Query(value = "stage_id") String stage_id,
                                          @Query(value = "class_id") String class_id,
                                          @Query(value = "department_id") String department_id,
                                          @Query(value = "subject_id") String subject_id,
                                          @Query(value = "document_type") String document_type

    );

    @GET("api/get-library-details-by-id")
    Call<StageDataModel> getBooks(@Query(value = "library_id") int library_id);

    @GET("api/student-rates")
    Call<StudentRateDataModel> getStudentRate(@Query(value = "student_id") int student_id);

    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String bearer_token);

    @GET("api/sliders")
    Call<SliderDataModel> getSlider();

    @GET("api/Get-teacher-Exams")
    Call<TeacherExamDataModel> getTeacherExams(@Header("Authorization") String bearer_token,
                                               @Query(value = "teacher_id") int teacher_id);


    @GET("api/get_stage_teachers")
    Call<TeachersDataModel> getStudentTeachers(@Query(value = "pagination_status") String pagination_status,
                                               @Query(value = "per_link_") int per_link,
                                               @Query(value = "page") int page,
                                               @Query(value = "stage_id") int stage_id
    );


    @GET("api/get-teacher-groups")
    Call<TeacherGroupDataModel> getStudentTeachersGroups(@Query(value = "stage_id") int stage_id,
                                                         @Query(value = "class_id") int class_id,
                                                         @Query(value = "department_id") String department_id,
                                                         @Query(value = "subject_id") int subject_id,
                                                         @Query(value = "student_id") int student_id
    );


    @FormUrlEncoded
    @POST("api/add-student-book")
    Call<ResponseBody> studentGroupReservation(@Field("student_id") int student_id,
                                               @Field("teacher_group_id") int teacher_group_id,
                                               @Field("subject_id") int subject_id


    );


    @GET("api/Get-exams-by-subject-id")
    Call<TeacherExamDataModel> getStudentExams(@Header("Authorization") String bearer_token,
                                               @Query(value = "subject_id") int subject_id);


    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contactUs(@Header("Authorization") String bearer_token,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("subject") String subject,
                                 @Field("message") String message


    );


    @GET("api/settings")
    Call<SettingDataModel> getSetting();

    @FormUrlEncoded
    @POST("api/update-profile")
    Call<UserModel> updateStudentProfileWithoutImage(@Header("Authorization") String bearer_token,
                                                     @Field("id") int id,
                                                     @Field("name") String name,
                                                     @Field("email") String email,
                                                     @Field("phone_code") String phone_code,
                                                     @Field("phone") String phone,
                                                     @Field("parent_phone") String parent_phone,
                                                     @Field("address") String address,
                                                     @Field("school_name") String school_name,
                                                     @Field("stage_id") int stage_id,
                                                     @Field("class_id") int class_id,
                                                     @Field("department_id") String department_id,
                                                     @Field("software_type") String software_type,
                                                     @Field("user_type") String user_type


    );

    @Multipart
    @POST("api/update-profile")
    Call<UserModel> updateStudentProfileWithImage(@Header("Authorization") String bearer_token,
                                                  @Part("id") RequestBody id,
                                                  @Part("name") RequestBody name,
                                                  @Part("email") RequestBody email,
                                                  @Part("phone_code") RequestBody phone_code,
                                                  @Part("phone") RequestBody phone,
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


    @FormUrlEncoded
    @POST("api/update-parent-profile")
    Call<UserModel> updateParentProfileWithoutImage(@Header("Authorization") String bearer_token,
                                                    @Field("id") int id,
                                                    @Field("name") String name,
                                                    @Field("email") String email,
                                                    @Field("phone_code") String phone_code,
                                                    @Field("phone") String phone,
                                                    @Field("address") String address,
                                                    @Field("software_type") String software_type,
                                                    @Field("user_type") String user_type


    );

    @Multipart
    @POST("api/update-parent-profile")
    Call<UserModel> updateParentProfileWithImage(@Header("Authorization") String bearer_token,
                                                 @Part("id") RequestBody id,
                                                 @Part("name") RequestBody name,
                                                 @Part("email") RequestBody email,
                                                 @Part("phone_code") RequestBody phone_code,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("address") RequestBody address,
                                                 @Part("software_type") RequestBody software_type,
                                                 @Part("user_type") RequestBody user_type,
                                                 @Part MultipartBody.Part logo
    );


    @FormUrlEncoded
    @POST("api/add-new-groups")
    Call<ResponseBody> teacherAddGroup(@Field("teacher_id") int teacher_id,
                                       @Field("stage_id") int stage_id,
                                       @Field("class_id") int class_id,
                                       @Field("department_id") String department_id,
                                       @Field("subject_id") int subject_id,
                                       @Field("title") String title,
                                       @Field("desc") String desc,
                                       @Field("student_limit") String student_limit


    );

    @GET("api/get-my-groups-by-teacher-id")
    Call<List<TeacherGroupModel>> getTeachersGroups(@Query(value = "teacher_id") int teacher_id);

    @FormUrlEncoded
    @POST("api/delete-group")
    Call<ResponseBody> deleteGroups(@Field(value = "teacher_group_id") int teacher_group_id);


    @GET("api/my-students")
    Call<TeacherStudentsDataModel> getStudents(@Query(value = "pagination_status") String pagination_status,
                                               @Query(value = "per_link_") int per_link,
                                               @Query(value = "page") int page,
                                               @Query(value = "teacher_id") int teacher_id
    );

    @FormUrlEncoded
    @POST("api/update_zoom")
    Call<StreamDataModel> teacherCreateStream(@Field("teacher_id") int teacher_id,
                                              @Field("stage_id") int stage_id,
                                              @Field("class_id") int class_id,
                                              @Field("department_id") String department_id,
                                              @Field("subject_id") int subject_id,
                                              @Field("teacher_group_id") int teacher_group_id,
                                              @Field("topic") String title,
                                              @Field("agenda") String subject,
                                              @Field("teacher_live_price") String teacher_live_price,
                                              @Field("duration") String duration,
                                              @Field("device_type") String device_type


    );

    @FormUrlEncoded
    @POST("api/create-new-room-group")
    Call<ResponseBody> teacherCreateChatGroups(@Header("Authorization") String bearer_token,
                                               @Field("title") String title,
                                               @Field("desc") String desc,
                                               @Field("room_type") String room_type,
                                               @Field("from_user_id") int from_user_id,
                                               @Field("group_id") int group_id


    );

    @FormUrlEncoded
    @POST("api/create-new-room")
    Call<ResponseBody> teacherCreateStudentChat(@Header("Authorization") String bearer_token,
                                                @Field("title") String title,
                                                @Field("desc") String desc,
                                                @Field("room_type") String room_type,
                                                @Field("from_user_id") int from_user_id,
                                                @Field("to_user_id[]") List<Integer> usersId


    );

    @GET("api/get-all-live-stream")
    Call<List<StreamModel>> getStreams(@Query(value = "student_id") int student_id);

    @GET("api/get-rooms")
    Call<RoomDataModel> getRooms(@Header("Authorization") String bearer_token,
                                 @Query(value = "pagination_status") String pagination_status,
                                 @Query(value = "per_link_") int per_link_,
                                 @Query(value = "page") int page,
                                 @Query(value = "user_id") int user_id,
                                 @Query(value = "user_type") String user_type,
                                 @Query(value = "room_status") String room_status

    );

    @GET("api/get-message-by-room-id")
    Call<MessageDataModel> getChatMessages(@Header("Authorization") String bearer_token,
                                           @Query(value = "pagination_status") String pagination_status,
                                           @Query(value = "per_link_") int per_link_,
                                           @Query(value = "page") int page,
                                           @Query(value = "room_id") int room_id

    );

    @FormUrlEncoded
    @POST("api/add-msg")
    Call<SingleMessageDataModel> sendChatMessage(@Header("Authorization") String bearer_token,
                                                 @Field("room_id") int room_id,
                                                 @Field("from_user_id") int from_user_id,
                                                 @Field("type") String type,
                                                 @Field("message") String message


    );

    @Multipart
    @POST("api/add-msg")
    Call<SingleMessageDataModel> sendChatAttachment(@Header("Authorization") String user_token,
                                                    @Part("room_id") RequestBody room_id,
                                                    @Part("from_user_id") RequestBody from_user_id,
                                                    @Part("type") RequestBody message_type,
                                                    @Part MultipartBody.Part attachment
    );


    @GET("api/my-sons")
    Call<MySonsDataModel> getMySons(@Header("Authorization") String user_token,
                                    @Query(value = "parent_id") int parent_id
    );

    @GET("api/search-for-my-sons")
    Call<MySonsDataModel> search(@Header("Authorization") String user_token,
                                 @Query(value = "search_val") String query
    );


    @FormUrlEncoded
    @POST("api/add-to-my-sons")
    Call<ResponseBody> addSon(@Header("Authorization") String bearer_token,
                              @Field("parent_id") int parent_id,
                              @Field("student_id") int student_id

    );

    @GET("api/get_my_notifications")
    Call<NotificationDataModel> getNotification(@Header("Authorization") String user_token,
                                                @Query(value = "user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/accept_or_refuse_parent_request")
    Call<ResponseBody> acceptRefuseRequest(@Header("Authorization") String bearer_token,
                                           @Field("status") String status,
                                           @Field("parent_with_son_id") int parent_with_son_id

    );

    @FormUrlEncoded
    @POST("api/get_notification_count")
    Call<NotificationCountModel> getNotificationCount(@Header("Authorization") String bearer_token,
                                                      @Field(value = "user_id") int user_id

    );

    @FormUrlEncoded
    @POST("api/update-is-read")
    Call<ResponseBody> readNotificationCount(@Header("Authorization") String bearer_token,
                                                      @Field(value = "user_id") int user_id

    );

}