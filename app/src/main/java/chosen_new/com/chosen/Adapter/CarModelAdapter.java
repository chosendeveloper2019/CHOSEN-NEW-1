package chosen_new.com.chosen.Adapter;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import chosen_new.com.chosen.Api.CallbackSelectCarListener;
import chosen_new.com.chosen.Fragment.FragmentShowInvoice;
import chosen_new.com.chosen.Api.NetworkConnectionManager;
import chosen_new.com.chosen.Fragment.FragmentStateCharting;
import chosen_new.com.chosen.Fragment.FragmentViewCard;
import chosen_new.com.chosen.Model.SelectCarModel;
import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;
import okhttp3.ResponseBody;

public class CarModelAdapter extends RecyclerView.Adapter<CarModelAdapter.MyHolder>  {
    private Context context;
    private List<Map<String,Object>> val;
    private String TAG = "<CarModelAdapter>";
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;

    public CarModelAdapter(Context context){

        this.context = context;

        sh = ((AppCompatActivity)context).getSharedPreferences(MyFerUtil.MY_FER,Context.MODE_PRIVATE);

        editor = sh.edit();
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

    }

    public void UpdateData(List<Map<String,Object>> val){
        this.val = val;

    }

    @Override
    public CarModelAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_carmodel,parent,false);
        return new CarModelAdapter.MyHolder(v,context);
    }

    @Override
    public void onBindViewHolder(CarModelAdapter.MyHolder holder, final int position) {

        Picasso.with(context).load(val.get(position).get(MyFerUtil.CAR_URL).toString()).into(holder.img_car);
        holder.tv_carmodel.setText(val.get(position).get(MyFerUtil.CAR_MODEL).toString());
        holder.tv_brand.setText(val.get(position).get(MyFerUtil.CAR_BRAND).toString());

        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(((AppCompatActivity)context).getString(R.string.msgLoading));
                progressDialog.show();

                // api call choose car for card
                new NetworkConnectionManager().callSelectCar(listener,""+val.get(position).get(MyFerUtil.CAR_ID)
                        ,""+val.get(position).get(MyFerUtil.KEY_SELECT_CARD));

//                Toast.makeText(context, ""+val.get(position).get(MyFerUtil.CAR_ID) +
//                        "\n"+val.get(position).get(MyFerUtil.KEY_SELECT_CARD) , Toast.LENGTH_SHORT).show();

            }
        });


        holder.setOnClickRecycleView(new RecycleViewOnClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
//                try {
//
//
//
//                    String stateFrg = sh.getString(MyFerUtil.STATE_FRG,"");
//
//                    if (stateFrg.equals(MyFerUtil.ADD_CARD)){
//                        FragmentShowInvoice showInvoice = new FragmentShowInvoice();
//                        fragmentTran(showInvoice,null);
//                    }else{
//                        FragmentStateCharting stateCharting = new FragmentStateCharting();
//                        fragmentTran(stateCharting,null);
//                        Log.e(TAG,stateFrg);
//                    }
//
//
//
//                }catch (Exception e){
//                    Log.e(TAG,e.getMessage());
//                }



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

        TextView tv_brand,tv_carmodel;
        ImageView img_car;
        Button btn_select;


        RecycleViewOnClickListener listener;

        public MyHolder(View v,Context context) {

            super(v);

            this.context = context;

            tv_brand = v.findViewById(R.id.tv_brands);
            tv_carmodel = v.findViewById(R.id.tv_carmodel);
            img_car = v.findViewById(R.id.img_car);
            btn_select = v.findViewById(R.id.btn_select);

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

    //  call back  choose car for card

    CallbackSelectCarListener listener = new CallbackSelectCarListener() {
        @Override
        public void onResponse(SelectCarModel res) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(context, ""+res.getMgs(), Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
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
