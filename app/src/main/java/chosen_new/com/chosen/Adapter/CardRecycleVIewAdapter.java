package chosen_new.com.chosen.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Fragment.FragmentShowInvoice;
import chosen_new.com.chosen.Fragment.FragmentCarModel;
import chosen_new.com.chosen.Fragment.FragmentStateCharting;
import chosen_new.com.chosen.Fragment.FragmentViewCard;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;

public class CardRecycleVIewAdapter extends RecyclerView.Adapter<CardRecycleVIewAdapter.MyHolder> {

    private Context context;
    private List<Map<String,Object>> val;
    private String TAG = "<CardRecycleVIewAdapter>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    public CardRecycleVIewAdapter(Context context){

        this.context = context;

        sh = ((AppCompatActivity)context).getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);
        editor = sh.edit();

    }

    public void UpdateData(List<Map<String,Object>> val){
           this.val = val;

    }

    @Override
    public CardRecycleVIewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_view,parent,false);
        return new MyHolder(v,context);
    }

    @Override
    public void onBindViewHolder(CardRecycleVIewAdapter.MyHolder holder, final int position) {

        String pageNow = sh.getString(MyFerUtil.KEY_PAGE_NOW,"");
        String path = ""+val.get(position).get(MyFerUtil.CAR_URL);

        try{
            Picasso.with(context).load(path).resize(100,100)
                    .into(holder.imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

//        Log.e(TAG+" IMG",""+val.get(position).get(MyFerUtil.CAR_URL));
        if(!pageNow.equals(MyFerUtil.ADD_CARD)){

           holder.btn_invoice.setVisibility(View.GONE);
           holder.btn_car_model.setVisibility(View.GONE);


        }else {

            holder.btn_invoice.setVisibility(View.VISIBLE);
            holder.btn_car_model.setVisibility(View.VISIBLE);

        }

        String CAR = ""+val.get(position).get(MyFerUtil.CAR_MODEL);

        if(!CAR.isEmpty()){

//        Log.e(TAG,""+val.get(position).get(MyFerUtil.CAR_MODEL));
            holder.tv_car_model.setText(""+val.get(position).get(MyFerUtil.CAR_MODEL));

        }else {

            holder.tv_car_model.setText("  ");

        }

        holder.btn_car_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(MyFerUtil.KEY_SELECT_CARD,
                        val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
                editor.commit();

                fragmentTran(new FragmentCarModel(),null);

            }
        });

        holder.btn_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(MyFerUtil.KEY_SELECT_CARD,val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
                editor.putString(MyFerUtil.KEY_TRANSID,"");  // put null
                editor.putString(MyFerUtil.CAR_URL,""+val.get(position).get(MyFerUtil.CAR_URL));
                editor.commit();

                String stateFrg = sh.getString(MyFerUtil.STATE_FRG,MyFerUtil.VIEW_CARD);

                if (stateFrg.equals(MyFerUtil.ADD_CARD)){

                    FragmentShowInvoice showInvoice = new FragmentShowInvoice();
                    fragmentTran(showInvoice,null);

                }else{
                    FragmentStateCharting stateCharting = new FragmentStateCharting();
                    fragmentTran(stateCharting,null);
                    Log.e(TAG,""+val.get(position).get(MyFerUtil.CAR_URL));
                }
            }
        });

        if(val.get(position).get(MyFerUtil.KEY_STATE).toString().equals("DISCHARGING")){

            holder.imageView.setImageResource(R.drawable.available);
        }else {

           holder.imageView.setImageResource(R.drawable.charging);

        }

        holder.tv_card_id.setText(val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());

        holder.setOnClickRecycleView(new RecycleViewOnClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                try {

//                    if(val.get(position).get(MyFerUtil.KEY_STATE).toString().equals("DISCHARGING")){


                        editor.putString(MyFerUtil.KEY_SELECT_CARD,val.get(position).get(MyFerUtil.KEY_CARD_ID).toString());
                        editor.putString(MyFerUtil.KEY_TRANSID,"");  // put null
                        editor.commit();

                        String stateFrg = sh.getString(MyFerUtil.STATE_FRG,MyFerUtil.VIEW_CARD);

                        if (stateFrg.equals(MyFerUtil.ADD_CARD)){

                            FragmentShowInvoice showInvoice = new FragmentShowInvoice();
                            fragmentTran(showInvoice,null);

                        }else{

                            FragmentStateCharting stateCharting = new FragmentStateCharting();
                            fragmentTran(stateCharting,null);


                        }


//                    }else if(val.get(position).get(MyFerUtil.KEY_STATE).toString().equals("CHARGING")){

//                    }

                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }



            }

            @Override
            public void onLongClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
//                    Toast.makeText(context, "Long Click "+val.get(position).get(MyFerUtil.KEY_STATE), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clear() {
        final int size = val.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                val.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return val.size();
        }catch (Exception e){
//            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            FragmentViewCard.newInstance(null);
            return 0;

        }

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.container, fragment).addToBackStack(null).commit();

    }

    public class MyHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener{
        Context context;

        TextView tv_card_id,tv_car_model;
        ImageView imageView;
        Button btn_car_model,btn_invoice;


        RecycleViewOnClickListener listener;

        public MyHolder(View v,Context context) {

            super(v);

            this.context = context;
            tv_card_id = v.findViewById(R.id.tv_card_show);
            tv_car_model = v.findViewById(R.id.tv_car_name);
            btn_car_model = v.findViewById(R.id.btn_carmodel);
            btn_invoice = v.findViewById(R.id.btn_invoice);
            imageView = v.findViewById(R.id.imgState);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void setOnClickRecycleView(RecycleViewOnClickListener listener){

            this.listener =  listener;

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), false, null);

        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(v, getAdapterPosition(), false, null);

            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onClick(v, getAdapterPosition(), false, event);

            return false;
        }
    }


}
