package chosen.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import chosen.com.chosen.Api.CallbackLoginListener;
import chosen.com.chosen.Api.NetworkConnectionManager;
import chosen.com.chosen.MainApplication;
import chosen.com.chosen.Model.LoginModel;
import chosen.com.chosen.R;
import chosen.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class FragmentLogin extends Fragment implements View.OnClickListener{
    //new object for use authentication facebook
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;

    private ProgressDialog progressDialog;

    //new object for bind widget
    private EditText et_usr,et_pwd;
    private String TAG = "<FragmentLogin>";
    private Context context;

    // session
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment,container,false);

            initInstance(v);

        return v;
    }

    private void initInstance(View v){

        //bind widget editText
        et_usr = v.findViewById(R.id.et_username);
        et_pwd = v.findViewById(R.id.et_password);
        et_usr.setText("admin");
        et_pwd.setText("Happy@2018");

        //init sharedPer
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        //bind widget button
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);

        // get context
        context = getContext();

        //init facebook authentication
        callbackManager = CallbackManager.Factory.create();
        loginButton =  v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Profile profile = Profile.getCurrentProfile();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name"); // 01/31/1980 format
                                    Log.e(TAG,"email = "+email+" name = "+name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

//               Log.d(TAG, "Login Hello "+profile.getProfilePictureUri(50,50));


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }


    private void do_login(){

        try {
            String usr = et_usr.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();

//            Toast.makeText(context, "Login btn", Toast.LENGTH_SHORT).show();
            //call api
            new NetworkConnectionManager().callLogin(listener,usr,pwd);

        }catch (Exception e){
            Toast.makeText(context, "Call Server Error.", Toast.LENGTH_SHORT).show();
        }

    }

    private void do_register(){

        FragmentRegister register = new FragmentRegister();
        fragmentTran(register,null);

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(context, ""+data.getDataString(), Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        profileTracker.stopTracking();
    }

    //callback from server
    private CallbackLoginListener listener = new CallbackLoginListener() {
        @Override
        public void onResponse(List<LoginModel> res) {


            //save to session
            String jsonResult =  saveResponse(res);
//            Toast.makeText(context, ""+jsonResult, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainApplication.class);
            intent.putExtra(MainApplication.KEY_DATA_USER, jsonResult);
            startActivity(intent);
            getActivity().finish();

//            Log.d(TAG,"size data  ="+res.size()+" res" + res.get(0).getUserFullname());


        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.d(TAG,"onBodyError "+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.d(TAG,"onBodyErrorIsNull");
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG,""+t.getMessage());
        }
    };

    private String saveResponse(List<LoginModel> response){


        JSONArray arr = new JSONArray();
        for(int i = 0 ;i<response.size();i++){
            JSONObject jsonObject = new JSONObject();
            Log.e(TAG,response.get(i).toString());
            editor.putString(MyFerUtil.KEY_USER_ID,""+response.get(i).getUserId());
            editor.putString(MyFerUtil.KEY_FULLNAME,""+response.get(i).getUserFullname());
            editor.putString(MyFerUtil.KEY_USER_NAME,""+response.get(i).getUserName());
            editor.putString(MyFerUtil.KEY_USER_EMAIL,""+response.get(i).getUserEmail());
            editor.putString(MyFerUtil.KEY_USER_PW,""+response.get(i).getUserPw());
            editor.putString(MyFerUtil.KEY_PASSWORD_SALT,""+response.get(i).getPasswordSalt());
            editor.putString(MyFerUtil.KEY_USER_PERMIT,""+response.get(i).getUserPermit());
            editor.putString(MyFerUtil.KEY_USER_CREATEDTE,""+response.get(i).getUserCreatedate());
            editor.putString(MyFerUtil.KEY_POLE_OWNER_ID,""+response.get(i).getPoleOwnerId());
            editor.putString(MyFerUtil.KEY_STREET,""+response.get(i).getStreet());
            editor.putString(MyFerUtil.KEY_STREETNUMBER,""+response.get(i).getStreetnumber());
            editor.putString(MyFerUtil.KEY_CITY,""+response.get(i).getCity());
            editor.putString(MyFerUtil.KEY_COMPANY,""+response.get(i).getCompany());
            editor.putString(MyFerUtil.KEY_MOBILE,""+response.get(i).getMobile());
            editor.putString(MyFerUtil.KEY_FAX,""+response.get(i).getFax());
            editor.putString(MyFerUtil.KEY_BIC,""+response.get(i).getBIC());
            editor.putString(MyFerUtil.KEY_IBN,""+response.get(i).getIBAN());
            editor.putString(MyFerUtil.KEY_ROLES,""+response.get(i).getRoles());
            editor.putString(MyFerUtil.KEY_REMEMBER_TOKEN,""+response.get(i).getRememberToken());
            editor.putString(MyFerUtil.KEY_EMAIL_CONFIRM,""+response.get(i).getEmailConfirm());
            editor.commit();


            try {
                jsonObject.put(MyFerUtil.KEY_USER_ID,response.get(i).getUserId());
                jsonObject.put(MyFerUtil.KEY_FULLNAME,response.get(i).getUserFullname());
                jsonObject.put(MyFerUtil.KEY_USER_NAME,response.get(i).getUserName());
                jsonObject.put(MyFerUtil.KEY_USER_EMAIL,response.get(i).getUserEmail());
                jsonObject.put(MyFerUtil.KEY_USER_PW,response.get(i).getUserPw());
                jsonObject.put(MyFerUtil.KEY_PASSWORD_SALT,response.get(i).getPasswordSalt());
                jsonObject.put(MyFerUtil.KEY_USER_PERMIT,response.get(i).getUserPermit());
                jsonObject.put(MyFerUtil.KEY_USER_CREATEDTE,response.get(i).getUserCreatedate());
                jsonObject.put(MyFerUtil.KEY_POLE_OWNER_ID,response.get(i).getPoleOwnerId());
                jsonObject.put(MyFerUtil.KEY_STREET,response.get(i).getStreet());
                jsonObject.put(MyFerUtil.KEY_STREETNUMBER,response.get(i).getStreetnumber());
                jsonObject.put(MyFerUtil.KEY_CITY,response.get(i).getCity());
                jsonObject.put(MyFerUtil.KEY_COMPANY,response.get(i).getCompany());
                jsonObject.put(MyFerUtil.KEY_MOBILE,response.get(i).getMobile());
                jsonObject.put(MyFerUtil.KEY_FAX,response.get(i).getFax());
                jsonObject.put(MyFerUtil.KEY_BIC,response.get(i).getBIC());
                jsonObject.put(MyFerUtil.KEY_IBN,response.get(i).getIBAN());
                jsonObject.put(MyFerUtil.KEY_ROLES,response.get(i).getRoles());
                jsonObject.put(MyFerUtil.KEY_REMEMBER_TOKEN,response.get(i).getRememberToken());
                jsonObject.put(MyFerUtil.KEY_EMAIL_CONFIRM,response.get(i).getEmailConfirm());

                //put json
                arr.put(jsonObject);
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }

        }

        return arr.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                // do login
                do_login();
                break;
            case  R.id.btn_register:
                // do register
                do_register();
                break;
        }

    }
}
