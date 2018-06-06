package chosen.com.chosen.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import chosen.com.chosen.R;

public class PieChartFragment extends Fragment{
    private static final String KEY_PIE_CHART = "KEY_PIE_CHART";

    private PieChart piechart;

    private ArrayList<PieEntry> yvalues;
    private ArrayList<String> xVals;

    private static String json_piechart;

    public static PieChartFragment newInstance(String PieChart){
        PieChartFragment fragment = new PieChartFragment();
        json_piechart = PieChart;
        return fragment;
    }

    public PieChartFragment(){ }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        piechart = (PieChart) view.findViewById(R.id.piechart);

        CreatePieChart();
    }

    private void CreatePieChart(){
        try {
            yvalues = new ArrayList<>();
            xVals = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<Integer>();
            JSONArray data = new JSONArray(json_piechart);
            if(data.length() > 0){
                for(int i = 0;i < data.length();i++){
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    colors.add(color);
                    JSONObject object = data.getJSONObject(i);
                    yvalues.add(new PieEntry(object.optInt("Total"), i));
                    xVals.add(object.optString("card_id"));
                }
                PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
                PieData Piedata = new PieData(dataSet);
                Piedata.setValueTextSize(15f);
                dataSet.setColors(colors);
                dataSet.setValueTextSize(15f);
                piechart.setCenterTextSize(15f);
                piechart.setData(Piedata);
                piechart.setDrawHoleEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
