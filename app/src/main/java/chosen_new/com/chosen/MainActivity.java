package chosen_new.com.chosen;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackFacebookLogin;
import chosen_new.com.chosen.Api.CallbackLoginListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Fragment.FragmentLogin;
import chosen_new.com.chosen.Fragment.FragmentRegister;
import chosen_new.com.chosen.Fragment.LoginFragment;
import chosen_new.com.chosen.Model.FbLoginModel;
import chosen_new.com.chosen.Model.LoginModel;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends LocalizationActivity {
    FragmentManager fragmentManager;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    private  String TAG = "MainActivity";

    private static final int REQUEST_WRITE_PERMISSION = 786;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////            openFilePicker();
//        }
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        LoginFragment loginFragment = new LoginFragment();
////        FragmentManager manager = getSupportFragmentManager();
////        FragmentTransaction transaction = manager.beginTransaction();
////        transaction.replace(R.id.content, fragment);
////        transaction.commit();
//
//
//        fragmentTran(loginFragment,null);
        init();
//        Get_hash_key();

    }
//    public void Get_hash_key() {
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("chosen_new.com.chosen", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }
//    }
    private void init(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
//                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }else{
//            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }
        //init
        fragmentManager = getSupportFragmentManager();


        //require permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        sh = getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        String user = sh.getString(MyFerUtil.KEY_USERNAME_KEEP,"");
        String pass = sh.getString(MyFerUtil.KEY_PASSWORD_KEEP,"");
//        String facebook = sh.getString(MyFerUtil.KEY_FACEBOOK_ID,"");
        checkPermission();

        if(!user.isEmpty() && !pass.isEmpty()){
//            Toast.makeText(context, "Wellcome", Toast.LENGTH_SHORT).show();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.msgLoading));
            progressDialog.show();

            // ส่งข้อมูลการยืนยันตัวตน
            new NetworkConnectionManager().callLogin(listener,user,pass);

//        }else if(!facebook.isEmpty()){
//            new NetworkConnectionManager().callFbLogin(listenerFacebook,facebook);

        }else {

            //go fragment login save to back stack
            FragmentLogin fragmentLogin = new FragmentLogin();
            fragmentTran(fragmentLogin,null);

        }



    }
    //callback facebook Login
    CallbackFacebookLogin listenerFacebook = new CallbackFacebookLogin() {
        @Override
        public void onResponse(List<FbLoginModel> res) {


            //save to session
            String jsonResult =  saveResponseFacebook(res);

            Intent intent = new Intent(MainActivity.this, MainApplication.class);
            intent.putExtra(MainApplication.KEY_DATA_USER, jsonResult);
            startActivity(intent);
            finish();

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,responseBodyError.source().toString());
//            if(progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }

        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG," response is null ");

//            if(progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }

        }

        @Override
        public void onFailure(Throwable t) {
//
            t.printStackTrace();
//            if(progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }

        }
    };



    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }
//    public void tosignup (View view) {
//        goToUrl ( "http://app.chosenenergy.co.th/register");
//    }
//
//    private void goToUrl (String url) {
//        Uri uriUrl = Uri.parse(url);
//        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//        startActivity(launchBrowser);
//    }

private void do_exit(){

    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setMessage(getString(R.string.msgExit));

    builder.setPositiveButton(getString(R.string.confirm_dialog), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

           finish();

        }
    });

    builder.setNegativeButton(getString(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    });

    builder.show();

}
    private CallbackLoginListener listener = new CallbackLoginListener() {
        @Override
        public void onResponse(List<LoginModel> res) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            //save to session
            String jsonResult =  saveResponse(res);

//            Toast.makeText(context, ""+res.get(0).getState(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MainActivity.this, MainApplication.class);
                intent.putExtra(MainApplication.KEY_DATA_USER, jsonResult);
                startActivity(intent);
                finish();

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
                Toast.makeText(MainActivity.this, "Login fail Please check Username or Password", Toast.LENGTH_SHORT).show();
            }

            Log.e(TAG,""+t.getMessage());
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
//        boolean doRegister = sharedPreferences.getBoolean(FragmentLogin.KEY_DO_REGISTER,false);
        try {
            int co = fragmentManager.getBackStackEntryCount();
            if(co >=2){
                fragmentManager.popBackStack();
            }else {
                do_exit();
            }
        }catch (Exception e){

        }
    }
    private String saveResponse(List<LoginModel> response){

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

    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                        1);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        1);
            }
        } else {
            Toast.makeText(this, "Successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }



}
