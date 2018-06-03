package chosen.com.chosen.View;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import chosen.com.chosen.Model.InfoWindowDataModel;
import chosen.com.chosen.R;

public class CustomMapDetail implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private TextView tv_header,tv_state,tv_detail,tv_start,tv_end;
    private Button btn_reserve;
    private InfoWindowDataModel model;

    public CustomMapDetail(Context ctx,InfoWindowDataModel model){
        context = ctx;
        this.model = model;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_detail, null);

        bindWidget(view,marker);

        return view;
    }

    private void bindWidget(View v,Marker marker){

        // bind widget custom layout
        tv_header = v.findViewById(R.id.tv_header_detail);
        tv_state = v.findViewById(R.id.tv_state);
        tv_detail = v.findViewById(R.id.tv_detail);
        tv_start = v.findViewById(R.id.tv_start);
        tv_end = v.findViewById(R.id.tv_end);
        btn_reserve= v.findViewById(R.id.btn_reserve);

        //get data from model
//        InfoWindowDataModel getData = (InfoWindowDataModel) marker.getTag();

        tv_header.setText(model.getHeaderStr());
        tv_state.setText(model.getStateStr());
        tv_detail.setText(model.getDetailStr());
        tv_start.setText(model.getStartStr());
        tv_end.setText(model.getEndStr());
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, " Reserve :  "+model.getHeaderStr(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
