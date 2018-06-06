package chosen.com.chosen.Adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import chosen.com.chosen.Model.InvoiceModel;
import chosen.com.chosen.R;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.CardHolder> {
    private FragmentActivity context;
    private ArrayList<InvoiceModel> list_data_invoice;

    public InvoiceAdapter(FragmentActivity context, ArrayList<InvoiceModel> list_data_invoice){
        this.context = context;
        this.list_data_invoice = list_data_invoice;
    }

    @Override
    public InvoiceAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.custom_invoice, parent, false);
        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(InvoiceAdapter.CardHolder holder, int position) {
        CardHolder h = (CardHolder) holder;
        h.setViewItem(list_data_invoice.get(position));
    }

    @Override
    public int getItemCount() {
        return list_data_invoice.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        private TextView text_no, text_card_id,text_total,text_invoice;
        public CardHolder(View itemView) {
            super(itemView);
            text_no = (TextView) itemView.findViewById(R.id.text_no);
            text_invoice = (TextView) itemView.findViewById(R.id.text_invoice);
            text_total = (TextView) itemView.findViewById(R.id.text_total);
        }

        public void setViewItem(InvoiceModel invoiceModel) {
            text_no.setText(invoiceModel.getNo());
            text_invoice.setText(invoiceModel.getInvoice());
            text_total.setText(invoiceModel.getTotal());
            int color = Color.WHITE;
            if(getAdapterPosition() % 2 != 0){
                color = ContextCompat.getColor(context, R.color.table);
            }
            text_no.setBackgroundColor(color);
            text_invoice.setBackgroundColor(color);
            text_total.setBackgroundColor(color);
        }
    }
}
