package com.example.photos;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Dheeraj on 27-01-2018.
 */

public class Utils {

    /**
     * Method to close the keyboard
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method to set the background on any widget.
     *
     * @param context      Context of the activity.
     * @param drawablePath The path to the drawable.
     * @return Returns the desired drawable.
     */
    public static Drawable setDrawable(Context context, int drawablePath) {
        if (Build.VERSION.SDK_INT > 21) {
            return context.getResources().getDrawable(drawablePath, context.getTheme());
        } else {
            return context.getResources().getDrawable(drawablePath);
        }
    }

    /**
     * Method to check if network is available or not
     * @param _context Context object
     * @return true if available else false
     */
    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    /**
     * Method to open the keyboard
     * @param view
     * @param context
     */
    public static void openKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
