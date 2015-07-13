package com.bitdubai.fermat_api.layer.pip_platform_service.event_manager;

import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;

/**
 * Created by ciencias on 24.01.15.
 */
public interface EventListener {

    public EventType getEventType();

    public void setEventHandler(EventHandler eventHandler);

    public EventHandler getEventHandler();

    public void raiseEvent(PlatformEvent platformEvent);

}
