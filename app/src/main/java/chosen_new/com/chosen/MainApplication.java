package chosen_new.com.chosen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackAddcardListener;
import chosen_new.com.chosen.Api.CallbackProfileListener;
import chosen_new.com.chosen.Api.CallbackUpdateProfileListener;
import chosen_new.com.chosen.Api.CallbackuploadpicListenner;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Fragment.AboutUsFragment;
import chosen_new.com.chosen.Fragment.CardFragment;
import chosen_new.com.chosen.Fragment.FragmentInvoice;
import chosen_new.com.chosen.Fragment.FragmentManageCard;
import chosen_new.com.chosen.Fragment.FragmentStateCharting;
import chosen_new.com.chosen.Fragment.FragmentViewCard;
import chosen_new.com.chosen.Fragment.HomeFragment;
import chosen_new.com.chosen.Fragment.PaypalFragment;
import chosen_new.com.chosen.Fragment.ReportCardFragment;
import chosen_new.com.chosen.Model.AddCardModel;
import chosen_new.com.chosen.Model.ProfileModel;
import chosen_new.com.chosen.Model.UpdateProfileModel;
import chosen_new.com.chosen.Model.UploadImageModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.Util.MyFerUtil;
import chosen_new.com.chosen.Util.UrlUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainApplication extends LocalizationActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    private AlertDialog alertDialog,alertEditProfile;

    private  BottomNavigationView bottomNavigationView;
    private Menu menu ;
    private MenuItem menuItem ;
    private String uid;
    private String TAG ="MainApplication";
    private String path;
    private File file;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    MultipartBody.Part body;
    RequestBody name;

    // select image from gallery
    private ImagePicker imagePicker;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            openFilePicker();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_application);
        setContentView(R.layout.bottom_main_app);
//        setLanguage("th");
//        onCreate(null);
        initInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initInstance(){

//        onCreate(null);
        context = MainApplication.this;
        //init session
        sh = getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();
        nameUser = sh.getString(MyFerUtil.KEY_USER_NAME,"");
        toolbar = findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        dataUser =  extras.getString(KEY_DATA_USER);
        toolbar.setTitle(getString(R.string.menu_map));
        toolbar.setTitleTextColor(getColor(R.color.text));
        uid = sh.getString(MyFerUtil.KEY_USER_ID, null);
        setSupportActionBar(toolbar);

        viewpager =  findViewById(R.id.viewpager);
//        drawer = findViewById(R.id.drawer_layout);

        //setNavigation bar
        setNavigationBar();

        requestPermission();

        //set wallet here
//        tv_name.setText(nameUser);
//        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();

        setDataUser();

        imagePicker = new ImagePicker(MainApplication.this);


    }


    private void setNavigationBar(){

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.my_bottom_nav_menu);
        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.bottom_nav_item_color));
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bottom_nav_item_color));
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
//        bottomNavigationView.
        menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                int co = fragmentManager.getBackStackEntryCount();  //get fragment number in backStack


                switch (id){
                    case R.id.nav_home:
                        toolbar.setTitle(getString(R.string.menu_map));

                        bottomNavigationView.getMenu().getItem(0).setCheckable(true);

                        setSupportActionBar(toolbar);

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
//                        fragmentTran(new HomeFragment(),null);

                        return true;

                    case R.id.nav_card:

                        toolbar.setTitle(R.string.menu_card);

                        bottomNavigationView.getMenu().getItem(1).setCheckable(true);

                        setSupportActionBar(toolbar);
//                        setNavigationBar();

                        clearFragment();

                        FragmentManageCard fragmentManageCard = new FragmentManageCard();
                        fragmentTran(fragmentManageCard,null);

                        return true;

                    case R.id.nav_state_charge:

                        toolbar.setTitle(getString(R.string.menu_charge));
                        bottomNavigationView.getMenu().getItem(2).setCheckable(true);

                        clearFragment();
                        setSupportActionBar(toolbar);
//                        setNavigationBar();

                        FragmentViewCard fragmentViewCard = new FragmentViewCard();
                        fragmentTran(fragmentViewCard,null);


                    return true;

                    case R.id.nav_report:

                        toolbar.setTitle(getString(R.string.menu_report));
                        setSupportActionBar(toolbar);
//                        setNavigationBar();
                        bottomNavigationView.getMenu().getItem(4).setCheckable(true);


                        clearFragment();
                        reportGraph();
//                        fragmentTran(new FragmentInvoice(),null);


                        return true;

                    case R.id.nav_payment:
                        bottomNavigationView.getMenu().getItem(3).setCheckable(true);

                        toolbar.setTitle(getString(R.string.menu_tran));
                        bottomNavigationView.getMenu().findItem(R.id.nav_payment).setChecked(true);

                        setSupportActionBar(toolbar);
//                        setNavigationBar();
                        clearFragment();

                        PaypalFragment paypalFragment = new PaypalFragment();
                        fragmentTran(paypalFragment,null);


                    return true;



                }

                return false;

            }
        });


