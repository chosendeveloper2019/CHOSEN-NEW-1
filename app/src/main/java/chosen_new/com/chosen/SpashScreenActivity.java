package chosen_new.com.chosen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SpashScreenActivity  extends AppCompatActivity{

    private Handler handler;
    private Runnable runnable;
    private long delay_time;
    private long time = 3000L;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_splash_screen);

        initInstance();

    }
        // init instance
    private void initInstance(){
        handler = new Handler();

        runnable = new Runnable() {
            public void run() {

                startActivity(new Intent(SpashScreenActivity.this,MainActivity.class));
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
