package chosen.com.chosen.Model;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class CombineModel {
    private String Month;
    private BarDataSet barDataSet;
    private ArrayList<DataModel> listData;

    public CombineModel(){ }

    public void setMonth(String month) {
        Month = month;
    }

    public void setBarDataSet(BarDataSet barDataSet) {
        this.barDataSet = barDataSet;
    }

    public void setListData(ArrayList<DataModel> listData) {
        this.listData = listData;
    }

    public String getMonth() {
        return Month;
    }

    public BarDataSet getBarDataSet() {
        return barDataSet;
    }

    public ArrayList<DataModel> getListData() {
        return listData;
    }

    public static class DataModel {
        private String name;
        private int value;

        public DataModel(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}
