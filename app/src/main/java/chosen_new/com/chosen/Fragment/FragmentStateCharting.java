package chosen_new.com.chosen.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackInvoiceListener;
import chosen_new.com.chosen.Api.CallbackShowInvoiceListener;
import chosen_new.com.chosen.Api.CallbackStatusChageListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.ChargeModel;
import chosen_new.com.chosen.Model.PaymentInvoiceModel;
import chosen_new.com.chosen.Model.ResultPaymentModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

import static chosen_new.com.chosen.MainApplication.KEY_DATA_USER;

public class FragmentStateCharting extends Fragment implements View.OnClickListener {

    public static final String TAG  = "<FragmentStateCharting>";
    private ProgressDialog progressDialog;
    private Context context;
    private AlertDialog alertDialog;



    private TextView tv_pole_id,tv_start_charge,tv_end_charge,tv_state_charge,tv_state,tv_hr,tv_min,tv_kwh,tv_card_id,tv_price;
//    private ImageView img_car;
//    private LinearLayout state;n
    private Button btn_showInvoice;

    private FragmentManager fragmentManager;

    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    private String userId;
    private String cardId;
    private String transId = "";
    private String timeFormat = "dd/MM/yyyy HH:mm:ss";
    private String timeStart = "";
    private String timeEnd = "";
    private SimpleDateFormat format;
    private ImageView imageBatt ;


    public static FragmentStateCharting newInstance(UserModel userModel){

        FragmentStateCharting fragment = new FragmentStateCharting();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_status_charting,container,false);
        initInstance(v);

