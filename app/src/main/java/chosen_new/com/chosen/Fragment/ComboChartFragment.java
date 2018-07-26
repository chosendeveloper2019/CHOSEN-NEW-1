package chosen_new.com.chosen.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import chosen_new.com.chosen.Model.CombineModel;
import chosen_new.com.chosen.R;

public class ComboChartFragment extends Fragment{

    private static final String TAG = ComboChartFragment.class.getName();
    private static final String KEY_COMBO_CHART = "KEY_COMBO_CHART";

    private BarChart comboChart;

    private static String json_combochart;
    private static String strTpye;
    private ArrayList<String> BarEntryLabels;
    private BarDataSet Bardataset;
    private BarData BARDATA;

    public static ComboChartFragment newInstance(String ComboChart, String type){

        ComboChartFragment fragment = new ComboChartFragment();
        json_combochart = ComboChart;
        strTpye = type;
        return fragment;

    }

    public ComboChartFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_combo_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comboChart = view.findViewById(R.id.comboChart);

        CreateCombochart();

    }

    private void CreateCombochart(){
        try{
            JSONArray data = new JSONArray(json_combochart);
            BarEntryLabels = new ArrayList<>();
            BARDATA = new BarData();
            AddValuesToBarEntryLabels();
            if(data.length() > 0){
                for(int i =0;i < data.length();i++){
                    JSONObject object = data.optJSONObject(i);
                    ArrayList<BarEntry> BARENTRY = new ArrayList<>();
                    JSONArray dataChart = object.optJSONArray("VALUE");

                    for(int j = 0;j < dataChart.length();j++){
                        BARENTRY.add(new BarEntry(j, dataChart.optInt(j)));
                    }

                    Bardataset = new BarDataSet(BARENTRY, object.optString(strTpye));
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                            rnd.nextInt(256));
                    Bardataset.setColors(color);

                    Bardataset.setValueFormatter(new IValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                            if(value > 0){
                                return String.valueOf((int)value);
                            } else {
                                return "";
                            }
                        }
                    });

                    BARDATA.addDataSet(Bardataset);
                }

                comboChart.getAxisRight().setDrawGridLines(false);
                comboChart.getAxisRight().setDrawLabels(false);
                comboChart.getAxisRight().setDrawAxisLine(false);

                comboChart.getXAxis().setLabelCount(12, true);
                comboChart.getXAxis().setTextSize(10f);
                comboChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                comboChart.getXAxis().setDrawGridLines(false);
                comboChart.getXAxis().setCenterAxisLabels(true);

                comboChart.getAxisLeft().setDrawGridLines(true);
                comboChart.getAxisLeft().setAxisMinimum(0);
                comboChart.getAxisLeft().setAxisLineColor(Color.TRANSPARENT);

                comboChart.getDescription().setText("");

                comboChart.animateX(300, Easing.EasingOption.EaseInOutQuart);
                comboChart.setDragEnabled(true);
                comboChart.setData(BARDATA);
                comboChart.invalidate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddValuesToBarEntryLabels(){
        BarEntryLabels.add("M.1");
        BarEntryLabels.add("M.2");
        BarEntryLabels.add("M.3");
        BarEntryLabels.add("M.4");
        BarEntryLabels.add("M.5");
        BarEntryLabels.add("M.6");
        BarEntryLabels.add("M.7");
        BarEntryLabels.add("M.8");
        BarEntryLabels.add("M.9");
        BarEntryLabels.add("M.10");
        BarEntryLabels.add("M.11");
        BarEntryLabels.add("M.12");
    }
}
