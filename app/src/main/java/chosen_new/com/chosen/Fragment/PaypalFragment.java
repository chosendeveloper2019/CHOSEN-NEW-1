package chosen_new.com.chosen.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Adapter.InvoiceUserRecycleAdapter;
import chosen_new.com.chosen.Api.CallbackInvoicuserListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Config.Config;
import chosen_new.com.chosen.Model.UserInvoiceModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class PaypalFragment extends Fragment {

    private static final String TAG = PaypalFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";;


    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)  //use sendbox
            .clientId(Config.PAYPAL_CLIENT_ID);  //client id

    private String amtPay = "";
    private Context context;

    private String usr_id = "";

//    private EditText et_amt ;
    private TextView tv_noData;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    private List<Map<String,Object>> lisValue;
    private InvoiceUserRecycleAdapter adp;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    public PaypalFragment newInstance(UserModel userModel){
        PaypalFragment fragment = new PaypalFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PaypalFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        initInstance(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initInstance(View view){

        context = getContext();
        sh =getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_usege));

        usr_id = sh.getString(MyFerUtil.KEY_USER_ID,"");

        tv_noData = view.findViewById(R.id.tv_noData);

        tv_noData.setText("");
        tv_noData.setVisibility(View.GONE);
        //init adapter recycleview
        recyclerView = view.findViewById(R.id.recycleViewInvoice);
        adp = new InvoiceUserRecycleAdapter(context);
        lisValue = new ArrayList<>();

        startLoading();

        //api call invoice by user_id
        new NetworkConnectionManager().callShowInvoiceUser(listener,usr_id);


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

                Map<String,Object> result = new HashMap<>();
                result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
                result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
                result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getCreatedAt());
                result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentsId());
                result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
                result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getPaymentStatus());
                result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
                result.put(MyFerUtil.KEY_KWH,res.get(i).getKwh());
                result.put(MyFerUtil.KEY_PAGE_NOW,"payment");
                lisValue.add(result);

            }


            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adp);

            adp.UpdateData(lisValue);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_usege)+" : "+res.size()+" "+getString(R.string.record));

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
            Log.e(TAG,t.getMessage());
        }

    };

}
