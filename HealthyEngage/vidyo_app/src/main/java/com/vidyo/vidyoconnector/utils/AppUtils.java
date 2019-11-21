package com.vidyo.vidyoconnector.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.vidyo.vidyoconnector.BuildConfig;
import com.vidyo.vidyoconnector.connect.ConnectParams;

import java.io.File;

public class AppUtils {

    private static final String LOGS_FOLDER = "VidyoConnectorLogs";
    private static final String LOG_FILE = "VidyoConnectorLog.log";

    public static String formatToken() {
        String token = ConnectParams.TOKEN;
        if (TextUtils.isEmpty(token)) return "";

        if (token.length() < 8) return token;

        return token.substring(0, 8).concat("...");
    }

    /**
     * Log file is create individually for every session
     *
     * @param context {@link Context}
     * @return log file path
     */
    public static String configLogFile(Context context) {
        File cacheDir = context.getCacheDir();
        File logDir = new File(cacheDir, LOGS_FOLDER);
        deleteRecursive(logDir);

        File logFile = new File(logDir, LOG_FILE);
        logFile.mkdirs();

        String[] logFiles = logDir.list();
        if (logFiles != null)
            for (String file : logFiles) Logger.i(AppUtils.class, "Cached log file: " + file);

        return logFile.getAbsolutePath();
    }

    /**
     * Expose log file URI for sharing.
     *
     * @param context {@link Context}
     * @return log file uri.
     */
    private static Uri logFileUri(Context context) {
        File cacheDir = context.getCacheDir();
        File logDir = new File(cacheDir, LOGS_FOLDER);
        File logFile = new File(logDir, LOG_FILE);

        if (!logFile.exists()) return null;

        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".file.provider", logFile);
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    /**
     * Send email with log file
     */
    public static void sendLogs(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Vidyo Connector Sample Logs");
        intent.putExtra(Intent.EXTRA_TEXT, "Logs attached..." + additionalInfo());

        intent.putExtra(Intent.EXTRA_STREAM, logFileUri(context));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(Intent.createChooser(intent, "Choose sender..."));
        } catch (Exception sendReportEx) {
            sendReportEx.printStackTrace();
        }
    }

    private static String additionalInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("\n\nModel: ").append(Build.MODEL);
        builder.append("\n").append("Manufactured: ").append(Build.MANUFACTURER);
        builder.append("\n").append("Brand: ").append(Build.BRAND);
        builder.append("\n").append("Android OS version: ").append(Build.VERSION.RELEASE);
        builder.append("\n").append("Hardware : ").append(Build.HARDWARE);
        builder.append("\n").append("SDK Version : ").append(Build.VERSION.SDK_INT);

        builder.append("\n").append("Resource : ").append(ConnectParams.RESOURCE);
        builder.append("\n").append("Token : ").append(ConnectParams.TOKEN);

        return builder.toString();
    }
}