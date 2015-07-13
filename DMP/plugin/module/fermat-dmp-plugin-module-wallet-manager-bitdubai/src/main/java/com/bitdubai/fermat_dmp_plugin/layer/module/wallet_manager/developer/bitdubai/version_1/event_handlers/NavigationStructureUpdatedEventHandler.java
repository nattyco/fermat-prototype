package com.bitdubai.fermat_dmp_plugin.layer.module.wallet_manager.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.dmp_module.ModuleNotRunningException;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.exceptions.CantEnableWalletException;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.WalletManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;

/**
 * Created by loui on 19/02/15.
 */
public class NavigationStructureUpdatedEventHandler implements EventHandler {
    
    WalletManager walletManager;

    public void setWalletManager (WalletManager walletManager){
        this.walletManager = walletManager;
    }

    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {

        if (((Service) this.walletManager).getStatus() == ServiceStatus.STARTED){

            try
            {
                this.walletManager.enableWallet();
            }
            catch (CantEnableWalletException cantEnableWalletException)
            {
                /**
                 * The main module could not handle this exception. Me neither. Will throw it again.
                 */
                System.err.println("CantCreateCryptoWalletException: "+ cantEnableWalletException.getMessage());
                cantEnableWalletException.printStackTrace();

                throw  cantEnableWalletException;
            }
        }
        else
        {
            throw new ModuleNotRunningException();
        }
    }
}
