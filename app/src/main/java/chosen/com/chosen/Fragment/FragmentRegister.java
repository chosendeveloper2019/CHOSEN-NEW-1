package chosen.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import chosen.com.chosen.R;

public class FragmentRegister extends Fragment implements View.OnClickListener {

    private String TAG = "<FragmentRegister>";

    //new object for use authentication facebook
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private Context context;


    private ProgressDialog progressDialog;

    //new Object  widget
    private EditText et_name,et_sername,et_email,et_tel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);

        initInstance(v);

        return v;
    }

    private void initInstance(View v){

        et_name = v.findViewById(R.id.et_input_name);
        et_sername =v.findViewById(R.id.et_input_sername);
        et_email = v.findViewById(R.id.et_input_email);

        //init facebook authentication
        callbackManager = CallbackManager.Factory.create();
        loginButton =  v.findViewById(R.id.btn_register_btn_face);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final Profile profile = Profile.getCurrentProfile();

                et_name.setText(profile.getFirstName());
                et_sername.setText(profile.getLastName());


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
                                    et_name.setText(name);


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
            }
        });


//        et_tel
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