//        drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.text));
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this,
//                drawer,
//                toolbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        toggle.syncState();
//        drawer.setDrawerListener(toggle);
//
//        toggle.syncState();

//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dehaze_black_24dp));

////        navigationView = findViewById(R.id.nav_view);
//        View headerView = navigationView.getHeaderView(0);
//        tv_name = headerView.findViewById(R.id.tv_name);

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
                toolbar.setTitle(getString(R.string.menu_map));

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

            case R.id.nav_invoice:
                toolbar.setTitle(getString(R.string.menu_report));
                setSupportActionBar(toolbar);
                setNavigationBar();

                clearFragment();
                reportGraph();
//                fragmentTran(new FragmentInvoice(),null);

                break;

//
            case R.id.nav_payment:

                toolbar.setTitle(getString(R.string.menu_tran));
                setSupportActionBar(toolbar);
                setNavigationBar();
                clearFragment();

                PaypalFragment paypalFragment = new PaypalFragment();
                fragmentTran(paypalFragment,null);

                break;



//            case R.id.nav_logout:
//                    do_logout();
//                break;

        }

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
//        adapter.addFrag(new CardFragment().newInstance(userModel), "TWO");
//        adapter.addFrag(new FragmentViewCard().newInstance(userModel), "EIGHT");
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
        frgTran.replace(R.id.container, fragment).addToBackStack(null).commit();

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
                    ChangeLanguage();
                break;
            case R.id.action_invoice:
                    clearFragment();
                    fragmentTran(new FragmentInvoice(),null);
                break;
            case R.id.action_profile:
                    ProfileView();
                break;
            case R.id.action_contact_us:
                    fragmentTran(new AboutUsFragment(),null);
                break;

            case R.id.action_logout:
                    do_logout();
                break;
//            case R.id.about:
//                break;
        }
        return true;
    }

    private void reportGraph() {

        fragmentTran(new ReportCardFragment(),null);

    }

    private void ProfileView() {

      // เรียกข้อมูลจาก user/get_myaddressAsync/{user_id}
       new NetworkConnectionManager().callProfile(profileListener,uid);


    }

    private void setProfile(final String userFullname,final  String street,final  String city, final String postalcode, final String mobile,final String urlProfile) {

        //show detail for reserve
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.layout_profile, null);
        TextView tv_fullname,tv_street,tv_city,tv_postalcode,tv_mobile;
        Button btn_edit,btn_cencel;
        ImageView imageProfile = v.findViewById(R.id.profile_image);
        try {
            // แสดงรูปภาพ
            Picasso.with(context).load(UrlUtil.URL+urlProfile.substring(2)).into(imageProfile);

        }catch (Exception e){

        }

//        Toast.makeText(context, ""+urlProfile.substring(2), Toast.LENGTH_SHORT).show();

        //bind widget
        tv_fullname = v.findViewById(R.id.tv_fullname);
        tv_street = v.findViewById(R.id.tv_street);
        tv_city = v.findViewById(R.id.tv_city);
        tv_postalcode = v.findViewById(R.id.tv_postcode);
        tv_mobile = v.findViewById(R.id.tv_mobile);

        btn_edit = v.findViewById(R.id.btn_edit_profile);
        btn_cencel = v.findViewById(R.id.btn_cancel);


        //set text
        tv_fullname.setText(userFullname);
        tv_street.setText(street);
        tv_city.setText(city);
        tv_postalcode.setText(postalcode);
        tv_mobile.setText(mobile);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);

        alertDialog = builder.show();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //แก้ไข profile
                editProfile(userFullname, street, city, postalcode, mobile,urlProfile);
            }
        });

        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });


    }

    private void editProfile(String userFullname, String street, String city, String postalcode, String mobile, String urlProfile) {
        //show detail for reserve

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.layout_edit_profile, null);
        final EditText et_fullname,et_street,et_city,et_postalcode,et_mobile;
        Button btn_select_img,btn_confirm,btn_cencel;

        final ImageView imageProfile = v.findViewById(R.id.profile_image);
        try {

            Picasso.with(context).load(UrlUtil.URL+urlProfile.substring(2)).into(imageProfile);

        }catch (Exception e){

        }

