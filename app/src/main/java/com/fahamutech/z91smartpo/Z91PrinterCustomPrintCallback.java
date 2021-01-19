package com.fahamutech.z91smartpo;

import android.app.Activity;

import com.zcs.sdk.Printer;
import com.zcs.sdk.print.PrnStrFormat;

public interface Z91PrinterCustomPrintCallback {
    void exec(Activity activity, Printer printer, PrnStrFormat prnStrFormat);
}
