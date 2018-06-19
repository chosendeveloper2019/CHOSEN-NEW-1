package chosen_new.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import okhttp3.ResponseBody;

public class FragmentRegister extends Fragment implements View.OnClickListener {

    private String TAG = "<FragmentRegister>";

    //new object for use authentication facebook
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private Context context;

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

        v.findViewById(R.id.btn_confirm).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);

        //facebook logout
        LoginManager.getInstance().logOut();

        //init fragment
        fragmentManager = getActivity().getSupportFragmentManager();

        //init facebook authentication
        callbackManager = CallbackManager.Factory.create();
        loginButton =  v.findViewById(R.id.btn_register_btn_face);
        loginButton.setReadPermissions( Arrays.asList("user_status"));
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final Profile profile = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {

                                    String email = object.getString("email");
                                    String name = object.getString("name"); //

                                    et_email.setText(email);
                                    et_fullname.setText(name);
                                    Log.e(TAG,"ID:"+profile.getId());
                                    new NetworkConnectionManager().callFbRegis(listener,profile.getId(),email,name,email);


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
                Toast.makeText(context, "on Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
//                Toast.makeText(context, "On Exit ", Toast.LENGTH_SHORT).show();
                Log.e(TAG,exception.getMessage());
            }
        });


//        et_tel
    }

    CallbackFacebookRegis listener = new CallbackFacebookRegis() {
        @Override
        public void onResponse(List<FbRegisModel> res) {
            fragmentManager.popBackStack();
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,""+responseBodyError);
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG,"Null");
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG,""+t.getMessage());
        }
    };

    CallbackRegisListener regisListener = new CallbackRegisListener() {
        @Override
        public void onResponse(RegisterModel res) {
            if(res.getState().equals("register success")){
                fragmentManager.popBackStack();
                Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show();
            }
            ;

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

        }

        @Override
        public void onBodyErrorIsNull() {

        }

        @Override
        public void onFailure(Throwable t) {

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
            new NetworkConnectionManager().callRegisterNormal(regisListener,fullname,usr,pwd,email,tel);
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
