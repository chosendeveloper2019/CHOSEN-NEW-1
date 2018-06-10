package chosen.com.chosen.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chosen.com.chosen.Model.UserModel;
import chosen.com.chosen.R;

public class FragmentRegisterCard extends Fragment{

    private static final String KEY_DATA_USER = "KEY_DATA_USER";

    public static FragmentRegisterCard newInstance(UserModel userModel) {
        FragmentRegisterCard fragment = new FragmentRegisterCard();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_card,container,false);
        initInstance(v);
        return v;
    }

    private void initInstance(View v) {


    }
}
