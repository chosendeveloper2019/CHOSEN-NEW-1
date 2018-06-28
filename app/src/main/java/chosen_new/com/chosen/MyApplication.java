package chosen_new.com.chosen;

import android.app.Application;

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
                .setDefaultFontPath("fonts/quicksand-regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
