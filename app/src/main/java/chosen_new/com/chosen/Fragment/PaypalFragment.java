package chosen_new.com.chosen.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import chosen_new.com.chosen.Api.CallbackPaymentListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Config.Config;
import chosen_new.com.chosen.Model.PaymentModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

public class PaypalFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = PaypalFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";;


    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)  //use sendbox
            .clientId(Config.PAYPAL_CLIENT_ID);  //client id

    private String amtPay = "";
    private Context context;
    EditText et_amt ;

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
        return inflater.inflate(R.layout.fragment_paypal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInstance(view);
    }

    private void initInstance(View view){
//        new NetworkConnectionManager().callPayment(listener,"1017");

        context = getContext();

        //start Payment service
        Intent intent = new Intent(context,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        getActivity().startService(intent);
        et_amt = view.findViewById(R.id.et_amt);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    private void ProcessPayment()
    {
        amtPay = et_amt.getText().toString();
        PayPalPayment payPalPayment  = new PayPalPayment(new BigDecimal(String.valueOf(amtPay)),
                "THB"," CHOSEN PAY ",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    private void showPayment(String idPayment,String amt ,String state){

        //show detail for reserve
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_state_payment, null);
        TextView tv_idPay = alertLayout.findViewById(R.id.tv_id_payment);
        TextView tv_amp = alertLayout.findViewById(R.id.tv_amt);
        TextView tv_state = alertLayout.findViewById(R.id.tv_state);
        Button btn_OK = alertLayout.findViewById(R.id.btn_OK);

        //set state
        tv_idPay.setText(idPayment);
        tv_amp.setText(amt);
        tv_state.setText(state);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppTheme_NoActionBar);
        builder.setView(alertLayout);

        final AlertDialog dialog  = builder.show();

        //event onclick
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_amt.setText("");
                dialog.dismiss();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK)
            {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirm != null){

                    try {
                        String paymentDetail = confirm.toJSONObject().toString(4);
                        Log.d(TAG,paymentDetail); // result
                        JSONObject jsonObject = new JSONObject(paymentDetail);
                        JSONObject response = new JSONObject(jsonObject.getString("response"));

                        showPayment(response.getString("id"),amtPay,response.getString("state"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        //stop service
        getActivity().stopService(new Intent(context,PayPalService.class));

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                ProcessPayment();
                break;

        }
    }

    //callback payment api
//    CallbackPaymentListener listener = new CallbackPaymentListener() {
//        @Override
//        public void onResponse(List<PaymentModel> res) {
//            String json = new Gson().toJson(res);
//            Log.e(TAG," "+json);
//        }
//
//        @Override
//        public void onBodyError(ResponseBody responseBodyError) {
//
//        }
//
//        @Override
//        public void onBodyErrorIsNull() {
//
//        }
//
//        @Override
//        public void onFailure(Throwable t) {
//
//        }
//    };



}
