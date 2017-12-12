package bidmart.fyp.pk.bidmartpk;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by CyberHJ on 10/29/2017.
 */

public class SplashTimer extends Application {

    @Override
    public void onCreate(){

        super.onCreate();
        SystemClock.sleep(2000);
    }
}
