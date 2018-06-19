package chosen_new.com.chosen.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chosen_new.com.chosen.R;

public class FragmentReserveDetail extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_reserve_detail,container,false);
        initInstance(v);
        return v;
    }

    private void initInstance(View v) {

    }

    @Override
    public void onClick(View view) {

    }
}
