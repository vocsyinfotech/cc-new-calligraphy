package com.Example.textart.calligrapy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class ScorpionNetwork {
    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(0).getState() == State.CONNECTED || connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED) {
            return true;
        }
        return false;
    }

    public static void showDialog(final Activity activity, String str, String str2, final boolean z, boolean z2) {
        AlertDialog create = new Builder(activity).setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (z) {
                    activity.finish();
                }
            }
        }).create();
        if (str != null) {
            create.setTitle(str);
        }
        if (str2 != null) {
            create.setMessage(str2);
        }
        create.show();
        create.setCancelable(z2);
    }
}
