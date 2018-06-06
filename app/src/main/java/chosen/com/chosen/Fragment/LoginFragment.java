package chosen.com.chosen.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import chosen.com.chosen.MainApplication;
import chosen.com.chosen.R;
import chosen.com.chosen.Util.ConnectivityReceiverUtil;
import chosen.com.chosen.Util.ServiceUtil;
import chosen.com.chosen.Util.UrlUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment
                implements ConnectivityReceiverUtil.ConnectivityReceiverListener {


    private static final String TAG = LoginFragment.class.getName();
    private EditText edittext_username, edittext_password;
    private TextView button_signin;

    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edittext_username = (EditText) view.findViewById(R.id.edittext_username);
        edittext_password = (EditText) view.findViewById(R.id.edittext_password);

        edittext_password.setText("Happy@2018");
        edittext_username.setText("admin");
        button_signin = view.findViewById(R.id.button_signin);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_signin.setEnabled(false);
                String username = edittext_username.getText().toString();
                String password = edittext_password.getText().toString();
                if(ConnectivityReceiverUtil.isConnected()){
                    if(!username.equals("") && !password.equals("")){
                        RequestLogin(username, password);
                    } else {
                        button_signin.setEnabled(true);
                        showSnack("Please enter username and password");
                    }
                } else {
                    button_signin.setEnabled(true);
                    showSnack("Sorry! Not connected to internet");
                }
            }
        });
    }

    private void RequestLogin(String username, String password){

        final FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(UrlUtil.URL_LOGIN)
                        .post(formBody)
                        .build();
                try {

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Exception";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception";
                }
            }

            @Override
            protected void onPostExecute(String response) {
                Log.w(TAG, response);
                if(response.length() > 20){
                    Intent intent = new Intent(getActivity(), MainApplication.class);
                    intent.putExtra(MainApplication.KEY_DATA_USER, response);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    showSnack("Login success.");
//                    getActivity().finish();
                } else {
                    showSnack("Please check username and password again.");
                }
                button_signin.setEnabled(true);
            }

        }.execute();

//        serviceUtil = new ServiceUtil(formBody, UrlUtil.URL_LOGIN, new ServiceUtil.OnSuccessCallback() {
//            @Override
//            public void Call(String response) {
//                Log.w(TAG, "response : " + response);
//                if(!response.equals("")){
//                    if(!response.equals("Fail")){
//                        showSnack("Please check username and password again....");
//                    } else {
//                        showSnack("Please check username and password again.");
//                    }
//                } else {
//                    button_signin.setEnabled(true);
//                    showSnack("Please check username and password again.");
//                }
//            }
//        }, new ServiceUtil.OnFailureCallback() {
//            @Override
//            public void Call(String message) {
//                Log.w(TAG, "message : " + message);
//            }
//        }).execute();

    }

    private void showSnack(String message) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)
            showSnack("Sorry! Not connected to internet");
    }
}
