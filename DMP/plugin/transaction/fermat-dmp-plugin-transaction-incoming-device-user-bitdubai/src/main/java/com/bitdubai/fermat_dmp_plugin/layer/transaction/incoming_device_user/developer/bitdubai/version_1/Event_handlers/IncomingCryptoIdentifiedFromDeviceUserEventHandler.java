package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1.Event_handlers;

import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import com.bitdubai.fermat_api.layer.dmp_transaction.incoming_device_user.IncomingDeviceUserManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;


/**
 * Created by loui on 23/02/15.
 */
public class IncomingCryptoIdentifiedFromDeviceUserEventHandler implements EventHandler {
    
    IncomingDeviceUserManager incomingDeviceUserManager;
    
    public void setIncomingDeviceUserManager(IncomingDeviceUserManager incomingDeviceUserManager){
        this.incomingDeviceUserManager = incomingDeviceUserManager;
        
    }


    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {
        if (((Service) this.incomingDeviceUserManager).getStatus() == ServiceStatus.STARTED){


        }
        else
        {
            throw new TransactionServiceNotStartedException();
        }
    }
}
