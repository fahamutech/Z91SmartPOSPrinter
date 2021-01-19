package com.fahamutech.z91smartpo;

import android.app.Activity;
import android.text.Layout;

import com.fahamutech.z91smartpo.utils.DialogUtils;
import com.szzcs.smartpos.R;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.print.PrnStrFormat;

import java.util.concurrent.ExecutorService;

public class Z91PrinterPlugin {
    private DriverManager mDriverManager = DriverManager.getInstance();
    private ExecutorService mSingleThreadExecutor;
    private Printer mPrinter;

    public void init() {
        mSingleThreadExecutor = mDriverManager.getSingleThreadExecutor();
        mPrinter = mDriverManager.getPrinter();
    }

    public void printText(final String data, final Activity homeActivity) {
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
                    mPrinter.setPrintAppendString("\n", format);
                    mPrinter.setPrintAppendString(data, format);
                    mPrinter.setPrintAppendString("\n", format);
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

    public void printQr(final String qr, final Activity homeActivity) {
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
                    mPrinter.setPrintAppendString("\n", format);
                    mPrinter.setPrintAppendQRCode(qr, 200, 200, Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString("\n", format);
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
}
