package com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventSource;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;

/**
 * Created by eze on 2015.06.19..
 */
public class IncomingCryptoTransactionsWaitingTransferenceExtraUserEvent  implements PlatformEvent {
    private EventType eventType;
    private EventSource eventSource;

    public IncomingCryptoTransactionsWaitingTransferenceExtraUserEvent(EventType eventType){
        this.eventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public void setSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    @Override
    public EventSource getSource() {
        return this.eventSource;
    }
}