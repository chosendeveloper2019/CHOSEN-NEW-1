package chosen_new.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Adapter.InvoiceUserRecycleAdapter;
import chosen_new.com.chosen.Api.CallbackInvoicuserListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.UserInvoiceModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class FragmentInvoice extends Fragment implements View.OnClickListener{

    private String TAG = "<FragmentInvoice>";
    private Spinner spinner;
    private Context context;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView tv_noData;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private String usr_id = "";
    private String stateInvoice = "";
    private List<Map<String,Object>> lisValue;
    private InvoiceUserRecycleAdapter adp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invoice_new,container,false);
        initInstance(v);
        return v;
    }

    private void initInstance(View v) {

        context = getContext();
        spinner = v.findViewById(R.id.spn_paid);
        tv_noData = v.findViewById(R.id.tv_noData);

        sh =getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PAYMENT");

        usr_id = sh.getString(MyFerUtil.KEY_USER_ID,"");


        list = new ArrayList<>();
        lisValue = new ArrayList<>();

        list.add(getString(R.string.pleaseChoose));
        list.add("Paid");
        list.add("UnPaid");

        recyclerView = v.findViewById(R.id.recycleViewInvoice);
        adp = new InvoiceUserRecycleAdapter(context);

        startLoading();

        // show invoice user
        new NetworkConnectionManager().callShowInvoiceUser(listener,usr_id);


        adapter = new ArrayAdapter<>(context, R.layout.spinner_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //whatever your logic is put it here
                if(!spinner.getSelectedItem().equals(getString(R.string.pleaseChoose))){

                    try {
                        adp.clear();
                        stateInvoice = spinner.getSelectedItem().toString();

                        startLoading();
                        new NetworkConnectionManager().callShowInvoiceUser(listener,usr_id);

                    }catch (Exception e){

                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void startLoading(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }

    //callback payment api
    CallbackInvoicuserListener listener = new CallbackInvoicuserListener() {
        @Override
        public void onResponse(List<UserInvoiceModel> res) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            tv_noData.setText("");
            tv_noData.setVisibility(View.GONE);

            if(res.size()<=0){

                tv_noData.setText("No Data");
                tv_noData.setVisibility(View.VISIBLE);

            }

            for(int i=res.size()-1;i>0;i-- ){
                Log.d(TAG,res.get(i).getPaymentsId());
                if(stateInvoice.equals("Paid")
                        && res.get(i).getPaymentStatus().equals("True")){

                    Map<String,Object> result = new HashMap<>();
                    result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
                    result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
                    result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getCreatedAt());
                    result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentsId());
                    result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
                    result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getPaymentStatus());
                    result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
                    result.put(MyFerUtil.KEY_KWH,res.get(i).getKwh());
                    result.put(MyFerUtil.KEY_PAGE_NOW,"invoice");
                    lisValue.add(result);

                }else if(stateInvoice.equals("UnPaid")
                        && res.get(i).getPaymentStatus().equals("False")){

                        Map<String,Object> result = new HashMap<>();
                        result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
                        result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
                        result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getCreatedAt());
                        result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentsId());
                        result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
                        result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getPaymentStatus());
                        result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
                        result.put(MyFerUtil.KEY_KWH,res.get(i).getKwh());
                        result.put(MyFerUtil.KEY_PAGE_NOW,"invoice");
                        lisValue.add(result);

                }


            }


            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_invoice)+" : "+lisValue.size()+getString(R.string.record));

            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adp);

            adp.UpdateData(lisValue);


        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            tv_noData.setText("No Data");
            tv_noData.setVisibility(View.VISIBLE);
            Log.e(TAG,responseBodyError.source().toString());

        }

        @Override
        public void onBodyErrorIsNull() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            tv_noData.setText("No Data is Null");
            tv_noData.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(Throwable t) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            tv_noData.setText("Error : "+t.getMessage());
            tv_noData.setVisibility(View.VISIBLE);
            t.printStackTrace();
            Log.e(TAG+" onFailure ",t.getMessage());
        }
//    };


    };


    @Override
    public void onClick(View view) {

        switch (view.getId()){

        }

    }
}
