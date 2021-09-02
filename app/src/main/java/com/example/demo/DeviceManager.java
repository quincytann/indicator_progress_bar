package com.example.demo;

import android.content.Context;
import android.provider.Settings;
public class DeviceManager {
    /**
     * Retrieves the name from Settings.Global.DEVICE_NAME
     *
     * @param context A context that can access Settings.Global
     * @return The device name.
     */
    public static String getDeviceName(Context context) {
        return Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
    }

}
