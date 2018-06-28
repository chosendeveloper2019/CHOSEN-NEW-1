package chosen_new.com.chosen.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Api.CallbackInvoiceListener;
import chosen_new.com.chosen.Api.CallbackShowInvoiceListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Fragment.FragmentPaypalWebview;
import chosen_new.com.chosen.Model.PaymentInvoiceModel;
import chosen_new.com.chosen.Model.ResultPaymentModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class InvoiceRecycleAdapter  extends RecyclerView.Adapter<InvoiceRecycleAdapter.MyHolder> {

    private Context context;
    private List<Map<String,Object>> val;
    private String TAG = "<CardRecycleVIewAdapter>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private AlertDialog alertDialog;


    public InvoiceRecycleAdapter(Context context){

        this.context = context;

        sh = ((AppCompatActivity)context).getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);

        editor = sh.edit();

    }

    public void UpdateData(List<Map<String,Object>> val){
        this.val = val;

    }

    @Override
    public InvoiceRecycleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_invoice_card,parent,false);
        return new InvoiceRecycleAdapter.MyHolder(v,context);
    }

    @Override
    public void onBindViewHolder(InvoiceRecycleAdapter.MyHolder holder, final int position) {


//        holder.tv_payCode.setText(val.get(position).get(MyFerUtil.KEY_PAY_CODE).toString());
//        holder.tv_create_at.setText(val.get(position).get(MyFerUtil.KEY_CREATEDATE_AT).toString());
//        holder.tv_totalprice.setText(val.get(position).get(MyFerUtil.KEY_TOTALPRICE).toString());
//        holder.tv_card_id.setText(val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
//        holder.tv_poleid.setText(val.get(position).get(MyFerUtil.KEY_POLE_ID).toString());
        holder.tv_create_at.setText(val.get(position).get(MyFerUtil.KEY_CREATEDATE_AT).toString());
        holder.tv_totalprice.setText(val.get(position).get(MyFerUtil.KEY_TOTALPRICE).toString());
        holder.tv_card_id.setText(val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
//
//
//        holder.btn_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                editor.putString(MyFerUtil.KEY_PAY_CODE,val.get(position).get(MyFerUtil.KEY_PAY_CODE).toString());
//                editor.commit();
//
//                FragmentPaypalWebview  webviewFrg = new FragmentPaypalWebview();
//                fragmentTran(webviewFrg,null);
//
//
//            }
//        });

        holder.setOnClickRecycleView(new RecycleViewOnClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

                do_payment(sh.getString(MyFerUtil.KEY_USER_ID,""),
                        val.get(position).get(MyFerUtil.KEY_INVOICE_PAY).toString());
                editor.putString(MyFerUtil.KEY_SELECT_CARD,val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
                editor.putString(MyFerUtil.KEY_PAY_CODE,val.get(position).get(MyFerUtil.KEY_PAY_CODE).toString());
                editor.commit();

                FragmentPaypalWebview  webviewFrg = new FragmentPaypalWebview();
                fragmentTran(webviewFrg,null);


            }

            @Override
            public void onLongClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {

            }
        });
    }

    public void clear() {
        final int size = val.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                val.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return val.size();
        }catch (Exception e){
            return 0;

        }

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }


    public class MyHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener{
        Context context;

        TextView tv_payCode , tv_totalprice , tv_create_at,tv_card_id,tv_poleid;
        Button btn_view;


        RecycleViewOnClickListener listener;

        public MyHolder(View v,Context context) {

            super(v);

            this.context = context;
//            tv_payCode = v.findViewById(R.id.tv_paycode);

            tv_create_at = v.findViewById(R.id.tv_create_at);
            tv_totalprice = v.findViewById(R.id.tv_price);
            tv_card_id = v.findViewById(R.id.tv_card_id);
//            tv_create_at = v.findViewById(R.id.tv_create_at);
//            tv_totalprice = v.findViewById(R.id.tv_price);
//            tv_totalprice = v.findViewById(R.id.tv_price);
//            tv_card_id = v.findViewById(R.id.tv_card_id);
//            tv_poleid = v.findViewById(R.id.tv_poleid);
//
//            btn_view = v.findViewById(R.id.btn_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void setOnClickRecycleView(RecycleViewOnClickListener listener){

            this.listener =  listener;

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), false, null);

        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(v, getAdapterPosition(), false, null);

            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onClick(v, getAdapterPosition(), false, event);

            return false;
        }
    }

    private void do_payment(String userId,String invoice_id) {


        new NetworkConnectionManager().callShowInvoice(listener,userId,invoice_id);

    }

    CallbackShowInvoiceListener listener = new CallbackShowInvoiceListener() {
        @Override
        public void onResponse(List<PaymentInvoiceModel> res) {

//            String result = new Gson().toJson(res);
//            Log.e(TAG,""+result);
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


            Log.e(TAG,"responseBodyError"+responseBodyError.source());

        }

        @Override
        public void onBodyErrorIsNull() {


            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {

            Log.e(TAG,"  "+t.getMessage());
        }
    };

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
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

                new NetworkConnectionManager().callSendInvoice(listenerInvoiceSend,
                        "Chosen Energy Invoice ID "+payment_code,
                        "Chosen Energy Invoice ID "+payment_code,
                        "THB",
                        "1",
                        "0.90",
                        total_price,
                        payment_id);

            }
        });

        btn_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                Toast.makeText(context, "Thanks you.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //payment call back
    CallbackInvoiceListener listenerInvoiceSend = new CallbackInvoiceListener() {
        @Override
        public void onResponse(ResultPaymentModel res) {


            String result = new Gson().toJson(res);
            Log.d(TAG,result);
            Toast.makeText(context, "Payment Success.", Toast.LENGTH_SHORT).show();

            //close dialog
            alertDialog.dismiss();

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {


            Log.e(TAG,"responseBodyError "+responseBodyError.source());

        }

        @Override
        public void onBodyErrorIsNull() {


            Log.e(TAG,"null");

        }

        @Override
        public void onFailure(Throwable t) {


            Log.e(TAG,"  "+t.getMessage());
        }
    };




}
