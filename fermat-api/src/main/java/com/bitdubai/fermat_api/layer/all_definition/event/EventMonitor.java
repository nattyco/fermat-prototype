package com.bitdubai.fermat_api.layer.all_definition.event;

/**
 * Created by ciencias on 25.01.15.
 */
public interface EventMonitor {

    public void handleEventException (Exception exception, PlatformEvent platformEvent );

}
