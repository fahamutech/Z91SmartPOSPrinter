package com.fahamutech.z91smartpo;

import android.app.Application;

import com.fahamutech.z91smartpo.utils.Config;
import com.zcs.sdk.card.CardInfoEntity;

/**
 * Created by yyzz on 2018/5/18.
 */

public class MyApp extends Application {
    public static CardInfoEntity cardInfoEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        cardInfoEntity = new CardInfoEntity();

        Config.init(this)
                .setUpdate();
//        new SettingsFragment().initSdk();
//        new Z91PrinterPlugin()
//                .printText("Hello, World\n\n\n\n\n\n\\n\n\n\n\n\n\n");
//
//        new Z91PrinterPlugin()
//                .printQr("Hello");

    }
}
