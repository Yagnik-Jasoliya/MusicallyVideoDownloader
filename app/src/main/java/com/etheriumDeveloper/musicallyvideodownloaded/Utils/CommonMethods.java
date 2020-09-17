package com.etheriumDeveloper.musicallyvideodownloaded.Utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

//import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class CommonMethods {
    public static Boolean is_a_premium_member = null;
//    public static MixpanelAPI mixpanel = null;
    public static final String pathName1 = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Musical.ly Video Downloader/");
    public static final String projectToken = "47f063c03b84383aceccce72bc68a330";
    public static ArrayList<Integer> selectedPosition = new ArrayList();

    public static boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public static void createDirectory() {
        System.out.println("Step 1::");
        if (isExternalStorageWritable()) {
            System.out.println("Step 1::");
            File file = new File(pathName1);
            System.out.println("Step 1::");
            if (file.exists()) {
                System.out.println(":::Directory already created" + file.getAbsolutePath());
                return;
            }
            file.mkdir();
            System.out.println(":::Directory created successfully" + file.getAbsolutePath());
            return;
        }
        System.out.println("Not enough space");
    }

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
