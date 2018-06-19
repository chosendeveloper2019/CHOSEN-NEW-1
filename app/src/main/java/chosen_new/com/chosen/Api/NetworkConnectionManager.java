package chosen_new.com.chosen.Api;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import chosen_new.com.chosen.Model.AddCardModel;
import chosen_new.com.chosen.Model.ChargeModel;
import chosen_new.com.chosen.Model.FbLoginModel;
import chosen_new.com.chosen.Model.FbRegisModel;
import chosen_new.com.chosen.Model.GetCardModel;
import chosen_new.com.chosen.Model.LoginModel;
import chosen_new.com.chosen.Model.MapModel_;
import chosen_new.com.chosen.Model.PaymentInvoiceModel;
import chosen_new.com.chosen.Model.PaymentModel;
import chosen_new.com.chosen.Model.RegisterModel;
import chosen_new.com.chosen.Model.ResPaypal.ResultPaymentModel;
import chosen_new.com.chosen.Util.UrlUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnectionManager {

    public NetworkConnectionManager(){

    }
    public void callFbLogin(final CallbackFacebookLogin listener, String fb_id){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        //call  server
        Call call = git.facebookLogin(fb_id);

        call.enqueue(new Callback<List<FbLoginModel>>() {

            @Override
            public void onResponse(Call<List<FbLoginModel>> call, Response<List<FbLoginModel>> response) {
                try {

                    List<FbLoginModel> fbLogin =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(fbLogin);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<FbLoginModel>> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }

    public void callFbRegis(final CallbackFacebookRegis listener
                            ,String fb_id
                            ,String fb_email
                            ,String fb_name
                            ,String usr)
    {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        //call  server
        Call call = git.facebookRegister(fb_id,fb_email,fb_name,usr);

        call.enqueue(new Callback<List<FbRegisModel>>() {

            @Override
            public void onResponse(Call<List<FbRegisModel>> call, Response<List<FbRegisModel>> response) {
                try {

                    List<FbRegisModel> fbLogin =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(fbLogin);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<FbRegisModel>> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

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

    public void callRegisterNormal(final CallbackRegisListener listener, String fullName, String usr, String pwd, String email, String tel){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);

        Call call = git.registerNormal(fullName,usr,pwd,email,tel);

        call.enqueue(new Callback<RegisterModel>() {

            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                try {

                    RegisterModel homeRes =  response.body();

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
            public void onFailure(Call<RegisterModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }


    public void callHomeFrg(final CallbackHomeListener listener, String uid){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
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

    public void callPayment(final CallbackPaymentListener listener, String uid){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        uid = "1017";
        Call call = git.getPaypal(uid);

        call.enqueue(new Callback<List<PaymentModel>>() {

            @Override
            public void onResponse(Call<List<PaymentModel>> call, Response<List<PaymentModel>> response) {
                try {

                    List<PaymentModel> homeRes =  response.body();

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
            public void onFailure(Call<List<PaymentModel>> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }


    public void callShowInvoice(final CallbackShowInvoiceListener listener, String userId,String invoice_id){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);
        Call call = git.getShowInvoice(userId,invoice_id);

        call.enqueue(new Callback<List<PaymentInvoiceModel>>() {

            @Override
            public void onResponse(Call<List<PaymentInvoiceModel>> call, Response<List<PaymentInvoiceModel>> response) {
                try {

                    List<PaymentInvoiceModel> homeRes =  response.body();

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
            public void onFailure(Call<List<PaymentInvoiceModel>> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public void callSendInvoice(final CallbackInvoiceListener listener, String proname,String description,
                                String currency,String quantity,String tax,String price,String invoice_id){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URLPAYMENT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);

        Call call = git.payMent(proname,description,currency,quantity,tax,price,invoice_id);

        call.enqueue(new Callback<ResultPaymentModel>() {

            @Override
            public void onResponse(Call<ResultPaymentModel> call, Response<ResultPaymentModel> response) {
                try {

                    ResultPaymentModel homeRes =  response.body();

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
            public void onFailure(Call<ResultPaymentModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public void callGetStateCharge(final CallbackStatusChageListener listener, String cardId, String userId,String transId){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);

        Call call = git.getStatusChrage(cardId,userId,transId);

        call.enqueue(new Callback<List<ChargeModel>>() {

            @Override
            public void onResponse(Call<List<ChargeModel>> call, Response<List<ChargeModel>> response) {
                try {

                    List<ChargeModel> homeRes =  response.body();
                   Log.d("callGetStateCharge","HAHAHAHAHA"+homeRes.get(0).getInvoiceId());

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
            public void onFailure(Call<List<ChargeModel>> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public void callGetCard(final CallbackCardListener listener, String userId) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);

        Call call = git.getCard(userId);

        call.enqueue(new Callback<List<GetCardModel>>() {

            @Override
            public void onResponse(Call<List<GetCardModel>> call, Response<List<GetCardModel>> response) {
                try {

                    List<GetCardModel> homeRes = response.body();

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


                } catch (Exception e) {
                    listener.onFailure(e);

                }
            }

            @Override
            public void onFailure(Call<List<GetCardModel>> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public void callAddcard(final CallbackAddcardListener listener, String userId,String cardId) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService git = retrofit.create(ApiService.class);

        Call call = git.addCard(userId,cardId);

        call.enqueue(new Callback<AddCardModel>() {

            @Override
            public void onResponse(Call<AddCardModel> call, Response<AddCardModel> response) {
                try {

                    AddCardModel homeRes = response.body();

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


                } catch (Exception e) {
                    listener.onFailure(e);

                }
            }

            @Override
            public void onFailure(Call<AddCardModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }



}
