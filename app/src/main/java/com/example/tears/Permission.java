package com.example.tears;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class Permission {

    private static String[] PERMISSIONS__STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 判断是否获得外部存储权限
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSIONS__STORAGE, requestCode);//申请权限
                return false;
            }
        }
        return true;
    }

}
