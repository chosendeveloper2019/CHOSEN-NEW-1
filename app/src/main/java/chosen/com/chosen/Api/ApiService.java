package chosen.com.chosen.Api;

import java.util.List;

import chosen.com.chosen.Model.FbLoginModel;
import chosen.com.chosen.Model.FbRegisModel;
import chosen.com.chosen.Model.LoginModel;
import chosen.com.chosen.Model.MapModel_;
import chosen.com.chosen.Model.PaymentModel;
import chosen.com.chosen.Util.ApiUtil;
import chosen.com.chosen.Util.UrlUtil;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/facebook_auth/")
    Call<List<FbRegisModel>> facebookRegister(@Field("facebook_id") String fb_id,
                                              @Field("email") String email,
                                              @Field("name") String name,
                                              @Field("username") String usr

                                   );

    @FormUrlEncoded
    @POST("auth/facebook_auth/")
    Call<List<FbLoginModel>> facebookLogin(@Field("facebook_id") String fb_id);

    @FormUrlEncoded
    @POST("Auth/Login")
    Call<List<LoginModel>> logInFrg(@Field("username") String usr , @Field("password") String pwd);

    @FormUrlEncoded
    @POST("Home/map")
    Call<List<MapModel_>> homeFrg(@Field("user_id") String uid);


    @GET("payment")
    Call<List<PaymentModel>> getPaypal(@Query("user_id") String uid);


}