        return v;
    }

    private void initInstance(View v) {

        //get format for diff time current
         format = new SimpleDateFormat(timeFormat);
         fragmentManager = getActivity().getSupportFragmentManager();

        tv_pole_id = v.findViewById(R.id.tv_poleid);
//        tv_state_charge = v.findViewById(R.id.tv_state_charge);
        tv_start_charge = v.findViewById(R.id.tv_start_charge);  //time start
        tv_end_charge = v.findViewById(R.id.tv_end_charge); // time end
        tv_state  = v.findViewById(R.id.tv_state);  //state charge
//        state = v.findViewById(R.id.li_state_charge); //
//        tv_kwh = v.findViewById(R.id.tv_kwh);

        tv_card_id = v.findViewById(R.id.tv_card_id);
        tv_hr = v.findViewById(R.id.tv_hr);
        tv_min = v.findViewById(R.id.tv_min);
        tv_price = v.findViewById(R.id.tv_price);
        tv_price.setText("");

//        v.findViewById(R.id.btn_payment).setOnClickListener(this);
        v.findViewById(R.id.tv_refresh).setOnClickListener(this);

        context = getContext();
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        editor.putString(MyFerUtil.KEY_PAGE_NOW,TAG);
        editor.commit();
        //getCard id
        cardId = sh.getString(MyFerUtil.KEY_SELECT_CARD,"");
        //get User ID
        userId = sh.getString(MyFerUtil.KEY_USER_ID,"");
        timeEnd = "";
        imageBatt = v.findViewById(R.id.img_batt);
        btn_showInvoice = v.findViewById(R.id.btn_invoice);
        btn_showInvoice.setVisibility(View.GONE);
        v.findViewById(R.id.btn_invoice).setOnClickListener(this);
        Log.e(TAG,sh.getString(MyFerUtil.KEY_TRUN,""));

        startRepeatingTask();  //start call server every 1 minute



    }

    private void do_callStatus(String uid){

        try {


            startProgress();

            String transIds = sh.getString(MyFerUtil.KEY_TRUN,"");
            Log.e("call status","uid :"+uid+" trans : "+transIds+"  card id : "+cardId);
            new NetworkConnectionManager().callGetStateCharge(listenerState,cardId,uid,transIds);

        }catch (Exception e){

            Log.e(TAG,e.getMessage());
        }

    }
    private void do_payment(String invoice_id) {

        startProgress();
        new NetworkConnectionManager().callShowInvoice(listener,userId,invoice_id);
        editor.putString(MyFerUtil.KEY_PAGE_NOW,MyFerUtil.STATE_FRG_VIEW);
//
//        editor.putString(MyFerUtil.KEY_PAY_CODE,invoice_code);
//        editor.commit();
//
//        FragmentPaypalWebview  webviewFrg = new FragmentPaypalWebview();
//        fragmentTran(webviewFrg,null);

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

    private void startProgress(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }

    private void showDialog(

            final String payment_id,
            final String payment_code,
            String pole_id,
            final String price,
            final String total_price,
            String vat,
            String start,
            String end,
            String dates,
            String status,
            String total_charge

    ){

        //show detail for reserve
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_show_invoice, null);
        Button btn_pay_now = alertLayout.findViewById(R.id.btn_paynow);
        Button btn_not_now = alertLayout.findViewById(R.id.btn_notnow);

        TextView tv_id_invoice,tv_dates,tv_status,tv_station_id,tv_start_charge,tv_end_charge,
                tv_charge_time,tv_price,tv_vat,tv_total;

        tv_id_invoice = alertLayout.findViewById(R.id.tv_id_invoice);
        tv_dates = alertLayout.findViewById(R.id.tv_dates);
        tv_status = alertLayout.findViewById(R.id.tv_status);
        tv_station_id = alertLayout.findViewById(R.id.tv_station_id);
        tv_start_charge = alertLayout.findViewById(R.id.tv_start_charge);
        tv_end_charge = alertLayout.findViewById(R.id.tv_end_charge);
        tv_charge_time = alertLayout.findViewById(R.id.tv_charge_time);
        tv_price = alertLayout.findViewById(R.id.tv_price);
        tv_vat = alertLayout.findViewById(R.id.tv_vat);
        tv_total = alertLayout.findViewById(R.id.tv_total);

        tv_id_invoice.setText("ID : "+payment_code);
        tv_dates.setText("Date :"+dates);
        tv_status.setText("Payment Status : "+status);
        tv_station_id.setText("Charge Station ID : "+pole_id);
        tv_start_charge.setText("Start Charge :"+start);
        tv_end_charge.setText("End Charge : "+end);
        tv_charge_time.setText("Charge Time : "+total_charge);
        tv_price.setText("Net Price : "+price);
        tv_vat.setText("Vat : "+vat);
        tv_total.setText("Total Price : "+total_price);


        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setView(alertLayout);

        alertDialog = builder.show();

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                alertDialog.dismiss();
                startProgress();
                new NetworkConnectionManager().callSendInvoice(listenerInvoiceSend,
                        "Chosen Energy Invoice ID "+payment_code,
                        "Chosen Energy Invoice ID "+payment_code,
                        "THB",
                        "1",
                        "0.90",
                        total_price,
                        payment_id);
//                Toast.makeText(context, ""+payment_id, Toast.LENGTH_SHORT).show();

            }
        });

        btn_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                fragmentManager.popBackStack(); //back to list card
                Toast.makeText(context, "Thanks you.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getCurrentTime(){

        SimpleDateFormat sdfDate = new SimpleDateFormat(timeFormat);//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private void getTime(){


        try {
            Date d1 = format.parse(timeStart);
            Date d2;

            if(timeEnd.isEmpty()) {

                d2 = format.parse(getCurrentTime());

            }else {
                 d2 = format.parse(timeEnd);

            }


            long elapsed = d2.getTime() - d1.getTime();

            int hours = (int) Math.floor(elapsed / 3600000);

            int minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
//            Log.e("DATE DIFF","Diff = hr: "+hours+" , min :"+minutes);
            tv_hr.setText(""+hours);
            tv_min.setText(""+minutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    // do every 1 minute
    private final static int INTERVAL = 1000 * 30 * 1; //1 minutes
    Handler mHandler = new Handler();

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {

            do_callStatus(userId);

            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_invoice:
//                do_payment();

                String invoice_id = sh.getString(MyFerUtil.KEY_INVOICE_ID,"");
                do_payment(invoice_id);

                break;
            case R.id.tv_refresh:
                do_callStatus(userId);
//                startRepeatingTask();
                break;
        }
    }

    @Override
    public void onStop() {
        stopRepeatingTask();
        super.onStop();
    }

    CallbackShowInvoiceListener  listener = new CallbackShowInvoiceListener() {
        @Override
        public void onResponse(List<PaymentInvoiceModel> res) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            String result = new Gson().toJson(res);
            Log.e(TAG,""+result);
            int indexData = 0;

            showDialog(
                        res.get(indexData).getPaymentId(),
                        res.get(indexData).getPaymentCode(),
                        res.get(indexData).getPoleId(),
                        res.get(indexData).getPrice(),
                        res.get(indexData).getTotalPrice(),
                        res.get(indexData).getVat(),
                        res.get(indexData).getStartCharge(),
                        res.get(indexData).getEndCharge(),
                        res.get(indexData).getDates(),
                        res.get(indexData).getStatus(),
                        res.get(indexData).getTotalCharge()
                      );
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"responseBodyError"+responseBodyError.source());

       }

        @Override
        public void onBodyErrorIsNull() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"  "+t.getMessage());
        }
    };


    CallbackInvoiceListener listenerInvoiceSend = new CallbackInvoiceListener() {
        @Override
        public void onResponse(ResultPaymentModel res) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            String result = new Gson().toJson(res);
            Log.d(TAG,result);
            Toast.makeText(context, "Payment Success.", Toast.LENGTH_SHORT).show();

            //close dialog
            alertDialog.dismiss();

            fragmentManager.popBackStack(); //back to list card
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"responseBodyError "+responseBodyError.source());

        }

        @Override
        public void onBodyErrorIsNull() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"  "+t.getMessage());
        }
    };


    CallbackStatusChageListener listenerState =  new CallbackStatusChageListener() {
        @Override
        public void onResponse(List<ChargeModel> res) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            String result = new Gson().toJson(res);
            Log.e(TAG,result);
                try{
                    if(res.get(0).getCharging().equals("DISCHARGING")){
                        imageBatt.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
                        tv_state.setTextColor(getResources().getColor(R.color.bootstrap_brand_danger));
//                        tv_price.setText("");
                    }else {
                        imageBatt.setImageResource(R.drawable.ic_battery_charging_);
                        tv_state.setTextColor(getResources().getColor(R.color.bootstrap_brand_success));
//                        tv_price.setText(res.get(0).getPrice());

                    }
                }catch (Exception e){

                }

                timeStart = res.get(0).getStartcharge();  // get time start
                timeEnd = res.get(0).getEndcharge();  //get time end
                tv_start_charge.setText(res.get(0).getStartcharge());
                tv_end_charge.setText(getCurrentTime());
                tv_state.setText(res.get(0).getCharging());
                tv_pole_id.setText(res.get(0).getPoleId());
//                tv_kwh.setText(res.get(0).getKhw());
                tv_card_id.setText(res.get(0).getCardId());
                tv_price.setText(res.get(0).getPrice()+" THB");

                getTime(); // get Data

                // put data to tmp
                editor.putString(MyFerUtil.KEY_TRUN,res.get(0).getTransactionId());
                editor.putString(MyFerUtil.KEY_CARD_ID,res.get(0).getCardId());
                editor.putString(MyFerUtil.KEY_INVOICE_ID,res.get(0).getInvoiceId());
                editor.commit();

            if(res.get(0).getCharging().equals("ENDCHARGE")){

                btn_showInvoice.setVisibility(View.VISIBLE);
                btn_showInvoice.setBackgroundColor(getActivity().getResources().getColor(R.color.bootstrap_brand_success));
                do_payment(res.get(0).getInvoiceId());

//                String invoiceid = sh.getString(MyFerUtil.KEY_INVOICE_ID,"");
//                do_payment(userId,invoiceid);


            }

        }


        @Override
        public void onBodyError(ResponseBody responseBodyError) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"responseBodyError "+responseBodyError.source());

       }

        @Override
        public void onBodyErrorIsNull() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };



}
