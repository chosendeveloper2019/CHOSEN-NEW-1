package chosen.com.chosen.Api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import chosen.com.chosen.Model.LoginModel;
import chosen.com.chosen.Model.MapModel_;
import chosen.com.chosen.Util.UrlUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnectionManager {

    public NetworkConnectionManager(){

    }

    public void callLogin(final CallbackLoginListener listener, String usr,String pwd){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        //call  server
        Call call = git.logInFrg(usr,pwd);

        call.enqueue(new Callback<List<LoginModel>>() {

            @Override
            public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                try {

                    List<LoginModel> loginRes =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(loginRes);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }
    public void callHomeFrg(final CallbackHomeListener listener, String uid){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.chosenenergy.co.th/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        uid = "";
        Call call = git.homeFrg(uid);

        call.enqueue(new Callback<List<MapModel_>>() {

            @Override
            public void onResponse(Call<List<MapModel_>> call, Response<List<MapModel_>> response) {
               try {

                    List<MapModel_> homeRes =  response.body();

                    if (response.code() != 200) {
                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(homeRes);

                    }


                }catch (Exception e){
                    listener.onFailure(e);

                }
            }
            @Override
            public void onFailure(Call<List<MapModel_>> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

}
