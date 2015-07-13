package com.bitdubai.fermat_api.layer.osa_android.location_system;

import com.bitdubai.fermat_api.layer.osa_android.location_system.exceptions.CantGetDeviceLocationException;

/**
 *
 *  <p>The public interfaces <code>LocationManager</code> is a interface
 *     that define the methods to get the geolocation of the device.
 *
 *
 *  @author  Natalia
 *  @version 1.0.0
 *  @since   30/04/15.
 * */
public interface LocationManager {

    public Location getLocation() throws CantGetDeviceLocationException;

    public void setContext (Object context);


}
