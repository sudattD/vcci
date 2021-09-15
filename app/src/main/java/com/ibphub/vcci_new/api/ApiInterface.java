package com.ibphub.vcci_new.api;

import com.ibphub.vcci_new.model.about_us.about_us_content.AboutUsContentResponse;
import com.ibphub.vcci_new.model.about_us.about_us_personnel.PersonnelsDataResponse;
import com.ibphub.vcci_new.model.about_us.about_us_secretariat.SecretariatResponse;
import com.ibphub.vcci_new.model.bulletin.BulletinResponse;
import com.ibphub.vcci_new.model.category.CategoriesResponse;
import com.ibphub.vcci_new.model.circulars.circular_detail.CircularDetailResponse;
import com.ibphub.vcci_new.model.circulars.circular_list.CircularListResponse;
import com.ibphub.vcci_new.model.cm_msg.CmMessageResponse;
import com.ibphub.vcci_new.model.committee.CommitteeDataResponse;
import com.ibphub.vcci_new.model.contact.ContactInfoResponse;
import com.ibphub.vcci_new.model.dashboard_news.DashboardResponse;
import com.ibphub.vcci_new.model.dignitary.dig_photo.DignitaryPhotoResponse;
import com.ibphub.vcci_new.model.dignitary.dig_video.DignitaryVideoResponse;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DignitaryPhotoResponseNew;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_video.DignitaryVideoResponseNew;
import com.ibphub.vcci_new.model.events.event_detail.EventDetailResponse;
import com.ibphub.vcci_new.model.events.event_list.EventListResponse;
import com.ibphub.vcci_new.model.gallery.photos.gallery_detail.GalleryDetailResponse;
import com.ibphub.vcci_new.model.gallery.photos.gallery_list.GalleryResponse;
import com.ibphub.vcci_new.model.gallery.videos.video_list.VideoResponse;
import com.ibphub.vcci_new.model.login.LoginResponse;
import com.ibphub.vcci_new.model.membership_form.MembershipFormsResponse;
import com.ibphub.vcci_new.model.newsDetail.NewsDetailResponse;
import com.ibphub.vcci_new.model.news_by_category.NewsByCategoryIDResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


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