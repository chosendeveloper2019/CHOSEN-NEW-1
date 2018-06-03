package chosen.com.chosen.Api;

import java.util.List;

import chosen.com.chosen.Model.LoginModel;
import chosen.com.chosen.Model.MapModel_;
import chosen.com.chosen.Util.ApiUtil;
import chosen.com.chosen.Util.UrlUtil;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("Auth/Login")
    Call<List<LoginModel>> logInFrg(@Field("username") String usr , @Field("password") String pwd);

    @FormUrlEncoded
    @POST("Home/map")
    Call<List<MapModel_>> homeFrg(@Field("user_id") String uid);

}
