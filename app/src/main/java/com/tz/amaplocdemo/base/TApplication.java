package com.tz.amaplocdemo.base;

import com.tz.amaplocdemo.util.SPUtils;

import org.litepal.LitePalApplication;

public class TApplication extends LitePalApplication {

    private static TApplication app;

    public static TApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        SPUtils.init(app);
        SPUtils.putInt(Constants.SP_COLLECTING_LINE_ID, -1);
        SPUtils.putBoolean(Constants.SP_IS_COLLECTING, false);
    }
}
