package chosen_new.com.chosen.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import chosen_new.com.chosen.Adapter.CardAdapter;
import chosen_new.com.chosen.Model.CardModel;
import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.ConnectivityReceiverUtil;
import chosen_new.com.chosen.Util.UrlUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PoleFragment extends Fragment  implements ConnectivityReceiverUtil.ConnectivityReceiverListener {
    private static final String TAG = PoleFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";

    private RecyclerView recyclerview;
    private ArrayList<CardModel> list_data_pole;

    public static PoleFragment newInstance(UserModel userModel){
        PoleFragment fragment = new PoleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PoleFragment(){ }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pole, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        UserModel userModel = (UserModel) getArguments().getSerializable(KEY_DATA_USER);

        if(ConnectivityReceiverUtil.isConnected()){
            new LoadDataPole(String.valueOf(userModel.getUser_id())).execute();
        } else {
            showSnack("Sorry! Not connected to internet");
        }
    }

    private class LoadDataPole extends AsyncTask<Void, Void, String> {
        private String user_id;
        public LoadDataPole(String user_id){
            this.user_id = user_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", user_id)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlUtil.URL_POLE)
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
            Log.w(TAG, response);

            try {
                JSONArray data = new JSONArray(response);
                list_data_pole = new ArrayList<>();
                if(data.length() > 0){
                    for(int i = 0;i < data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        CardModel cardModel = new CardModel();
                        cardModel.setNo(String.valueOf(i + 1));
                        cardModel.setCard_id(object.optString("pole_id"));
                        list_data_pole.add(cardModel);
                    }
                    CardAdapter adapter = new CardAdapter(getActivity(), list_data_pole);
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
