package chosen.com.chosen.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import chosen.com.chosen.Api.CallbackPaymentListener;
import chosen.com.chosen.Api.NetworkConnectionManager;
import chosen.com.chosen.Model.PaymentModel;
import chosen.com.chosen.Model.UserModel;
import chosen.com.chosen.R;
import okhttp3.ResponseBody;

public class PaypalFragment extends Fragment{
    private static final String TAG = PaypalFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";;

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
        new NetworkConnectionManager().callPayment(listener,"1017");
    }

    //callback payment api
    CallbackPaymentListener listener = new CallbackPaymentListener() {
        @Override
        public void onResponse(List<PaymentModel> res) {
            String json = new Gson().toJson(res);
            Log.e(TAG," "+json);
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
