package com.bitdubai.android_core.layer._2_os.android.developer.bitdubai.version_1;

import android.content.Context;
import com.bitdubai.fermat_osa_addon.layer.android.location_system.developer.bitdubai.version_1.DeviceLocationOsAddonRoot;
import com.bitdubai.fermat_api.layer.osa_android.LocationSystemOs;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;

/**
 * Created by toshiba on 21/05/2015.
 */
public class AndroidOsLocationSystem implements LocationSystemOs {
    /**
     * LocationSystemOs interface member variables.
     */


    LocationManager locationManager;

    LocationSystemOs locationSystemOs;

    Context context;

    /**
     * Constructor
     */

    public AndroidOsLocationSystem() {

        locationSystemOs =  new DeviceLocationOsAddonRoot();
        this.locationManager = locationSystemOs.getLocationSystem();


    }
    /**
     * LocationSystemOs interface implementation.
     */

    @Override
    public LocationManager getLocationSystem()
    {
        return this.locationManager;
    }

    @Override
    public void setContext(Object context) {

        this.context = (Context) context;
        this.locationManager.setContext(this.context);



    }
}

