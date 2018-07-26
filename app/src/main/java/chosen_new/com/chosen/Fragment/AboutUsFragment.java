package chosen_new.com.chosen.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chosen_new.com.chosen.Model.UserModel;
import chosen_new.com.chosen.R;

public class AboutUsFragment extends Fragment{
    private static final String TAG = AboutUsFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";;

    public AboutUsFragment newInstance(UserModel userModel){
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public AboutUsFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_contact_us));

    }
}
