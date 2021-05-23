package com.trueway.app.uilib.tool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by trueway on 2016/12/28.
 */

public class PermissionsUtil {

    public static final int PERMISSION_CAMERA = 1001;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1002;
    public static final int PERMISSION_WRITE_CONTACTS = 1003;
    public static final int PERMISSION_RECORD_AUDIO = 1004;
    public static final int PERMISSION_VIDEO = 1005;
    public static final int PERMISSION_KEYBOARD = 1006;
    public static final int PERMISSION_READ_CONTACTS = 1007;
    public static final int PERMISSION_CALL_PHONE = 1;

    public static boolean hasCameraPermission(Activity activity) {
        int hasPermission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    PermissionsUtil.PERMISSION_CAMERA);
            return false;
        }
        return true;
    }

}
