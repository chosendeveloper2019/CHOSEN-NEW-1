package chosen_new.com.chosen.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Adapter.CarModelAdapter;
import chosen_new.com.chosen.Adapter.CardRecycleVIewAdapter;
import chosen_new.com.chosen.Api.CallbackCarModelListener;
import chosen_new.com.chosen.Api.CallbackCardListener;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Model.CarModel;
import chosen_new.com.chosen.Model.CardModel;
import chosen_new.com.chosen.Model.GetCardModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class FragmentCarModel  extends Fragment{

    private RecyclerView recyclerView;
    private Context context;
    private CarModelAdapter adp;
    private ProgressDialog progressDialog;

    private String TAG = "<FragmentCarModel>";
    private List<Map<String,Object>> val;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private String idUser;
    private TextView tv_nodata;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String cardId = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_carmodel,container,false);
            initInstance(v);
        return v;

    }

    private void initInstance(View v) {

        val = new ArrayList<>();

        context = getContext();
        sh = getActivity().getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

        cardId = sh.getString(MyFerUtil.KEY_SELECT_CARD,"");
        //set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_carmodel)+" "+getString(R.string.card)+" : "
                        +cardId);

        tv_nodata = v.findViewById(R.id.tv_noData);



        editor.putString(MyFerUtil.STATE_FRG,MyFerUtil.VIEW_CARD);
        editor.commit();
        try{

            adp = new CarModelAdapter(context);
            recyclerView  = v.findViewById(R.id.recyclerviewCarmodel);
            showProgressDialog();
            new NetworkConnectionManager().callGetCarModel(listener);

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }


    }

    private void showProgressDialog(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }

    CallbackCarModelListener listener = new CallbackCarModelListener() {
        @Override
        public void onResponse(List<CarModel> res) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }


            String resStr = new Gson().toJson(res);
            Log.d(TAG,resStr);

            try{

                String result = new Gson().toJson(res);
//                Log.e(TAG,result);

                if(!result.equals("[]")){
                    tv_nodata.setVisibility(View.GONE);
                }


                for (int i = 0;i<res.size();i++){
                    Map<String,Object> getRes = new HashMap<>();
                    getRes.put(MyFerUtil.CAR_ID,res.get(i).getId());
                    getRes.put(MyFerUtil.CAR_BRAND,res.get(i).getBrands());
                    getRes.put(MyFerUtil.CAR_MODEL,res.get(i).getModelName());
                    getRes.put(MyFerUtil.CAR_URL,res.get(i).getImage());
                    getRes.put(MyFerUtil.KEY_SELECT_CARD,cardId);
                    val.add(getRes);
                }

                editor.putString(MyFerUtil.KEY_PAGE_NOW,MyFerUtil.VIEW_CARD);
                editor.commit();

                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adp);

                adp.UpdateData(val);

            }catch (Exception e){
                e.printStackTrace();
//                Toast.makeText(context, "Null ", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    };

}
