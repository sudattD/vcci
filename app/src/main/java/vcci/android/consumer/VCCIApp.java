package vcci.android.consumer;

import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import vcci.android.consumer.util.LocaleManager;
import vcci.android.consumer.util.Stash;
import vcci.android.consumer.util.TypefaceUtil;


public class VCCIApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans-Regular.ttf");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }
}
