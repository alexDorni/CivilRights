package com.example.heroalex.copscivilslicense.utils;

import android.content.Context;
import android.widget.Toast;


public class ViewUtils {

    public static void showToastMessage(Context context, int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

}
