package application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Asus on 2016/08/29.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
