package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.events.IncomingCryptoTransactionsWaitingTransferenceExtraUserEvent;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantSaveEventException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserEventRecorderService;

/**
 * Created by eze on 2015.06.19..
 */
public class IncomingCryptoTransactionsWaitingTransferenceExtraUserEventHandler implements EventHandler {
    /**
     * IncomingCryptoIdentifiedEventHandler member variables
     */
    private final IncomingExtraUserEventRecorderService eventRecorderService;

    public IncomingCryptoTransactionsWaitingTransferenceExtraUserEventHandler(final IncomingExtraUserEventRecorderService eventRecorderService){
        this.eventRecorderService = eventRecorderService;
    }

    /**
     * EventHandler interface implementation
     */
    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {
        if(!eventRecorderService.getStatus().equals(ServiceStatus.STARTED))
            throw new TransactionServiceNotStartedException(TransactionServiceNotStartedException.DEFAULT_MESSAGE, null, null, "Events can't be handled if the service is not started");

        try {
            this.eventRecorderService.incomingCryptoWaitingTransference((IncomingCryptoTransactionsWaitingTransferenceExtraUserEvent) platformEvent);
        } catch (ClassCastException classCastException) {
            /**
             * The main module could not handle this exception. Me neither. Will throw it again.
             */
            throw  new CantSaveEventException(CantSaveEventException.DEFAULT_MESSAGE, FermatException.wrapException(classCastException), null, "Some weird casting happened here");
        }
    }

}