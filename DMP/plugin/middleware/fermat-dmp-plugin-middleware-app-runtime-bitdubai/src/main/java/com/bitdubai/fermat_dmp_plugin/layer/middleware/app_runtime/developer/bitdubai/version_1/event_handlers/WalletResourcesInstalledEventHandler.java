package com.bitdubai.fermat_dmp_plugin.layer.middleware.app_runtime.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.AppRuntimeManager;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;

/**
 * Created by loui on 18/02/15.
 */
public class WalletResourcesInstalledEventHandler implements EventHandler {
    AppRuntimeManager appRuntimeManager;

    public void setAppRuntimeManager ( AppRuntimeManager appRuntimeManager){
        this.appRuntimeManager = appRuntimeManager;
    }
    
    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {
        
    }
}
