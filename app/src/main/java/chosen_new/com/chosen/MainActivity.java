package chosen_new.com.chosen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import chosen_new.com.chosen.Fragment.FragmentLogin;
import chosen_new.com.chosen.Fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Get_hash_key();

    }
    public void Get_hash_key() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("chosen_new.com.chosen", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
    private void init(){
        //init
        fragmentManager = getSupportFragmentManager();

        //require permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //go fragment login save to back stack
        FragmentLogin fragmentLogin = new FragmentLogin();
        fragmentTran(fragmentLogin,null);
    }


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
}
