package com.softwareoverflow.hangtight.helper;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardHelper {

    public static void hide(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            // Do nothing
        }
    }
}
