package com.szzcs.smartpos;

import android.app.Application;

import com.szzcs.smartpos.utils.Config;
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

    }
}
