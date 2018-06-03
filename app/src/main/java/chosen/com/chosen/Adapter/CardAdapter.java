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

import chosen.com.chosen.Model.CardModel;
import chosen.com.chosen.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    private FragmentActivity context;
    private ArrayList<CardModel> list_data_card;

    public CardAdapter(FragmentActivity context, ArrayList<CardModel> list_data_card){
        this.context = context;
        this.list_data_card = list_data_card;
    }

    @Override
    public CardAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.custom_card, parent, false);
        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(CardAdapter.CardHolder holder, int position) {
        CardHolder h = (CardHolder) holder;
        h.setViewItem(list_data_card.get(position));
    }

    @Override
    public int getItemCount() {
        return list_data_card.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        private TextView text_no, text_card_id;
        public CardHolder(View itemView) {
            super(itemView);
            text_no = (TextView) itemView.findViewById(R.id.text_no);
            text_card_id = (TextView) itemView.findViewById(R.id.text_card_id);
        }

        public void setViewItem(CardModel cardModel) {
            text_no.setText(cardModel.getNo());
            text_card_id.setText(cardModel.getCard_id());
            int color = Color.WHITE;
            if(getAdapterPosition() % 2 != 0){
                color = ContextCompat.getColor(context, R.color.table);
            }
            text_no.setBackgroundColor(color);
            text_card_id.setBackgroundColor(color);
        }
    }
}
