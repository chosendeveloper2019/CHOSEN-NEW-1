package chosen_new.com.chosen;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;

import chosen_new.com.chosen.Util.ConnectivityReceiverUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by workmyhappy@gmail.com on 3/1/2018 AD.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initFont();

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiverUtil.ConnectivityReceiverListener listener) {
        ConnectivityReceiverUtil.connectivityReceiverListener = listener;
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/kanitregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

//        Toast.makeText(mInstance, "Font init " , Toast.LENGTH_SHORT).show();
    }

    LocalizationApplicationDelegate localizationDelegate = new LocalizationApplicationDelegate(this);

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localizationDelegate.onConfigurationChanged(this);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }


}
