package chosen_new.com.chosen.Fragment;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackFacebookLogin;
import chosen_new.com.chosen.Api.CallbackLoginListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.MainApplication;
import chosen_new.com.chosen.Model.FbLoginModel;
import chosen_new.com.chosen.Model.LoginModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
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

//    ImageView img;

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

//        img = v.findViewById(R.id.imgLogo);

//        img.setImageDrawable(getActivity().getDrawable(R.drawable.fully));

        et_usr.setText("chosen");
        et_pwd.setText("chosen@2018");

        //facebook logout
        LoginManager.getInstance().logOut();

        //init sharedPer
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        //bind widget button
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);

        // get context
        context = getContext();


        //set dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));


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
                final Profile profile = Profile.getCurrentProfile();
                new NetworkConnectionManager().callFbLogin(listenerFacebook,profile.getId());
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

        //get key hash
//        try {
//            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
//                    getActivity().getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }

    }


    private void do_login(){

        try {


            String usr = et_usr.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if(!usr.isEmpty() && !pwd.isEmpty()){

               showLoading();
                new NetworkConnectionManager().callLogin(listener,usr,pwd);

            }else {
                if (usr.isEmpty())
                    Toast.makeText(context, "Please input Username.", Toast.LENGTH_SHORT).show();
                if (pwd.isEmpty())
                    Toast.makeText(context, "Please input Password.", Toast.LENGTH_SHORT).show();
            }


//            Toast.makeText(context, "Login btn", Toast.LENGTH_SHORT).show();
            //call api

        }catch (Exception e){
            Toast.makeText(context, "Call Server Error.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showLoading(){
        //show progress dialog
        progressDialog.show();
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
//        Toast.makeText(context, ""+data.getDataString(), Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        String user = sh.getString(MyFerUtil.KEY_USERNAME_KEEP,"");
        String pass = sh.getString(MyFerUtil.KEY_PASSWORD_KEEP,"");
        if(!user.isEmpty() && !pass.isEmpty()){
//            Toast.makeText(context, "Wellcome", Toast.LENGTH_SHORT).show();

            et_usr.setText(user);
            et_pwd.setText(pass);

            new NetworkConnectionManager().callLogin(listener,user,pass);

        }

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

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            //save to session
            String jsonResult =  saveResponse(res);

//            Toast.makeText(context, ""+res.get(0).getState(), Toast.LENGTH_SHORT).show();
            if(res.get(0).getState().equals("Login Success")){

                editor.putString(MyFerUtil.KEY_USERNAME_KEEP,et_usr.getText().toString());
                editor.putString(MyFerUtil.KEY_PASSWORD_KEEP,et_pwd.getText().toString());
                editor.commit();

                Intent intent = new Intent(getActivity(), MainApplication.class);
                intent.putExtra(MainApplication.KEY_DATA_USER, jsonResult);
                startActivity(intent);
                getActivity().finish();
            }


        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Log.d(TAG,"onBodyError "+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Log.d(TAG,"onBodyErrorIsNull");
        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
                Toast.makeText(context, "Login fail Please check Username or Password", Toast.LENGTH_SHORT).show();
            }

            Log.e(TAG,""+t.getMessage());
        }
    };


    //callback facebook Login
    CallbackFacebookLogin listenerFacebook = new CallbackFacebookLogin() {
        @Override
        public void onResponse(List<FbLoginModel> res) {


            //save to session
            String jsonResult =  saveResponseFacebook(res);

            Intent intent = new Intent(getActivity(), MainApplication.class);
            intent.putExtra(MainApplication.KEY_DATA_USER, jsonResult);
            startActivity(intent);
            getActivity().finish();

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
//            if(progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }

        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

        }

        @Override
        public void onFailure(Throwable t) {
            Log.d(TAG,t.getMessage());
//            if(progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }

        }
    };

    private String saveResponse(List<LoginModel> response){


//        JSONArray arr = new JSONArray();
//        for(int i = 0 ;i<response.size();i++) {
//            JSONObject jsonObject = new JSONObject();
//            Log.e(TAG, response.get(i).toString());
            int i = 0;
            editor.putString(MyFerUtil.KEY_USER_ID, "" + response.get(i).getUserId());
            editor.putString(MyFerUtil.KEY_FULLNAME, "" + response.get(i).getUserFullname());
            editor.putString(MyFerUtil.KEY_USER_NAME, "" + response.get(i).getUserName());
            editor.putString(MyFerUtil.KEY_USER_EMAIL, "" + response.get(i).getUserEmail());
            editor.putString(MyFerUtil.KEY_USER_PW, "" + response.get(i).getUserPw());
            editor.putString(MyFerUtil.KEY_PASSWORD_SALT, "" + response.get(i).getPasswordSalt());
            editor.putString(MyFerUtil.KEY_USER_PERMIT, "" + response.get(i).getUserPermit());
            editor.putString(MyFerUtil.KEY_USER_CREATEDTE, "" + response.get(i).getUserCreatedate());
            editor.putString(MyFerUtil.KEY_POLE_OWNER_ID, "" + response.get(i).getPoleOwnerId());
            editor.putString(MyFerUtil.KEY_STREET, "" + response.get(i).getStreet());
            editor.putString(MyFerUtil.KEY_STREETNUMBER, "" + response.get(i).getStreetnumber());
            editor.putString(MyFerUtil.KEY_CITY, "" + response.get(i).getCity());
            editor.putString(MyFerUtil.KEY_COMPANY, "" + response.get(i).getCompany());
            editor.putString(MyFerUtil.KEY_MOBILE, "" + response.get(i).getMobile());
            editor.putString(MyFerUtil.KEY_FAX, "" + response.get(i).getFax());
            editor.putString(MyFerUtil.KEY_BIC, "" + response.get(i).getBIC());
            editor.putString(MyFerUtil.KEY_IBN, "" + response.get(i).getIBAN());
            editor.putString(MyFerUtil.KEY_ROLES, "" + response.get(i).getRoles());
            editor.putString(MyFerUtil.KEY_REMEMBER_TOKEN, "" + response.get(i).getRememberToken());
            editor.putString(MyFerUtil.KEY_EMAIL_CONFIRM, "" + response.get(i).getEmailConfirm());
            editor.commit();
//        }

//
//            try {
//                jsonObject.put(MyFerUtil.KEY_USER_ID,response.get(i).getUserId());
//                jsonObject.put(MyFerUtil.KEY_FULLNAME,response.get(i).getUserFullname());
//                jsonObject.put(MyFerUtil.KEY_USER_NAME,response.get(i).getUserName());
//                jsonObject.put(MyFerUtil.KEY_USER_EMAIL,response.get(i).getUserEmail());
//                jsonObject.put(MyFerUtil.KEY_USER_PW,response.get(i).getUserPw());
//                jsonObject.put(MyFerUtil.KEY_PASSWORD_SALT,response.get(i).getPasswordSalt());
//                jsonObject.put(MyFerUtil.KEY_USER_PERMIT,response.get(i).getUserPermit());
//                jsonObject.put(MyFerUtil.KEY_USER_CREATEDTE,response.get(i).getUserCreatedate());
//                jsonObject.put(MyFerUtil.KEY_POLE_OWNER_ID,response.get(i).getPoleOwnerId());
//                jsonObject.put(MyFerUtil.KEY_STREET,response.get(i).getStreet());
//                jsonObject.put(MyFerUtil.KEY_STREETNUMBER,response.get(i).getStreetnumber());
//                jsonObject.put(MyFerUtil.KEY_CITY,response.get(i).getCity());
//                jsonObject.put(MyFerUtil.KEY_COMPANY,response.get(i).getCompany());
//                jsonObject.put(MyFerUtil.KEY_MOBILE,response.get(i).getMobile());
//                jsonObject.put(MyFerUtil.KEY_FAX,response.get(i).getFax());
//                jsonObject.put(MyFerUtil.KEY_BIC,response.get(i).getBIC());
//                jsonObject.put(MyFerUtil.KEY_IBN,response.get(i).getIBAN());
//                jsonObject.put(MyFerUtil.KEY_ROLES,response.get(i).getRoles());
//                jsonObject.put(MyFerUtil.KEY_REMEMBER_TOKEN,response.get(i).getRememberToken());
//                jsonObject.put(MyFerUtil.KEY_EMAIL_CONFIRM,response.get(i).getEmailConfirm());
//
//                //put json
//                arr.put(jsonObject);
//            } catch (JSONException e) {
//                Log.e(TAG,e.getMessage());
//            }



        return new Gson().toJson(response);
    }

    private String saveResponseFacebook(List<FbLoginModel> response){


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