//        Toast.makeText(context, ""+urlProfile.substring(2), Toast.LENGTH_SHORT).show();

        //bind widget
        et_fullname = v.findViewById(R.id.et_fullname);
        et_street = v.findViewById(R.id.et_street);
        et_city = v.findViewById(R.id.et_city);
        et_postalcode = v.findViewById(R.id.et_postcode);
        et_mobile = v.findViewById(R.id.et_mobile);

        btn_select_img = v.findViewById(R.id.btn_select_img);
        btn_confirm = v.findViewById(R.id.btn_confirm);
        btn_cencel = v.findViewById(R.id.btn_cancel);


        //set text
        et_fullname.setText(userFullname);
        et_street.setText(street);
        et_city.setText(city);
        et_postalcode.setText(postalcode);
        et_mobile.setText(mobile);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);

        alertEditProfile = builder.show();

        btn_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                                       @Override
                                                       public void onImagesChosen(List<ChosenImage> list) {

                                                           // get path and create file.
                                                            path = list.get(0).getOriginalPath();
                                                            file = new File(path);
                                                            //ตั้งค่าก่อนส่งรูปภาพ
                                                           RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                                           body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                                                           MultipartBody.Part uidBodyMulti = MultipartBody.Part.createFormData("user_id", uid);
                                                           name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

                                                           //  อัพโหลด รูปภาพ ไปยัง server user/update_image
                                                           new NetworkConnectionManager().callUploadImg(imgListener,body,uidBodyMulti);

                                                           if(file.exists()){
                                                               // เปลี่ยนรูปภาพ
                                                               Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                               imageProfile.setImageBitmap(myBitmap);
                                                           }

                                                       }

                                                       @Override
                                                       public void onError(String message) {
                                                           // Do error handling
                                                       }
                                                   }
                );

                imagePicker.pickImage();


            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname = et_fullname.getText().toString().trim();
                String street = et_street.getText().toString().trim();
                String city = et_city.getText().toString().trim();
                String postalcode  = et_postalcode.getText().toString().trim();
                String mobile = et_mobile.getText().toString().trim();

                // api อัพเดท ข้อมูลส่วนตัว
                new NetworkConnectionManager().callUpdateProfile(profileDataListener,uid,fullname,street,city,postalcode,mobile);

            }
        });

        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertEditProfile.dismiss();
            }
        });
    }


    // layout เปลี่ยนภาษา
    private void ChangeLanguage(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.layout_change_language, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);



        alertDialog = builder.show();
        Button btnSetEnglish,btnSetThai,btnSetLao;

        btnSetEnglish= v.findViewById(R.id.btn_english);
        btnSetThai  = v.findViewById(R.id.btn_thai);
        btnSetLao = v.findViewById(R.id.btn_laos);

        btnSetEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //เปลี่ยนเป็นภาษา อังกฤษ
                setLanguage("en");
                alertDialog.dismiss();
            }
        });

        btnSetThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //เปลี่ยนเป็นภาษา ไทย
                setLanguage("th");
                alertDialog.dismiss();
            }
        });

        btnSetLao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //เปลี่ยนเป็นภาษา ลาว
                setLanguage("lo");
                alertDialog.dismiss();
            }
        });

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

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);


            if(co == 2){  //check fragment status charge
               fragmentManager.popBackStack();  // kill fragment status charge

            }else if(co == 3){
                fragmentManager.popBackStack();  // kill fragment status charge
            }
            else {
                do_logout();
            }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (requestCode == Picker.PICK_IMAGE_DEVICE) {
            imagePicker.submit(intent);
        }

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

    //
    CallbackAddcardListener listener = new CallbackAddcardListener() {
        @Override
        public void onResponse(AddCardModel res) {
                // ได้รับ response จากการเพิ่มการ์ด
                Toast.makeText(context, ""+res.getMessage(), Toast.LENGTH_SHORT).show();

                clearFragment();

                FragmentManageCard fragmentManageCard = new FragmentManageCard();
                fragmentTran(fragmentManageCard,null);


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

    // ได้รับ response จาก การเรียกดูข้อมูล profile
    CallbackProfileListener profileListener = new CallbackProfileListener() {
        @Override
        public void onResponse(List<ProfileModel> res) {


            int Index = 0;
            // response จาก
            setProfile(res.get(Index).getUserFullname(),
                       res.get(Index).getStreet(),
                       res.get(Index).getCity(),
                       res.get(Index).getPostalcode(),
                       res.get(Index).getMobile(),
                       res.get(Index).getImage()
                    );

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG,"null");
        }

        @Override
        public void onFailure(Throwable t) {
//            Log.e(TAG,.source());
            t.printStackTrace();
        }
    };


    // ได้รับ response จาก การ upload รูปภาพ
    CallbackuploadpicListenner imgListener = new CallbackuploadpicListenner() {
        @Override
        public void onResponse(UploadImageModel res) {



            if(res.getState().equals("success")){

                Toast.makeText(context, ""+getString(R.string.saveSuccess), Toast.LENGTH_SHORT).show();
            }

            editor.putString(MyFerUtil.KEY_IMAGE_USER,res.getImage());
            editor.commit();


        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {
//            Log.e(TAG,""+responseBodyError.source());

            t.printStackTrace();
        }
    };

    // response จาก การอัพโหลด รปภาพ
    CallbackUpdateProfileListener profileDataListener = new CallbackUpdateProfileListener() {
        @Override
        public void onResponse(UpdateProfileModel res) {

            Log.e(TAG,new Gson().toJson(res));
            Toast.makeText(context, ""+getString(R.string.saveSuccess), Toast.LENGTH_SHORT).show();
            alertEditProfile.dismiss();
            alertDialog.dismiss();

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e(TAG,""+responseBodyError.source().toString());
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e(TAG,"Null");

        }

        @Override
        public void onFailure(Throwable t) {
          t.printStackTrace();
        }
    };




    static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        public static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

        } else {
//            openFilePicker();
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
