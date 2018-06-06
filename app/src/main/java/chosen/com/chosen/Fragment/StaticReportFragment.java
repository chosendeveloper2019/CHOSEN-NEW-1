package chosen.com.chosen.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chosen.com.chosen.Adapter.ReportAdapter;
import chosen.com.chosen.Model.ReportModel;
import chosen.com.chosen.R;

public class StaticReportFragment extends Fragment {
    private static final String TAG = StaticReportFragment.class.getName();
    private static final String KEY_TABLE = "KEY_TABLE";

    private ReportAdapter adapter;
    private RecyclerView recyclerview;
    private static String json_table;

    private ArrayList<ReportModel> list_data_report;

    public static StaticReportFragment newInstance(String table){
        Log.w(TAG, "1 : " + table);
        StaticReportFragment fragment = new StaticReportFragment();
        json_table = table;
        return fragment;
    }

    public StaticReportFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_static, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        CreateTable();
    }

    private void CreateTable(){
        try {
            list_data_report = new ArrayList<>();
            JSONArray data = new JSONArray(json_table);
            if(data.length() > 0){
                for(int i = 0;i < data.length();i++){
                    JSONObject object = data.getJSONObject(i);
                    ReportModel reportModel= new ReportModel();
                    reportModel.setTransaction_id(object.optInt("transaction_id"));
                    reportModel.setContract_id(object.optString("contract_id"));
                    reportModel.setCard_id(object.optString("card_id"));
                    reportModel.setPole_id(object.optString("pole_id"));
                    reportModel.setMeterstartdate(object.optString("meterstartdate"));
                    reportModel.setMetereinddate(object.optString("metereinddate"));
                    reportModel.setTotal(object.optString("Total"));
                    reportModel.setMetereind(object.optString("metereind"));
                    reportModel.setRates(object.optString("rates"));
                    reportModel.setPrice(object.optString("price"));
                    reportModel.setUsages(object.optString("usages"));
                    reportModel.setSumary(object.optString("sumary"));
                    reportModel.setStartdate(object.optString("startdate"));
                    reportModel.setEnddate(object.optString("enddate"));
                    reportModel.setName(object.optString("Name"));
                    list_data_report.add(reportModel);
                }
            }
            adapter = new ReportAdapter(getActivity(), list_data_report);
            recyclerview.setAdapter(adapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
