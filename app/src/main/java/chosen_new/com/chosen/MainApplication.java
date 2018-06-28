package chosen_new.com.chosen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import chosen_new.com.chosen.Api.CallbackAddcardListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Fragment.AboutUsFragment;
import chosen_new.com.chosen.Fragment.CardFragment;
import chosen_new.com.chosen.Fragment.FragmentInvoice;
import chosen_new.com.chosen.Fragment.FragmentManageCard;
import chosen_new.com.chosen.Fragment.FragmentRegisterCard;
import chosen_new.com.chosen.Fragment.FragmentStateCharting;
import chosen_new.com.chosen.Fragment.FragmentViewCard;
import chosen_new.com.chosen.Fragment.HomeFragment;
import chosen_new.com.chosen.Fragment.InvoiceFragment;
import chosen_new.com.chosen.Fragment.PaypalFragment;
import chosen_new.com.chosen.Fragment.PoleFragment;
import chosen_new.com.chosen.Fragment.ReportCardFragment;
import chosen_new.com.chosen.Fragment.ReportPoleFragment;
import chosen_new.com.chosen.Model.AddCardModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainApplication extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String KEY_DATA_USER = "KEY_DATA_USER";

    private String dataUser;
    private UserModel userModel;
    private ViewPager viewpager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tv_name;
    private Context context;
    private Toolbar toolbar;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;
    private String nameUser = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application);
        initInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initInstance(){

        Configuration config = new Configuration();
        config.locale = Locale.ITALIAN;
        getResources().updateConfiguration(config, null);
//        onCreate(null);
        context = MainApplication.this;
        //init session
        sh = getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();
        nameUser = sh.getString(MyFerUtil.KEY_USER_NAME,"");
        toolbar = findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        dataUser =  extras.getString(KEY_DATA_USER);
        toolbar.setTitle("HOME");
        toolbar.setTitleTextColor(getColor(R.color.text));

        setSupportActionBar(toolbar);

        viewpager =  findViewById(R.id.viewpager);
        drawer = findViewById(R.id.drawer_layout);

        //setNavigation bar
        setNavigationBar();

        //set wallet here
        tv_name.setText(nameUser);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();
        setDataUser();

    }

    private void setNavigationBar(){

        drawer.setStatusBarBackgroundColor(getColor(R.color.text));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dehaze_black_24dp));

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.tv_name);

    }

    private void setDataUser(){
        try {

            JSONArray data = new JSONArray(dataUser);
            JSONObject object = data.getJSONObject(0);
            userModel = new UserModel();

            userModel.setUser_id(object.optInt("user_id"));
            userModel.setUser_fullname(object.optString("user_fullname"));
            userModel.setUser_name(object.optString("user_name"));
            userModel.setUser_email(object.optString("user_email"));
            userModel.setUser_pw(object.optString("user_pw"));
            userModel.setPassword_salt(object.optString("password_salt"));
            userModel.setUser_permit(object.optString("user_permit"));
            userModel.setUser_createdate(object.optString("user_createdate"));
            userModel.setPole_owner_id(object.optString("pole_owner_id"));
            userModel.setCard_holder_id(object.optString("card_holder_id"));
            userModel.setStreet(object.optString("street"));
            userModel.setStreetnumber(object.optString("streetnumber"));
            userModel.setPostalcode(object.optString("postalcode"));
            userModel.setCity(object.optString("city"));
            userModel.setCompany(object.optString("company"));
            userModel.setMobile(object.optString("mobile"));
            userModel.setFax(object.optString("fax"));
            userModel.setBIC(object.optString("BIC"));
            userModel.setIBAN(object.optString("IBAN"));
            userModel.setReference(object.optString("reference"));
            userModel.setRoles(object.optString("Roles"));

        } catch (JSONException e){
            e.printStackTrace();
        }

        setupViewPager(viewpager);
        viewpager.setOffscreenPageLimit(8);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int co = fragmentManager.getBackStackEntryCount();  //get fragment number in backStack
        switch (id){
            case R.id.nav_home:
                toolbar.setTitle(getString(R.string.menu_home));
                setSupportActionBar(toolbar);

                setNavigationBar();
                try{
                    while (co>0){
                        fragmentManager.popBackStack();
                        co--;
                    }

                    String getPage = sh.getString(MyFerUtil.KEY_PAGE_NOW,"");
                    if(getPage.equals(FragmentStateCharting.TAG)){
                        fragmentManager.popBackStack();
                    }
                }catch (Exception e){

                }
                viewpager.setCurrentItem(0);

                break;

            case R.id.nav_card:
                toolbar.setTitle(R.string.menu_card);
                setSupportActionBar(toolbar);
                setNavigationBar();

                clearFragment();

                FragmentManageCard fragmentManageCard = new FragmentManageCard();
                fragmentTran(fragmentManageCard,null);

                break;

            case R.id.nav_state_charge:
                toolbar.setTitle(getString(R.string.menu_charge));
                clearFragment();
                setSupportActionBar(toolbar);
                setNavigationBar();

                FragmentViewCard fragmentViewCard = new FragmentViewCard();
                fragmentTran(fragmentViewCard,null);

                break;


//            case R.id.nav_station:
//                viewpager.setCurrentItem(2);  // Out of scope
//                break;
//
//            case R.id.nav_profile:
//                viewpager.setCurrentItem(2);// Out of scope
//                break;
//
//            case R.id.nav_wallet:
//                viewpager.setCurrentItem(2);// Out of scope
//                break;
//
            case R.id.nav_paypal:

                toolbar.setTitle(getString(R.string.menu_payment));
                setSupportActionBar(toolbar);
                setNavigationBar();
                clearFragment();

                PaypalFragment paypalFragment = new PaypalFragment();
                fragmentTran(paypalFragment,null);

                break;
            case R.id.nav_invoice:
                toolbar.setTitle(getString(R.string.menu_invoice));
                setSupportActionBar(toolbar);
                setNavigationBar();

                clearFragment();
                fragmentTran(new FragmentInvoice(),null);

                break;
//            case R.id.nav_usage_detail:
//
//                viewpager.setCurrentItem(2);// Out of scope
//
//                break;
//
//            case R.id.nav_service:
//                viewpager.setCurrentItem(2);// Out of scope
//                break;

            case R.id.nav_logout:
                    do_logout();
                break;

        }
//        if (id == R.id.nav_home) {
//            viewpager.setCurrentItem(0);
//
////            int count  = getSupportFragmentManager().getBackStackEntryCount();
////            Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
//
//
//        } else if (id == R.id.nav_station) {
//
//            viewpager.setCurrentItem(1);
//        } else if (id == R.id.nav_profile) {
//            viewpager.setCurrentItem(2);
//        } else if(id == R.id.nav_wallet){
//            viewpager.setCurrentItem(3);
//        } else if(id == R.id.nav_usage_detail){
//            viewpager.setCurrentItem(4);
//        } else if(id == R.id.nav_service){
//            viewpager.setCurrentItem(5);
//        } else if(id == R.id.nav_logout){
//            do_logout();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearFragment(){
        int co = fragmentManager.getBackStackEntryCount();  //get fragment number in backStack

        while (co>0){
            fragmentManager.popBackStack();
            co--;
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(MainApplication.this, getSupportFragmentManager());
        adapter.addFrag(HomeFragment.newInstance(userModel), "ONE");
        adapter.addFrag(new CardFragment().newInstance(userModel), "TWO");
        adapter.addFrag(new FragmentViewCard().newInstance(userModel), "EIGHT");
//        adapter.addFrag(new FragmentRegisterCard().newInstance(userModel), "THREE");
//        adapter.addFrag(new PoleFragment().newInstance(userModel), "THREE");
//        adapter.addFrag(new PaypalFragment().newInstance(userModel), "FOUR");
//        adapter.addFrag(new ReportPoleFragment().newInstance(userModel), "FIVE");
//        adapter.addFrag(new AboutUsFragment().newInstance(userModel), "SIX");
//        adapter.addFrag(new InvoiceFragment().newInstance(userModel), "SEVEN");
//

        viewPager.setAdapter(adapter);

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

    private void do_logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.msgLogout));

        builder.setPositiveButton(getString(R.string.confirm_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //facebook logout
                LoginManager.getInstance().logOut();

                //editor clear
                editor.clear();
                editor.commit();

                startActivity(new Intent(MainApplication.this,MainActivity.class));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_settings:
                break;
//            case R.id.friends:
//                break;
//            case R.id.about:
//                break;
        }
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context context;
        private int [] tabIcons;
        private int [] TabText;

        public ViewPagerAdapter(Context context, FragmentManager manager) {

            super(manager);
            this.context = context;
            this.tabIcons = tabIcons;
            this.TabText = TabText;

        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        int co = fragmentManager.getBackStackEntryCount();  //get fragment number in backStack

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(co == 2){  //check fragment status charge
               fragmentManager.popBackStack();  // kill fragment status charge

            }else if(co == 3){
                fragmentManager.popBackStack();  // kill fragment status charge
            }
            else {
                do_logout();
            }

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");

            } else {

                String userId  = sh.getString(MyFerUtil.KEY_USER_ID,"");
                new NetworkConnectionManager().callAddcard(listener,userId,result.getContents());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    CallbackAddcardListener listener = new CallbackAddcardListener() {
        @Override
        public void onResponse(AddCardModel res) {

                Toast.makeText(context, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                FragmentViewCard viewCard = new FragmentViewCard();
                viewCard.newInstance(userModel);

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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
