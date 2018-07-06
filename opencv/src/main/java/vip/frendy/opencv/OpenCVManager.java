package vip.frendy.opencv;

import android.graphics.Bitmap;

/**
 * Created by frendy on 2018/7/6.
 */

public class OpenCVManager {

    private static OpenCVManager manager;

    public static OpenCVManager getInstance() {
        if(manager == null) {
            manager = new OpenCVManager();
        }
        return manager;
    }

    private OpenCVManager() {
        System.loadLibrary("CVDroid");
    }

    public native Bitmap toBW(Bitmap bitmap);

}
