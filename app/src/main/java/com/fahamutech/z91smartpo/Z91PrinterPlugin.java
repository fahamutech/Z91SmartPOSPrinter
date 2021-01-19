package com.fahamutech.z91smartpo;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fahamutech.z91smartpo.utils.Config;
import com.fahamutech.z91smartpo.utils.DialogUtils;
import com.fahamutech.z91smartpo.utils.SDK_Result;
import com.fahamutech.z91smartpo.utils.SystemInfoUtils;
import com.szzcs.smartpos.BuildConfig;
import com.szzcs.smartpos.R;
import com.zcs.sdk.ConnectTypeEnum;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;
import com.zcs.sdk.print.PrnStrFormat;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Z91PrinterPlugin {
    private DriverManager mDriverManager = DriverManager.getInstance();
    private ExecutorService mSingleThreadExecutor;
    private Printer mPrinter;
    private Sys mSys = mDriverManager.getBaseSysDevice();

    public void init() {
        mSingleThreadExecutor = mDriverManager.getSingleThreadExecutor();
        mPrinter = mDriverManager.getPrinter();
        new SettingsFragment().initSdk(false);
        this.initSdk(false);
    }

    Z91PrinterPlugin(){
        // this.init();
    }

    public void printText(final String data) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
//                    homeActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));
//
//                        }
//                    });
                } else {

                    PrnStrFormat format = new PrnStrFormat();
//                    mPrinter.setPrintStart();
                    mPrinter.setPrintAppendString("\n", format);
                    mPrinter.setPrintAppendString(data, format);
                    mPrinter.setPrintAppendString("\n", format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
//                        homeActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));
//
//                            }
//                        });
                    }
                }
            }
        });
    }

    public void printQr(final String qr) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int printStatus = mPrinter.getPrinterStatus();
                Log.e("tag*****", String.valueOf(printStatus));
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
//                    homeActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));
//
//                        }
//                    });
                } else {
                    PrnStrFormat format = new PrnStrFormat();
                    mPrinter.setPrintAppendString("\n", format);
                    mPrinter.setPrintAppendQRCode(qr, 200, 200, Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString("\n", format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
//                        homeActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));
//
//                            }
//                        });
                    }
                }
            }
        });
    }

    public void customPrint(final Activity homeActivity, final Z91PrinterCustomPrintCallback callback) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    homeActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));

                        }
                    });
                } else {
                    PrnStrFormat format = new PrnStrFormat();
                    callback.exec(homeActivity, mPrinter, format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                        homeActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.show(homeActivity, homeActivity.getString(R.string.printer_out_of_paper));

                            }
                        });
                    }
                }
            }
        });
    }

    //int speed = 115200;
    private void initSdk(final boolean reset) {
        // Config the SDK base info
         mSys.showLog(true);
//        if (mDialogInit == null) {
//            mDialogInit = (ProgressDialog) DialogUtils.showProgress(mActivity, getString(R.string.title_waiting), getString(R.string.msg_init));
//        } else if (!mDialogInit.isShowing()) {
//            mDialogInit.show();
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int statue = mSys.getFirmwareVer(new String[1]);
                if (statue != SdkResult.SDK_OK) {
                    int sysPowerOn = mSys.sysPowerOn();
                    Log.i("z91****", "sysPowerOn: " + sysPowerOn);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mSys.setUartSpeed(115200);
                final int i = mSys.sdkInit();
                if (i == SdkResult.SDK_OK) {
                    setDeviceInfo();
                }
//                if (reset && ++count < 2 && i == SdkResult.SDK_OK && mSys.getCurSpeed() != 460800) {
//                    Log.d(TAG, "switch baud rate, cur speed = " + mSys.getCurSpeed());
//                    int ret = mSys.setDeviceBaudRate();
//                    if (ret != SdkResult.SDK_OK) {
//                        // DialogUtils.show(getActivity(), "SwitchBaudRate error: " + ret);
//                    }
//                    mSys.sysPowerOff();
//                    initSdk();
//                    return;
//                }
//                if (mActivity != null) {
//                    mActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            if (mDialogInit != null)
////                                mDialogInit.dismiss();
//                            Log.d("z91***", "Cur speed: " + mSys.getCurSpeed());
//                            if (BuildConfig.DEBUG && mSys.getConnectType() == ConnectTypeEnum.COM) {
//                                //  DialogUtils.show(getActivity(), "Cur speed: " + mSys.getCurSpeed());
//                            }
//                            String initRes = (i == SdkResult.SDK_OK) ? "Done init" : "Fail to init";
//
//                            // Toast.makeText(getActivity(), initRes, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        }).start();
    }

    private void setDeviceInfo() {
        // 读取并判断, 不存在则存入
        byte[] info = new byte[1000];
        byte[] infoLen = new byte[2];
        int getInfo = mSys.getDeviceInfo(info, infoLen);
        if (getInfo == SdkResult.SDK_OK) {
            int len = infoLen[0] * 256 + infoLen[1];
            byte[] newInfo = new byte[len];
            System.arraycopy(info, 0, newInfo, 0, len);
            String infoStr = new String(newInfo);
            Log.i("z91****", "getDeviceInfo: " + getInfo + "\t" + len + "\t" + infoStr);
            if (!TextUtils.isEmpty(infoStr)) {
                String[] split = infoStr.split("\t");
                // 已存则返回
                try {
                    // 确保imei1和mac 存值正确
                    if (split.length >= 4) {
                        String val1 = split[0].split(":")[1];
                        String val4 = split[3].split(":")[1];
                        if (!TextUtils.isEmpty(val1) && !val1.equals("null") && val1.length() >= 15
                                && !TextUtils.isEmpty(val4) && !val4.equals("null") && val4.length() >= 12 && val4.contains(":")) {
                            Log.i("z91****", "Have saved, return");
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Map<String, String> map = SystemInfoUtils.getImeiAndMeid(mActivity.getApplicationContext());
        // String imei1 = map.get("imei1");
        // String imei2 = map.get("imei2");
        // String meid = map.get("meid");
        String mac = SystemInfoUtils.getMac();
        // WifiManager wifi = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        long start = System.currentTimeMillis();
//        while (TextUtils.isEmpty(mac) && System.currentTimeMillis() - start < 5000) {
//            if (!wifi.isWifiEnabled()) {
//                wifi.setWifiEnabled(true);
//            }
//            mac = SystemInfoUtils.getMac();
//        }
//        Log.i(TAG, "mac = " + mac);
//        if (TextUtils.isEmpty(mac)) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    DialogUtils.show(mActivity, "Warning! No mac!!!");
//                }
//            });
//            return;
//        }
        String msg = ""; // "IMEI1:" + (imei1 == null ? "" : imei1) + "\t" + "IMEI2:" + (imei2 == null ? "" : imei2) + "\t" + "MEID:" + (meid == null ? "" : meid) + "\t" + "MAC:" + mac;
//        Log.i(TAG, "readDeviceInfo: " + msg);
        byte[] bytes = msg.getBytes();
        int setInfo;
        int count = 0;
        do {
            setInfo = mSys.setDeviceInfo(bytes, bytes.length);
            Log.i("z91***", "setDeviceInfo: " + setInfo);
            if (setInfo == SdkResult.SDK_OK) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (count++ < 5);
    }
}
