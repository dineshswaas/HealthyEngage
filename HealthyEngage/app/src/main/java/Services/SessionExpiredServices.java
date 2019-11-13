package Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.swaas.healthyengage.GlobalApplication;

import utils.Constants;

public class SessionExpiredServices extends Service {
    public static CountDownTimer timer;
    @Override
    public void onCreate(){
        super.onCreate();
        timer = new CountDownTimer(1 *60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                //Some code
                Log.v("", "Service Started");
            }

            public void onFinish() {
                Log.v("", "Call Logout by Service");
                Toast.makeText(GlobalApplication.sThis,"Expired",Toast.LENGTH_LONG).show();
                stopSelf();
            }
        };
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
