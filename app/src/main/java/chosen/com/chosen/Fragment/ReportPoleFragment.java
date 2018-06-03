package chosen.com.chosen.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import chosen.com.chosen.Adapter.ReportAdapter;
import chosen.com.chosen.Model.CardModel;
import chosen.com.chosen.Model.ReportModel;
import chosen.com.chosen.Model.UserModel;
import chosen.com.chosen.R;
import chosen.com.chosen.Util.ConnectivityReceiverUtil;
import chosen.com.chosen.Util.UrlUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReportPoleFragment extends Fragment implements ConnectivityReceiverUtil.ConnectivityReceiverListener{
    private static final String TAG = ReportPoleFragment.class.getName();
    private static final String KEY_DATA_USER = "KEY_DATA_USER";

    private EditText edittext_start_date, edittext_end_date;
    private TextView button_go, button_clear, TVtotal_charges;
    private Spinner spinner_item;
    private TabLayout tabLayout;
    private ViewPager viewpager_report;
//    private RecyclerView recyclerview;
//    private ProgressBar progressBar;

    private ArrayList<CardModel> list_data_pole;

    private Calendar myCalendar = Calendar.getInstance();

    public static ReportPoleFragment newInstance(UserModel userModel){
        ReportPoleFragment fragment = new ReportPoleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReportPoleFragment(){ }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_pole, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TVtotal_charges = (TextView) view.findViewById(R.id.total_charges);
        spinner_item = (Spinner) view.findViewById(R.id.spinner_item);
        edittext_start_date = (EditText) view.findViewById(R.id.edittext_start_date);
        edittext_end_date = (EditText) view.findViewById(R.id.edittext_end_date);
        button_go = (TextView) view.findViewById(R.id.button_go);
        button_clear = (TextView) view.findViewById(R.id.button_clear);
        viewpager_report = (ViewPager) view.findViewById(R.id.viewpager_report);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final UserModel userModel = (UserModel) getArguments().getSerializable(KEY_DATA_USER);

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStart();
            }

        };
        edittext_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEnd();
            }

        };
        edittext_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        button_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectivityReceiverUtil.isConnected()){
                    String startDate = edittext_start_date.getText().toString();
                    String endDate = edittext_end_date.getText().toString();
                    if(list_data_pole != null && list_data_pole.size() > 0){
                        button_go.setEnabled(false);
//                        progressBar.setVisibility(View.VISIBLE);
                        String pole_id = "";
                        if(spinner_item.getSelectedItemPosition() > 0){
                            pole_id = spinner_item.getSelectedItem().toString();
                        }
                        new LoadDataReportPole(String.valueOf(userModel.getUser_id()), pole_id,
                                startDate, endDate).execute();
                    } else {
                        showSnack("Please, Select Station.");
                    }
                } else {
                    showSnack("Sorry! Not connected to internet");
                }
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list_data_pole.size() > 0){
                    spinner_item.setSelection(0);
                }
                edittext_start_date.setText("");
                edittext_end_date.setText("");
            }
        });

//        progressBar.setVisibility(View.GONE);
        new LoadDataPole(String.valueOf(userModel.getUser_id())).execute();
    }

    private void updateStart() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext_start_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEnd() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext_end_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void showSnack(String message) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class LoadDataPole extends AsyncTask<Void, Void, String> {
        private String user_id;
        public LoadDataPole(String user_id){
            this.user_id = user_id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", user_id)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlUtil.URL_POLE)
                    .post(formBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Exception";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Log.w(TAG, response);
            try {
                JSONArray data = new JSONArray(response);
                list_data_pole = new ArrayList<>();
                ArrayList<String> list_card = new ArrayList<>();
                list_card.add("All Station");
                if(data.length() > 0){
                    for(int i = 0;i < data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        CardModel cardModel = new CardModel();
                        cardModel.setNo(String.valueOf(i + 1));
                        cardModel.setCard_id(object.optString("pole_id"));
                        list_data_pole.add(cardModel);
                        list_card.add(object.optString("pole_id"));
                    }
                    ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.custom_text_spinner, list_card);
                    provinceAdapter.setDropDownViewResource(R.layout.custom_text_spinner);
                    spinner_item.setAdapter(provinceAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadDataReportPole extends AsyncTask<Void, Void, String> {
        private String user_id;
        private String pole_or_card_id;
        private String startDate;
        private String endDate;
        public LoadDataReportPole(String user_id, String pole_or_card_id, String startDate, String endDate){
            this.user_id = user_id;
            this.pole_or_card_id = pole_or_card_id;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected String doInBackground(Void... voids) {
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", user_id)
                    .add("types", "Pole")
                    .add("fromdate", startDate)
                    .add("enddate", endDate)
                    .add("pole_or_card_id", pole_or_card_id)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlUtil.URL_REPORT)
                    .post(formBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Exception";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Log.w(TAG, response);
            try {
                JSONObject obj = new JSONObject(response);
                setupViewPager(viewpager_report, obj.getJSONArray("Datagrid").toString(),
                        obj.getJSONArray("PieChart").toString(),
                        obj.getJSONArray("ComboChart").toString());
                viewpager_report.setOffscreenPageLimit(3);
                try {
                    JSONArray data = new JSONArray(obj.getJSONArray("Datagrid").toString());
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            TVtotal_charges .setText("Charged :"+object.optString("usages") + " "+"Price :"+object.optString("sumary"));

                        }
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            button_go.setEnabled(true);
//            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(ViewPager viewPager, String json_table, String PieChart, String ComboChart) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), getChildFragmentManager());
        adapter.addFrag(StaticReportFragment.newInstance(json_table), "Static");
        adapter.addFrag(PieChartFragment.newInstance(PieChart), "Pie Chart");
        adapter.addFrag(ComboChartFragment.newInstance(ComboChart, "POLE_ID"), "Combo Chart");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context context;
        private int [] tabIcons;
        private int [] TabText;

        public ViewPagerAdapter(Context context, FragmentManager manager) {
            super(manager);
            this.context = context;
            this.tabIcons = tabIcons;
            this.TabText = TabText;
        }

        @Override
        public Fragment getItem(int position) {
//            if(position == 0){
//                return StaticReportFragment.newInstance("");
//            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)
            showSnack("Sorry! Not connected to internet");
    }
}
