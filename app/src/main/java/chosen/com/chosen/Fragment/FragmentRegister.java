package chosen.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

import chosen.com.chosen.R;

public class FragmentRegister extends Fragment implements View.OnClickListener {
    //new object for use authentication facebook
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;

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

//        et_tel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
