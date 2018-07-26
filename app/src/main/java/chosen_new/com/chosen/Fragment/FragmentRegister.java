package chosen_new.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackFacebookRegis;
import chosen_new.com.chosen.Api.CallbackRegisListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.FbRegisModel;
import chosen_new.com.chosen.Model.RegisterModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class FragmentRegister extends Fragment implements View.OnClickListener {

    private String TAG = "<FragmentRegister>";

    //new object for use authentication facebook
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private Context context;
    private ProgressDialog progressDialog;
    private String facebook_id = "";
    private String name = "";
    private String email = "";
    private SharedPreferences sh;

    private FragmentManager fragmentManager;


    //new Object  widget
    private EditText et_fullname,et_username,et_password,et_email,et_tel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);

        initInstance(v);

        return v;
    }

    private void initInstance(View v){

        context = getContext();
        //bind widget
        et_fullname = v.findViewById(R.id.et_fullname);
        et_email = v.findViewById(R.id.et_input_email);
        et_username = v.findViewById(R.id.et_input_username);
        et_password = v.findViewById(R.id.et_input_password);
        et_tel = v.findViewById(R.id.et_input_tel);
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);

        facebook_id = sh.getString(MyFerUtil.KEY_FACEBOOK_ID,"");
        email = sh.getString(MyFerUtil.KEY_FACEBOOK_EMAIL,"");
        name = sh.getString(MyFerUtil.KEY_FACEBOOK_NAME,"");

        if(!email.isEmpty() && !name.isEmpty()){
            et_username.setText(name);
            et_email.setText(email);
            et_fullname.setText(name);
        }


        v.findViewById(R.id.btn_confirm).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);

        //facebook logout
//        LoginManager.getInstance().logOut();

        //init fragment
        fragmentManager = getActivity().getSupportFragmentManager();

        //init facebook authentication
//        callbackManager = CallbackManager.Factory.create();
//        loginButton =  v.findViewById(R.id.btn_register_btn_face);
//        loginButton.setReadPermissions("email");
//        // If using in a fragment
//        loginButton.setFragment(this);
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                final Profile profile = Profile.getCurrentProfile();
//
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
////                                Log.v("LoginActivity", response.toString());
//                                Log.v("LoginActivity", response.toString());
//                                Log.e(TAG,profile.getId()+" , "+profile.getName()+" "+profile.getProfilePictureUri(12,31));
//
//                                // Application code
//                                try {
//                                    String id = object.getString("id");
//                                    String email = object.getString("email");
//                                    String name = object.getString("name"); //
//
//                                    et_email.setText(email);
//                                    et_fullname.setText(name);
//                                    et_username.setText(email);
//                                    facebook_id = id;
////                                    Log.e(TAG,object.toString());
////                                    showProgress();
//
////                                    //api register by facebook
////                                    new NetworkConnectionManager().callFbRegis(listener,id,email,name,email,"","");
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//
//
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Toast.makeText(context, "on Cancel", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
////                Toast.makeText(context, "On Exit ", Toast.LENGTH_SHORT).show();
//                Log.e(TAG,exception.getMessage());
//            }
//        });


//        et_tel
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();
    }

    //callback from server api register by facebook
    CallbackFacebookRegis listener = new CallbackFacebookRegis() {
        @Override
        public void onResponse(List<FbRegisModel> res) {

            Log.e(TAG,new Gson().toJson(res));

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            fragmentManager.popBackStack();
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+responseBodyError);
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"Null");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+t.getMessage());
        }
    };


    CallbackRegisListener regisListener = new CallbackRegisListener() {
        @Override
        public void onResponse(RegisterModel res) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(res.getState().equals("register success")){
                fragmentManager.popBackStack();
                Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(context, ""+getString(R.string.registerFail), Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void do_confirm(){

        String fullname = et_fullname.getText().toString().trim();
        String usr = et_username.getText().toString().trim();
        String pwd = et_password.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String tel = et_tel.getText().toString().trim();


            if(!fullname.isEmpty() && !usr.isEmpty() && !pwd.isEmpty()  && !email.isEmpty()  && !tel.isEmpty()){

                showProgress();

                if(!facebook_id.isEmpty() && facebook_id.length()>5){
                    //api register by facebook
                    new NetworkConnectionManager().callFbRegis(listener,facebook_id,email,usr,email,tel,pwd);
                    Toast.makeText(context, " callFbRegis ", Toast.LENGTH_SHORT).show();



                }else {
                        //api register normal
                    new NetworkConnectionManager().callRegisterNormal(regisListener,fullname,usr,pwd,email,tel);


                }
            }else {

                if(fullname.isEmpty())
                    et_fullname.setError(getString(R.string.errInput));
                if(usr.isEmpty())
                    et_username.setError(getString(R.string.errInput));
                if(pwd.isEmpty())
                    et_password.setError(getString(R.string.errInput));
                if(email.isEmpty())
                    et_email.setError(getString(R.string.errInput));
                if(tel.isEmpty())
                    et_tel.setError(getString(R.string.errInput));

            }

       }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                do_confirm();
//                    Toast.makeText(context, "Confirm Register", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                    fragmentManager.popBackStack();
                break;

        }
    }
}
