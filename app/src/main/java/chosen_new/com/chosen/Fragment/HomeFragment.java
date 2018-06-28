package chosen_new.com.chosen.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nitrico.mapviewpager.MapViewPager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Api.CallbackHomeListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.GPS.GPSTracker;
import chosen_new.com.chosen.GPS.MapDistance;
import chosen_new.com.chosen.Model.MapModel_;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.ConnectivityReceiverUtil;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements
        ConnectivityReceiverUtil.ConnectivityReceiverListener
        , View.OnClickListener

{

    private static final String TAG = HomeFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";

    //val for get user_id
    private String uid;

    private LinearLayout linearLayout;
    private MapFragment fragment_view_map;
//    private ArrayList<MapModel> list_data_map;


    private ArrayList<MapModel_> list_data_map;  //create new model for retrofit library

    private Context context;


    // map view detail
    private ViewPager viewPager;
    private MapViewPager mvp;

    //session
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    // new Object for ProgressDialog
    ProgressDialog progressDialog;

    // new Object widget
//    private EditText et_search;
    private TextView tv_header, tv_station, tv_state, tv_detail, tv_start, tv_end, tv_share;

    private UserModel userModel;

    //get gps tracker
    private GPSTracker gps;
    private LatLng latLng;

    public static HomeFragment newInstance(UserModel userModel) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        initInstance(v);
        return v;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initInstance(view);
//
//    }

    private void initInstance(View v) {
        try {
            //bing  context
            context = getContext();
            //bind widget
            linearLayout = v.findViewById(R.id.detail);
            linearLayout.setVisibility(View.GONE);
//            et_search = v.findViewById(R.id.et_search_map);
//            et_search.setRawInputType(0); // hide key board

            tv_header = v.findViewById(R.id.tv_header_detail);
//            tv_station = v.findViewById(R.id.tv_numstation);
//            tv_detail = v.findViewById(R.id.tv_detail);
            tv_state = v.findViewById(R.id.tv_state);
//            tv_start = v.findViewById(R.id.tv_start);
//            tv_end = v.findViewById(R.id.tv_end);
            tv_share = v.findViewById(R.id.tv_share);

//            v.findViewById(R.id.btn_search).setOnClickListener(this);
//            v.findViewById(R.id.btn_reserve).setOnClickListener(this);
            v.findViewById(R.id.tv_share).setOnClickListener(this);

            //init for SharedPreferences ( SESSION )
            sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER, Context.MODE_PRIVATE);
            editor = sh.edit();

            editor.putString(MyFerUtil.KEY_PAGE_NOW,MyFerUtil.PAGE_HOME);
            editor.commit();

            //get user id from shared perferences
            uid = sh.getString(MyFerUtil.KEY_USER_ID, null);

            // Check if no view has focus:
            View view = getActivity().getCurrentFocus();

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            fragment_view_map = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_view_map);


            MapsInitializer.initialize(getActivity().getApplicationContext());

//            userModel = (UserModel) getArguments().getSerializable(KEY_DATA_USER);

            if (ConnectivityReceiverUtil.isConnected()) {

                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(getString(R.string.msgLoading));
                progressDialog.show();

                new NetworkConnectionManager().callHomeFrg(listener, uid);

            } else {
                showSnack("Sorry! Not connected to internet");
            }

            getGPS();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upDateUI(final List<MapModel_> res){

        try {
                final MapDistance map = new MapDistance();

                list_data_map = new ArrayList<>();

            for(int i = 0;i < res.size();i++){

                    MapModel_ mapModel = new MapModel_();
                    mapModel.setLat(res.get(i).getLat());
                    mapModel.setLon(res.get(i).getLon());
                    mapModel.setPoleId(res.get(i).getPoleId());
                    mapModel.setUserFullname(res.get(i).getUserFullname());
                    mapModel.setUserId(res.get(i).getUserId());
                    mapModel.setColor(res.get(i).getColor());
                    list_data_map.add(mapModel);

                    double b = map.Distance(Double.parseDouble(res.get(i).getLat()),Double.parseDouble(res.get(i).getLon())
                            ,gps.getLatitude(),gps.getLongitude(),'K');
                    Log.d(TAG+"DISTANCE","name "+res.get(i).getUserFullname()+" distance ="+b);

                }


            fragment_view_map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {


                    googleMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(Double.parseDouble(list_data_map.get(0).getLat())
                                                    ,Double.parseDouble(list_data_map.get(0).getLon())), 10f));

                    //dum add====================
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            linearLayout.setVisibility(View.GONE);

                        }
                    });

                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            double b = map.Distance(marker.getPosition().latitude
                                    ,marker.getPosition().longitude
                                    ,gps.getLatitude(),gps.getLongitude(),'K');

                            Map<Object,String> val = new HashMap<>();
                            val.put("header",marker.getTitle());
                            val.put("station","10 STATION");
                            val.put("state","Distance current: "+(float) b);
                            val.put("detail","\t- 10 TYPE 2");
                            val.put("start","NORMAL CHARGE 100 Baht/hr(11 kWh)");
                            val.put("end","PERMIUM CHARGE 300 Baht/hr(42 kWh)");
                            val.put("lat"," lat :"+marker.getPosition().longitude);
                            val.put("long"," long :"+marker.getPosition().longitude);
                            latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                            setShowDetail(val);

                            return false;
                        }
                    });


                    if(list_data_map != null){
                        for(int i = 0;i < list_data_map.size();i++){
                            //Log.w(TAG, "LOOP : " + i);

                            if(list_data_map.get(i).getColor()==101) {

                                googleMap.addMarker(
                                        new MarkerOptions().position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.p15))//p15  icon_maker
                                                .title(list_data_map.get(i).getPoleId()));
//                                                .snippet("Population: 4,137,400"));
                            }

                            if(list_data_map.get(i).getColor()==102) {

                                googleMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.p16)) //p16 icon_maker_red
                                        .title(list_data_map.get(i).getPoleId()));

                            }

                            if(list_data_map.get(i).getColor()==103) {
                                googleMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.p13))  // p13  icon_maker_blue
                                        .title(list_data_map.get(i).getPoleId()));
                            }

                        }
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();

        }
    }
    private void openGoogleMap(LatLng src, LatLng dest) {
        String url = "http://maps.google.com/maps?saddr="+src.latitude+","+src.longitude+"&daddr="+dest.latitude+","+dest.longitude+"&mode=driving";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private String getGPS(){
        try{
            JSONObject json  = new JSONObject();

            gps = new GPSTracker(context);

            if (!gps.canGetLocation()){
                gps.showSettingsAlert();
            }
            try {

                json.put("lat",gps.getLatitude());
                json.put("longi",gps.getLongitude());

//                Toast.makeText(context, ""+json.toString(), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();

        }catch(Exception e){

            Log.e("Error at getGPS() ",e.toString());

            return null;
        }
    }

    private void setShowDetail(Map<Object,String> data){

        linearLayout.setVisibility(View.VISIBLE);

        String header = data.get("header").toString();
        String station = data.get("station").toString();
        String state = data.get("state").toString();
        String detail = data.get("detail").toString();
        String start = data.get("start").toString();
        String end = data.get("end").toString();

        tv_header.setText(header);
//        tv_station.setText(station);
        tv_state.setText(state);
//        tv_detail.setText(detail);
//        tv_start.setText(start);
//        tv_end.setText(end);

    }

    private void do_refresh(){

            new NetworkConnectionManager().callHomeFrg(listener,uid);

    }

//    private void do_reserve(){
//
//        //show detail for reserve
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.layout_reserve_detail, null);
//        Button button = alertLayout.findViewById(R.id.btn_reserve);
////        TextView tv_close = alertLayout.findViewById(R.id.tv_close);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
//        builder.setView(alertLayout);
//
//        final AlertDialog alertDialog = builder.show();
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                alertDialog.dismiss();
//                Toast.makeText(context, "Reserve success", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }


    private void do_share(){
        LatLng latLngCurrent = new LatLng(gps.getLatitude(),gps.getLongitude());
        openGoogleMap(latLngCurrent,latLng);
//        Toast.makeText(context, "do_share", Toast.LENGTH_SHORT).show();;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.btn_search:
//                do_refresh();
//                break;
//            case R.id.btn_reserve:
//                do_reserve();
//                break;
            case R.id.tv_share:
                do_share();
                break;
        }
    }


    //callback from server
     CallbackHomeListener listener = new CallbackHomeListener() {
        @Override
        public void onResponse(List<MapModel_> res) {

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            upDateUI(res);

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Toast.makeText(context, "ResponseBody "+responseBodyError.source(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onBodyErrorIsNull() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(Throwable t) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Toast.makeText(context, "onFailure  "+t.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void showSnack(String message) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


}
