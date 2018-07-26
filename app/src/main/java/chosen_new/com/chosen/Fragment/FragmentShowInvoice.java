package chosen_new.com.chosen.Fragment;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Adapter.InvoiceRecycleAdapter;
import chosen_new.com.chosen.Adapter.InvoiceUserRecycleAdapter;
import chosen_new.com.chosen.Api.CallbackGetInvoiceListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.InvoiceCardModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class FragmentShowInvoice extends Fragment{

    private String TAG = "<FragmentShowInvoice>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private String userId = "";
    private String cardId = "";
    private RecyclerView recyclerView;
    private List<Map<String,Object>> lisValue;
    private Context context;
    private InvoiceUserRecycleAdapter adp;
    private TextView tv_noData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment,container,false);
        initInStance(v);
        return v;
    }

    private void initInStance(View v) {

        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER, Context.MODE_PRIVATE);
        editor = sh.edit();
        context = getContext();
        tv_noData = v.findViewById(R.id.tv_noData);

        userId = sh.getString(MyFerUtil.KEY_USER_ID,"");
        cardId = sh.getString(MyFerUtil.KEY_SELECT_CARD,"");


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("CARD ID : "+cardId);

        recyclerView = v.findViewById(R.id.recycleViewInvoice);

        tv_noData = v.findViewById(R.id.tv_noData);
        tv_noData.setText("");
        tv_noData.setVisibility(View.GONE);

        //init adapter recycleview
        adp = new InvoiceUserRecycleAdapter(context);
        lisValue = new ArrayList<>();

        // get data invoice from server
        new NetworkConnectionManager().callGetInvoice(listener,userId,cardId);

    }

    CallbackGetInvoiceListener listener = new CallbackGetInvoiceListener() {
        @Override
        public void onResponse(List<InvoiceCardModel> res) {
            tv_noData.setText("");
            tv_noData.setVisibility(View.GONE);

            if(res.size()<=0){

                tv_noData.setText("No Data");
                tv_noData.setVisibility(View.VISIBLE);

            }

            for(int i=0;i<res.size();i++ ){
//                Map<String,Object> result = new HashMap<>();
//                result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
//                result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
//                result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getStartCharge());
//                result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentCode());
//                result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
//                result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getStatus());
//                result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
//                lisValue.add(result);

                Map<String,Object> result = new HashMap<>();


                result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
                result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
                result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getStartCharge());
                result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentId());
                result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
                result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getStatus());
                result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
                result.put(MyFerUtil.KEY_PAGE_NOW,"showInvoice");

//
//                result.put(MyFerUtil.KEY_PAY_CODE,res.get(i).getPaymentCode());
//                result.put(MyFerUtil.KEY_TOTALPRICE,res.get(i).getTotalPrice());
//                result.put(MyFerUtil.KEY_CARD_ID,res.get(i).getCardId());
//                result.put(MyFerUtil.KEY_POLE_ID,res.get(i).getPoleId());
//                result.put(MyFerUtil.KEY_CREATEDATE_AT,res.get(i).getStartCharge());
//                result.put(MyFerUtil.KEY_INVOICE_PAY,res.get(i).getPaymentCode());
//                result.put(MyFerUtil.KEY_STATE_PAY,res.get(i).getStatus());
//                result.put(MyFerUtil.KEY_SELECT_CARD,res.get(i).getCardId());
                lisValue.add(result);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adp);

            adp.UpdateData(lisValue);

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            tv_noData.setText("No Data");
            tv_noData.setVisibility(View.VISIBLE);
            Log.e(TAG,"body err "+responseBodyError.source());

        }

        @Override
        public void onBodyErrorIsNull() {
            tv_noData.setText("No Data");
            tv_noData.setVisibility(View.VISIBLE);
            Log.e(TAG,"response is null");
        }

        @Override
        public void onFailure(Throwable t) {
            tv_noData.setText("No Data");
            tv_noData.setVisibility(View.VISIBLE);
            Log.e(TAG,"Error "+t.getMessage());
        }
    };
}
