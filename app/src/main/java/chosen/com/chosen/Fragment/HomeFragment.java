package chosen.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen.com.chosen.Api.CallbackHomeListener;
import chosen.com.chosen.Api.NetworkConnectionManager;
import chosen.com.chosen.Model.InfoWindowDataModel;
import chosen.com.chosen.Model.MapModel;
import chosen.com.chosen.Model.MapModel_;
import chosen.com.chosen.Model.UserModel;
import chosen.com.chosen.R;
import chosen.com.chosen.Util.ConnectivityReceiverUtil;
import chosen.com.chosen.Util.MyFerUtil;
import chosen.com.chosen.Util.UrlUtil;
import chosen.com.chosen.View.CustomMapDetail;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements
        ConnectivityReceiverUtil.ConnectivityReceiverListener
        ,View.OnClickListener

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
    private EditText et_search;
    private UserModel userModel;

    public static HomeFragment newInstance(UserModel userModel){
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeFragment(){ }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initInstance(view);

    }

    private void initInstance(View v){
        try {
        //bing  context
        context = getContext();
        //bind widget
        linearLayout = v.findViewById(R.id.detail);
        linearLayout.setVisibility(View.GONE);

        et_search = v.findViewById(R.id.et_search_map);
        et_search.setInputType(0); // hide key board

        v.findViewById(R.id.btn_refresh).setOnClickListener(this);
        v.findViewById(R.id.btn_search).setOnClickListener(this);

        //init for SharedPreferences ( SESSION )
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();
        //get user id from shared perferences
        uid = sh.getString(MyFerUtil.KEY_USER_ID,null);

        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        fragment_view_map = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_view_map);


            MapsInitializer.initialize(getActivity().getApplicationContext());



        userModel = (UserModel) getArguments().getSerializable(KEY_DATA_USER);

        if(ConnectivityReceiverUtil.isConnected()) {
                new NetworkConnectionManager().callHomeFrg(listener,uid);
        } else{
            showSnack("Sorry! Not connected to internet");
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upDateUI(List<MapModel_> res){

        Log.e(TAG,res.get(0).getUserFullname());
        try {

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

                    Log.e(TAG,list_data_map.get(i).getUserFullname().toString()
                            +"lat ="+list_data_map.get(i).getLat()
                            +"long = "+list_data_map.get(i).getLon());
                }

            fragment_view_map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {


                    googleMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(13.151351,101.490104), 6f));


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

                            Map<Object,String> val = new HashMap<>();
                            val.put("lat"," lat :"+marker.getPosition().longitude);
                            val.put("long"," long :"+marker.getPosition().longitude);
//                            Toast.makeText(context, ""+latLng.longitude, Toast.LENGTH_SHORT).show();
                            setShowDetail(val);

                            return false;
                        }
                    });
//                        googleMap.setMinZoomPreference(11);
                    //dum add====================

                    if(list_data_map != null){
                        for(int i = 0;i < list_data_map.size();i++){
                            //Log.w(TAG, "LOOP : " + i);

                            if(list_data_map.get(i).getColor()==101) {

                                InfoWindowDataModel info = new InfoWindowDataModel();
                                info.setHeaderStr("TEST HEADER");
                                info.setStateStr("TEST STATE");
                                info.setDetailStr("DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL DETAIL ");
                                info.setStartStr("start ................................");
                                info.setEndStr("End....................................");

                                CustomMapDetail customInfoWindow = new CustomMapDetail(context,info);
//                                    googleMap.setInfoWindowAdapter(customInfoWindow);

                                googleMap.addMarker(
                                        new MarkerOptions().position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_maker)));
//                                                .title(list_data_map.get(i).getPoleId())
//                                                .snippet("Population: 4,137,400"));
                            }

                            if(list_data_map.get(i).getColor()==102) {

                                googleMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_maker_red)));
//                                        .title(list_data_map.get(i).getPoleId()));

                            }

                            if(list_data_map.get(i).getColor()==103) {
                                googleMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                                        Double.parseDouble(list_data_map.get(i).getLon())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_maker_blue)));
//                                        .title(list_data_map.get(i).getPoleId()));
                            }

                            googleMap.addMarker(new MarkerOptions()
                                    .position( new LatLng(Double.parseDouble(list_data_map.get(i).getLat()),
                                            Double.parseDouble(list_data_map.get(i).getLon())))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_maker)));
//                                    .title(list_data_map.get(i).getPoleId()));

                        }
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    private void setShowDetail(Map<Object,String> data){
//        Toast.makeText(context, ""+, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Response  = test1 ="+data.get("lat").toString() +" test 2 = "+data.get("long") );

        linearLayout.setVisibility(View.VISIBLE);

    }

    private void do_refresh(){
        Toast.makeText(context, "Uid ="+uid, Toast.LENGTH_SHORT).show();
            new NetworkConnectionManager().callHomeFrg(listener,uid);
//        new LoadDataHome(String.valueOf(userModel.getUser_id())).execute();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_refresh:
                do_refresh();
                break;
            case R.id.btn_search:

                break;
        }
    }


     CallbackHomeListener listener = new CallbackHomeListener() {
        @Override
        public void onResponse(List<MapModel_> res) {

            upDateUI(res);
            Toast.makeText(context, "Response ="+res.get(0).getLat(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, ""+res.get(0).getUserFullname(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Toast.makeText(context, "ResponseBody "+responseBodyError.source(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onBodyErrorIsNull() {
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(Throwable t) {
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
