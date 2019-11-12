package Alerts;

import android.content.Context;
import android.graphics.Typeface;

public class IOSDialogBuilder {
    private Typeface tf;
    private boolean bold,cancelable,singleView;
    private String title, subtitle, okLabel, koLabel,singleButtonText;
    private Context context;
    private IOSDialogClickListener positiveListener;
    private IOSDialogClickListener negativeListener;
    private IOSDialogClickListener singlePositiveListener;

    public IOSDialogBuilder(Context context) {
        this.context = context;
    }

    public IOSDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public IOSDialogBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public IOSDialogBuilder setBoldPositiveLabel(boolean bold) {
        this.bold = bold;
        return this;
    }

    public IOSDialogBuilder setSingleText(String  singleButtonText) {
        this.singleButtonText = singleButtonText;
        return this;
    }

    public IOSDialogBuilder setSingleButtonView(boolean view) {
        this.singleView = view;
        return this;
    }

    public IOSDialogBuilder setFont(Typeface font) {
        this.tf=font;
        return this;
    }
    public IOSDialogBuilder setCancelable(boolean cancelable){
        this.cancelable=cancelable;
        return this;
    }

    public IOSDialogBuilder setNegativeListener(String koLabel, IOSDialogClickListener listener) {
        this.negativeListener=listener;
        this.koLabel=koLabel;
        return this;
    }

    public IOSDialogBuilder setPositiveListener(String okLabel, IOSDialogClickListener listener) {
        this.positiveListener = listener;
        this.okLabel=okLabel;
        return this;
    }

    public IOSDialogBuilder setSinglePositiveListener(String okLabel, IOSDialogClickListener listener) {
        this.singlePositiveListener = listener;
        this.singleButtonText=okLabel;
        return this;
    }

    public IOSDialog build(){
        IOSDialog dialog = new IOSDialog(context,title,subtitle, bold, tf,cancelable,singleView);
        dialog.setNegative(koLabel,negativeListener);
        dialog.setPositive(okLabel,positiveListener);
        dialog.setSinglePositive(singleButtonText,singlePositiveListener);
        return dialog;
    }
}
