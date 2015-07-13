package com.bitdubai.fermat_dmp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.dmp_network_service.CantCheckResourcesException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_resources.WalletResourcesManager;


/**
 * Created by loui on 17/02/15.
 */
public class BegunWalletInstallationEventHandler implements EventHandler {
    WalletResourcesManager walletResourcesManager;
    
    public void setWalletResourcesManager(WalletResourcesManager walletResourcesManager){
        this.walletResourcesManager = walletResourcesManager;
    }
    
    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {




        if (((Service) this.walletResourcesManager).getStatus() == ServiceStatus.STARTED) {

            try
            {
                this.walletResourcesManager.checkResources();
            }
            catch (CantCheckResourcesException cantCheckResourcesException)
            {
                /**
                 * The main module could not handle this exception. Me neither. Will throw it again.
                 */

                throw cantCheckResourcesException;
            }
        }
        else
        {
            throw new CantCheckResourcesException("CAN'T CHECK WALLET RESOURCES:",null,"Error intalled wallet resources fields" , "");
        }

    }
}
