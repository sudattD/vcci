package vcci.android.consumer.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vcci.android.consumer.model.about_us.about_us_content.AboutUsContentResponse;
import vcci.android.consumer.model.about_us.about_us_personnel.PersonnelsDataResponse;
import vcci.android.consumer.model.about_us.about_us_secretariat.SecretariatResponse;
import vcci.android.consumer.model.bulletin.BulletinResponse;
import vcci.android.consumer.model.category.CategoriesResponse;
import vcci.android.consumer.model.circulars.circular_detail.CircularDetailResponse;
import vcci.android.consumer.model.circulars.circular_list.CircularListResponse;
import vcci.android.consumer.model.cm_msg.CmMessageResponse;
import vcci.android.consumer.model.committee.CommitteeDataResponse;
import vcci.android.consumer.model.contact.ContactInfoResponse;
import vcci.android.consumer.model.dashboard_news.DashboardResponse;
import vcci.android.consumer.model.dignitary.dig_photo.DignitaryPhotoResponse;
import vcci.android.consumer.model.dignitary.dig_video.DignitaryVideoResponse;
import vcci.android.consumer.model.dignitary_new.dignitary_photos.DignitaryPhotoResponseNew;
import vcci.android.consumer.model.dignitary_new.dignitary_video.DignitaryVideoResponseNew;
import vcci.android.consumer.model.events.event_detail.EventDetailResponse;
import vcci.android.consumer.model.events.event_list.EventListResponse;
import vcci.android.consumer.model.gallery.photos.gallery_detail.GalleryDetailResponse;
import vcci.android.consumer.model.gallery.photos.gallery_list.GalleryResponse;
import vcci.android.consumer.model.gallery.videos.video_list.VideoResponse;
import vcci.android.consumer.model.login.LoginResponse;
import vcci.android.consumer.model.membership_form.MembershipFormsResponse;
import vcci.android.consumer.model.newsDetail.NewsDetailResponse;
import vcci.android.consumer.model.news_by_category.NewsByCategoryIDResponse;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("/web-service/")
    Call<CategoriesResponse> getCategoriesList(@Query("v") String version, @Query("device-type") String device_type,
                                               @Query("service") String service, @Field("device_id") String device_id,
                                               @Field("ip_address") String ip_address, @Field("page") String page, @Field("limit") String limit);


    @FormUrlEncoded
    @POST("/web-service/")
    Call<DashboardResponse> getDashboardData(@Query("v") String version, @Query("device-type") String device_type,
                                             @Query("service") String service, @Field("device_id") String device_id,
                                             @Field("ip_address") String ip_address, @Field("page") String page, @Field("limit") String limit);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<NewsByCategoryIDResponse> getNewsByCategoryID(@Query("v") String version, @Query("device-type") String device_type,
                                                       @Query("service") String service, @Field("device_id") String device_id,
                                                       @Field("ip_address") String ip_address, @Field("page") String page,
                                                       @Field("limit") String limit, @Field("category") String category);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<NewsDetailResponse> getNewsDetail(@Query("v") String version, @Query("device-type") String device_type,
                                           @Query("service") String service, @Field("device_id") String device_id,
                                           @Field("ip_address") String ip_address, @Field("news_id") String news_id);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<BulletinResponse> getBulletinNews(@Query("v") String version, @Query("device-type") String device_type,
                                           @Query("service") String service, @Field("device_id") String device_id,
                                           @Field("ip_address") String ip_address, @Field("page") String page,
                                           @Field("limit") String limit);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<AboutUsContentResponse> getAbouUsContent(@Query("v") String version, @Query("device-type") String device_type,
                                                  @Query("service") String service, @Field("device_id") String device_id,
                                                  @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<PersonnelsDataResponse> getAbouUsPersonnelData(@Query("v") String version, @Query("device-type") String device_type,
                                                        @Query("service") String service, @Field("device_id") String device_id,
                                                        @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<SecretariatResponse> getSecretariatData(@Query("v") String version, @Query("device-type") String device_type,
                                                     @Query("service") String service, @Field("device_id") String device_id,
                                                     @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<EventListResponse> getEventsList(@Query("v") String version, @Query("device-type") String device_type,
                                          @Query("service") String service, @Field("device_id") String device_id,
                                          @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<EventDetailResponse> getEventsDetail(@Query("v") String version, @Query("device-type") String device_type,
                                              @Query("service") String service, @Field("device_id") String device_id,
                                              @Field("ip_address") String ip_address, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<CircularListResponse> getCircularList(@Query("v") String version, @Query("device-type") String device_type,
                                               @Query("service") String service, @Field("device_id") String device_id,
                                               @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<CircularDetailResponse> getCircularDetail(@Query("v") String version, @Query("device-type") String device_type,
                                                   @Query("service") String service, @Field("device_id") String device_id,
                                                   @Field("ip_address") String ip_address, @Field("event_id") String event_id);


    @FormUrlEncoded
    @POST("/web-service/")
    Call<GalleryResponse> getGalleryList(@Query("v") String version, @Query("device-type") String device_type,
                                         @Query("service") String service, @Field("device_id") String device_id,
                                         @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<DignitaryPhotoResponseNew> getDignitaryPhotoList(@Query("v") String version, @Query("device-type") String device_type,
                                                          @Query("service") String service, @Field("device_id") String device_id,
                                                          @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<DignitaryVideoResponseNew> getDignitaryVideoList(@Query("v") String version, @Query("device-type") String device_type,
                                                          @Query("service") String service, @Field("device_id") String device_id,
                                                          @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<DignitaryPhotoResponse> getDignitaryPhotoData(@Query("v") String version, @Query("device-type") String device_type,
                                                  @Query("service") String service, @Field("device_id") String device_id,
                                                  @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<DignitaryVideoResponse> getDignitaryVideoData(@Query("v") String version, @Query("device-type") String device_type,
                                                  @Query("service") String service, @Field("device_id") String device_id,
                                                  @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<GalleryDetailResponse> getGalleryDetail(@Query("v") String version, @Query("device-type") String device_type,
                                                @Query("service") String service, @Field("device_id") String device_id,
                                                @Field("ip_address") String ip_address, @Field("gallery_id") String gallery_id);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<VideoResponse> getGalleryVideoList(@Query("v") String version, @Query("device-type") String device_type,
                                            @Query("service") String service, @Field("device_id") String device_id,
                                            @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<CmMessageResponse> getCmMessage(@Query("v") String version, @Query("device-type") String device_type,
                                         @Query("service") String service, @Field("device_id") String device_id,
                                         @Field("ip_address") String ip_address);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<CommitteeDataResponse> getCommitteeList(@Query("v") String version, @Query("device-type") String device_type,
                                                 @Query("service") String service, @Field("device_id") String device_id,
                                                 @Field("ip_address") String ip_address, @Field("type") String type);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<ContactInfoResponse> getContactInfo(@Query("v") String version, @Query("device-type") String device_type,
                                             @Query("service") String service, @Field("device_id") String device_id,
                                             @Field("ip_address") String ip_address);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<MembershipFormsResponse> getMembershipForms(@Query("v") String version, @Query("device-type") String device_type,
                                                     @Query("service") String service, @Field("device_id") String device_id,
                                                     @Field("ip_address") String ip_address);

    @FormUrlEncoded
    @POST("/web-service/")
    Call<LoginResponse> makeLogin(@Query("v") String version, @Query("device-type") String device_type,
                                  @Query("service") String service, @Field("device_id") String device_id,
                                  @Field("ip_address") String ip_address, @Field("email") String email,
                                  @Field("password") String password);




}