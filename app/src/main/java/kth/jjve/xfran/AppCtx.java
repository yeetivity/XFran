package kth.jjve.xfran;

import android.app.Application;
import android.content.Context;

/**
 * Get application context without memory leak
 */

public class AppCtx extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
