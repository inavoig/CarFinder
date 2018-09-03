package br.com.rocha.carfinder.util;

import android.content.Context;
import android.location.LocationManager;

public class GPSUtil {

    public static boolean isGPSEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
