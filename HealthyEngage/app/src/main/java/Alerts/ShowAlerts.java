package Alerts;

import android.content.Context;

public class ShowAlerts  {

    Context context;
   void ShowAlerts(Context context){
        this.context = context;
    }


    void showAlert(String title,String message){
        new IOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(message)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setSingleButtonView(true)
                .setPositiveListener("",null)
                .setNegativeListener("",null)
                .setSinglePositiveListener("OK", new IOSDialogClickListener() {
                    @Override
                    public void onClick(IOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

}
