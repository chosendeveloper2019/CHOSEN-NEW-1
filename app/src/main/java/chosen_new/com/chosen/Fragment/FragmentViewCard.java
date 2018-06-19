package chosen_new.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Adapter.CardRecycleVIewAdapter;
import chosen_new.com.chosen.Api.CallbackAddcardListener;
import chosen_new.com.chosen.Api.CallbackCardListener;
import chosen_new.com.chosen.Api.CallbackStatusChageListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.AddCardModel;
import chosen_new.com.chosen.Model.ChargeModel;
import chosen_new.com.chosen.Model.GetCardModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static chosen_new.com.chosen.MainApplication.KEY_DATA_USER;

public class FragmentViewCard extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private CardRecycleVIewAdapter adp;
    private ProgressDialog progressDialog;

    private String TAG = "<FragmentViewCard>";
    private List<Map<String,Object>> val;
    private SharedPreferences sh;
    private String idUser;
    private TextView tv_nodata;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static FragmentViewCard newInstance(UserModel userModel) {
        FragmentViewCard fragment = new FragmentViewCard();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentViewCard() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_card,container,false);
        initInstance(v);
        return v;
    }

    private void initInstance(View v) {

        val = new ArrayList<>();

        context = getContext();
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        tv_nodata = v.findViewById(R.id.tv_noData);
        idUser = sh.getString(MyFerUtil.KEY_USER_ID,"");
        adp = new CardRecycleVIewAdapter(context);
        recyclerView  = v.findViewById(R.id.recyclerCard);
        showProgressDialog();

        FloatingActionButton fab = v.findViewById(R.id.fab_addCard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //click fab
                InsertCardDialog();

            }
        });

        mSwipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adp.clear();  // clear
                new NetworkConnectionManager().callGetCard(listenerState,idUser);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });


        new NetworkConnectionManager().callGetCard(listenerState,idUser);



    }

    private void showProgressDialog(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }

    public void InsertCardDialog(){


        //show detail for reserve
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_insert_card, null);
        final EditText et_cardId = alertLayout.findViewById(R.id.et_cardid);
        Button btn_scan = alertLayout.findViewById(R.id.btn_scan);
        Button btn_confirm = alertLayout.findViewById(R.id.btn_confirm);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
//        TextView tv_close = alertLayout.findViewById(R.id.tv_close);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(alertLayout);

        alertDialog = builder.show();

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do scan
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                getActivity().startActivityForResult(intent, 0);
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan A barcode or QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                alertDialog.dismiss();


            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do confirm
                String input = et_cardId.getText().toString().trim();
                input = input.toUpperCase();

                if(!input.isEmpty() && input.length() > 3){

                    new NetworkConnectionManager().callAddcard(listenerAddcard,idUser,input);


                }else {

                    Toast.makeText(context, "Please input data card id.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


    }

    CallbackCardListener listenerState =  new CallbackCardListener() {
        @Override
        public void onResponse(List<GetCardModel> res) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            try{

                String result = new Gson().toJson(res);
                Log.e(TAG,result);
                if(!result.equals("[]"))
                    tv_nodata.setVisibility(View.GONE);

                for (int i = 0;i<res.size();i++){
                    Map<String,Object> getRes = new HashMap<>();
                    getRes.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
                    getRes.put(MyFerUtil.KEY_STATE,res.get(i).getState());
                    val.add(getRes);
                }


                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adp);

                adp.UpdateData(val);

            }catch (Exception e){
                Toast.makeText(context, "Null ", Toast.LENGTH_SHORT).show();
            }


        }



        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            tv_nodata.setVisibility(View.VISIBLE);
            Log.e(TAG,"responseBodyError "+responseBodyError.source());

        }

        @Override
        public void onBodyErrorIsNull() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            tv_nodata.setVisibility(View.VISIBLE);
            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            tv_nodata.setVisibility(View.VISIBLE);
            Log.e(TAG,"  "+t.getMessage());
        }
    };

    CallbackAddcardListener listenerAddcard = new CallbackAddcardListener() {
        @Override
        public void onResponse(AddCardModel res) {
            Toast.makeText(context, ""+res.getState(), Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
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


}
