package chosen_new.com.chosen.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import chosen_new.com.chosen.R;
import chosen_new.com.chosen.Util.MyFerUtil;

import static chosen_new.com.chosen.Util.UrlUtil.URL_PAYMENT_WEB;

public class FragmentPaypalWebview extends Fragment{

    WebView mWebView;
    SharedPreferences sharedPreferences;
    String invoiceId;
    private String TAG = "<FragmentPaypalWebview>";
    private String cardId = "";
    private Context context;
    private FragmentManager manager;
    private String urlLoad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_paypal_web,container,false);

            initInstance(v);

        return v;
    }

    private void initInstance(View v){

        context = getContext();
        sharedPreferences = getActivity().getSharedPreferences(MyFerUtil.MY_FER, Context.MODE_PRIVATE);

        urlLoad = sharedPreferences.getString(MyFerUtil.KEY_URL_LOAD,"");

        manager = getActivity().getSupportFragmentManager();

        invoiceId = sharedPreferences.getString(MyFerUtil.KEY_PAY_CODE,"");

        mWebView = v.findViewById(R.id.web_paypal);
//        mWebView.loadUrl(URL_PAYMENT_WEB+ invoiceId);
        mWebView.loadUrl(urlLoad);

        cardId = sharedPreferences.getString(MyFerUtil.KEY_SELECT_CARD,"");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("CARDID : "+cardId);


        FloatingActionButton fab = v.findViewById(R.id.fab_addCard);
//        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //click fab
                    mWebView.goBack();
                    String webUrl = mWebView.getUrl();

                    String title = ""+((AppCompatActivity) getActivity()).getSupportActionBar().getTitle();
//                    Log.e(TAG,""+((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());

                    if (webUrl.equals(urlLoad)){
                        manager.popBackStack();
                        PaypalFragment paypalFragment = new PaypalFragment();
                        fragmentTran(paypalFragment, null);
//                        if(title.equals("PAYMENT")) {
//
//                        }
//                        }else if(title.equals("CHARGE")){
//                            FragmentViewCard paypalFragment = new FragmentViewCard();
//                            fragmentTran(paypalFragment,null);
//                        }else if(title.equals("CARDID")){
//                            FragmentManageCard paypalFragment = new FragmentManageCard();
//                            fragmentTran(paypalFragment,null);
//                        }


                    }


                }catch (Exception e){

                }

            }
        });

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

    }

    public void fragmentTran(Fragment fragment, Bundle bundle){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }
}
