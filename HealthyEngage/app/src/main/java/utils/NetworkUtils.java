package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtils {

    public static boolean isNetworkAvailable(final Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            if (ctx != null &&!((Activity) ctx).isFinishing()) {//crashlytics #2171
                //showNewAlertDialog(ctx);
                Toast.makeText(ctx,"Network is disabled in your device. Would you like to enable it ?",Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

/*
    private static void showNewAlertDialog(final Context context) {
        Activity activity = (Activity)context;
        new FancyAlertDialog.Builder(activity)
                .setTitle("Network Connection")
                .setBackgroundColor(Color.parseColor("#17487A"))  //Don't pass R.color.colorvalue
                .setMessage("Network is disabled in your device. Would you like to enable it ?")
                .setNegativeBtnText(context.getResources().getString(R.string.no))
                .setPositiveBtnBackground(Color.parseColor("#17487A"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText(context.getResources().getString(R.string.yes))
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_error_outline_black_24dp,Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick(String s) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick(String s) {

                    }
                })
                .build();

    }
*/

}
