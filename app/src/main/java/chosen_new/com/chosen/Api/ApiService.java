package chosen_new.com.chosen.Api;

import com.facebook.appevents.internal.Constants;

import java.io.File;
import java.util.List;

import chosen_new.com.chosen.Model.AddCardModel;
import chosen_new.com.chosen.Model.CarModel;
import chosen_new.com.chosen.Model.CardModel;
import chosen_new.com.chosen.Model.ChargeModel;
import chosen_new.com.chosen.Model.FbLoginModel;
import chosen_new.com.chosen.Model.FbRegisModel;
import chosen_new.com.chosen.Model.GetCardModel;
import chosen_new.com.chosen.Model.InvoiceCardModel;
import chosen_new.com.chosen.Model.LoginModel;
import chosen_new.com.chosen.Model.MapModel_;
import chosen_new.com.chosen.Model.PaymentInvoiceModel;
import chosen_new.com.chosen.Model.PaymentModel;
import chosen_new.com.chosen.Model.ProfileModel;
import chosen_new.com.chosen.Model.RegisterModel;
import chosen_new.com.chosen.Model.ResultPaymentModel;
import chosen_new.com.chosen.Model.SelectCarModel;
import chosen_new.com.chosen.Model.UpdateProfileModel;
import chosen_new.com.chosen.Model.UploadImageModel;
import chosen_new.com.chosen.Model.UserInvoiceModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/facebook_auth/")
    Call<List<FbRegisModel>> facebookRegister(@Field("facebook_id") String fb_id,
                                              @Field("email") String email,
                                              @Field("name") String name,
                                              @Field("username") String usr,
                                              @Field("tel") String tel,
                                              @Field("password") String pwd
                                   );

    @FormUrlEncoded
    @POST("auth/facebook_auth/")
    Call<List<FbLoginModel>> facebookLogin(@Field("facebook_id") String fb_id);

    @FormUrlEncoded
    @POST("Auth/Login")
    Call<List<LoginModel>> logInFrg(@Field("username") String usr , @Field("password") String pwd);

    @FormUrlEncoded
    @POST("register/create")
    Call<RegisterModel> registerNormal(@Field("user_fullname") String fullname
                                     , @Field("user_name") String usr,
                                       @Field("user_pw") String name,
                                       @Field("user_email") String email,
                                       @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("Home/map")
    Call<List<MapModel_>> homeFrg(@Field("user_id") String uid);


    @FormUrlEncoded
    @POST("payment/get_card")
    Call<List<GetCardModel>> getCard(@Field("user_id") String userid);


    @GET("carmodel/")
    Call<List<CarModel>> getCarModel();

    @FormUrlEncoded
    @POST("carmodel/select_carmodel")
    Call<SelectCarModel> selectCarModel(@Field("car_models_id") String id, @Field("card_id") String card_id);



    @FormUrlEncoded
    @POST("payment/check_card_transaction")
    Call<List<ChargeModel>> getStatusCharge(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("payment/get_card_status")
    Call<List<ChargeModel>> getStatusChrage( @Field("card_id") String card_id,
                                             @Field("user_id") String uid,
                                             @Field("transaction_id") String transactionid
                                            );
    @FormUrlEncoded
    @POST("card/addcard")
    Call<AddCardModel> addCard( @Field("user_id") String uid,
                                    @Field("card_id") String card_id

                                    );

    @FormUrlEncoded
    @POST("payment/get_card_invoice")
    Call<List<InvoiceCardModel>> getInvoice(@Field("user_id") String uid,
                                            @Field("card_id") String card_id

    );


    @GET("payment")
    Call<List<UserInvoiceModel>> getInvoiceUser(@Query("user_id") String uid

    );

    @GET("payment")
    Call<List<PaymentModel>> getPaypal(@Query("user_id") String uid);


    @FormUrlEncoded
    @POST("payment/get_invoice")
    Call<List<PaymentInvoiceModel>> getShowInvoice(
            @Field("user_id") String uid,
            @Field("invoice_id") String invoie_id
    );


    @GET("user/get_myaddressAsync/{user_id}")
    Call<List<ProfileModel>> getProfile(@Path("user_id") String uid

    );

    @FormUrlEncoded
    @POST("index.php/paypal-ec")
    Call<ResultPaymentModel> payMent(@Field("proname") String proname,
                                     @Field("description") String description,
                                     @Field("currency") String currency,
                                     @Field("quantity") String quantity,
                                     @Field("tax") String tax,
                                     @Field("price") String price,
                                     @Field("invoice_id") String invoice_id
    );


    //    @FormUrlEncoded
    @Multipart
    @POST("user/update_image")
    Call<UploadImageModel> updatePicture(@Part MultipartBody.Part file,
                                         @Part MultipartBody.Part user_id);

    @FormUrlEncoded
    @POST("user/update_address")
    Call<UpdateProfileModel> upDateProfile(@Field("user_id") String uid,
                                           @Field("user_fullname") String user_fullname,
                                           @Field("street") String street,
                                           @Field("city") String city,
                                           @Field("postalcode") String postalcode,
                                           @Field("mobile") String mobile
                                         );
}
