package com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventSource;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;

import java.util.UUID;

/**
 * Created by ciencias on 26.01.15.
 */
public class DeviceUserCreatedEvent implements PlatformEvent {

    private UUID userId;
    private EventType eventType;
    private EventSource eventSource;

    public void setUserId (UUID userId){
        this.userId = userId;
    }

    public UUID getUserId() {
        return this.userId;
    }


    public DeviceUserCreatedEvent(EventType eventType){
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
