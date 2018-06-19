package chosen_new.com.chosen.Adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import chosen_new.com.chosen.Model.ReportModel;
import chosen_new.com.chosen.R;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {
    private FragmentActivity context;
    private ArrayList<ReportModel> list_data_report;
    public ReportAdapter(FragmentActivity context, ArrayList<ReportModel> list_data_report){
        this.context = context;
        this.list_data_report = list_data_report;
    }

    @Override
    public ReportAdapter.ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.custom_report, parent, false);
        return new ReportAdapter.ReportHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ReportHolder holder, int position) {
        ReportHolder h = (ReportHolder) holder;
        h.setDataBindView(list_data_report.get(position));
    }

    @Override
    public int getItemCount() {
        return list_data_report.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder {
        private TextView text_report_id, text_report_card_id, text_report_station_id, text_report_timestamp,
                text_report_metereinddate, text_report_total, text_report_perhour, text_report_price;
        public ReportHolder(View itemView) {
            super(itemView);
            text_report_id = (TextView) itemView.findViewById(R.id.text_report_id);
            text_report_card_id = (TextView) itemView.findViewById(R.id.text_report_card_id);
            text_report_station_id = (TextView) itemView.findViewById(R.id.text_report_station_id);
            text_report_timestamp = (TextView) itemView.findViewById(R.id.text_report_timestamp);
            text_report_metereinddate = (TextView) itemView.findViewById(R.id.text_report_metereinddate);
            text_report_total = (TextView) itemView.findViewById(R.id.text_report_total);
            text_report_perhour = (TextView) itemView.findViewById(R.id.text_report_perhour);
            text_report_price = (TextView) itemView.findViewById(R.id.text_report_price);
        }

        public void setDataBindView(ReportModel model){
            text_report_id.setText(setTextItemView(String.valueOf(model.getTransaction_id())));
            text_report_card_id.setText(setTextItemView(model.getCard_id()));
            text_report_station_id.setText(setTextItemView(model.getPole_id()));
            text_report_timestamp.setText(setTextItemView(model.getMeterstartdate()));
            text_report_metereinddate.setText(setTextItemView(model.getMetereinddate()));
            text_report_total.setText(setTextItemView(model.getTotal()));
            text_report_perhour.setText(setTextItemView(model.getRates()));
            text_report_price.setText(setTextItemView(model.getPrice()));
            int color = Color.WHITE;
            if(getAdapterPosition() % 2 != 0){
                color = ContextCompat.getColor(context, R.color.table);
            }
            text_report_id.setBackgroundColor(color);
            text_report_card_id.setBackgroundColor(color);
            text_report_station_id.setBackgroundColor(color);
            text_report_timestamp.setBackgroundColor(color);
            text_report_metereinddate.setBackgroundColor(color);
            text_report_total.setBackgroundColor(color);
            text_report_perhour.setBackgroundColor(color);
            text_report_price.setBackgroundColor(color);
        }

        private String setTextItemView(String data){
            if(!data.equals("")){
                return data;
            } else {
                return "-";
            }
        }
    }
}
