package chosen_new.com.chosen.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import chosen_new.com.chosen.Adapter.InvoiceAdapter;
import chosen_new.com.chosen.Model.InvoiceModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.ConnectivityReceiverUtil;
import chosen_new.com.chosen.Util.MyFerUtil;
import chosen_new.com.chosen.Util.UrlUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InvoiceFragment extends Fragment implements ConnectivityReceiverUtil.ConnectivityReceiverListener {
    private static final String TAG = InvoiceFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";

    private RecyclerView recyclerview;
    private ArrayList<InvoiceModel> list_data_invoice;
    private View User_id;
    private String uid;

    private SharedPreferences sh;


    public static InvoiceFragment newInstance(UserModel userModel){
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public InvoiceFragment(){


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invoice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerview = view.findViewById(R.id.recyclerview);
//        UserModel userModel = (UserModel) getArguments().getSerializable(KEY_DATA_USER);
//
        if(ConnectivityReceiverUtil.isConnected()){
//            new LoadDataInvoice(String.valueOf(userModel.getUser_id())).execute();

            sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER, Context.MODE_PRIVATE);


            uid = sh.getString(MyFerUtil.KEY_USER_ID,"");
            Button twitterButton = view.findViewById(R.id.ClickInvoice);
            twitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToInvoice(Integer.parseInt(uid));
                }
            });

        } else {
            showSnack("Sorry! Not connected to internet");
        }

    }

    protected void sendToInvoice(int user_id) {
        String url = "http://app.chosenenergy.co.th/payment/allmyinvoice?user_id="+user_id;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }

    private class LoadDataInvoice extends AsyncTask<Void, Void, String>{
        private String user_id;
        public LoadDataInvoice(String user_id){
            this.user_id = user_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", user_id)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlUtil.URL_INVOICE)
                    .post(formBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Exception";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String response) {
//            Log.w(TAG, response);
            try {
                JSONArray data = new JSONArray(response);
                list_data_invoice = new ArrayList<>();
                if(data.length() > 0){
                    for(int i = 0;i < data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        InvoiceModel invoiceModel = new InvoiceModel();
                        invoiceModel.setNo(String.valueOf(i + 1));
                        invoiceModel.setInvoice(object.optString("payment_code"));
                        invoiceModel.setTotal(object.optString("total_price"));

                        list_data_invoice.add(invoiceModel);
                    }
                    InvoiceAdapter adapter = new InvoiceAdapter(getActivity(), list_data_invoice);
                    recyclerview.setAdapter(adapter);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSnack(String message) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}
