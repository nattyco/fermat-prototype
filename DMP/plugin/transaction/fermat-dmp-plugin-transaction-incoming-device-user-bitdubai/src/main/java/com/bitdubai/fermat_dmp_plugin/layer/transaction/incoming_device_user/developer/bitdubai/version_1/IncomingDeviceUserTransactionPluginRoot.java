package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.dmp_transaction.incoming_device_user.IncomingDeviceUserManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.DealsWithEvents;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventHandler;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventListener;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;

import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1.Event_handlers.IncomingCryptoIdentifiedFromDeviceUserEventHandler;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1.Event_handlers.IncomingCryptoReceivedFromDeviceUserEventHandler;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1.Event_handlers.IncomingCryptoReceptionConfirmedFromDeviceUserEventHandler;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_device_user.developer.bitdubai.version_1.Event_handlers.IncomingCryptoReversedFromDeviceUserEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by loui on 22/02/15.
 */


/**
 * The Incoming Device User Transaction Manager Plugin is in charge of coordinating the transactions coming from the
 * system, meaning from people user of the platform and the device.
 *
 * This plugin knows which wallet to store the funds.
 *
 * Usually a crypto address is generated from a particular wallet, and that payment should go there, but there is nothing
 * preventing a user to uninstall a wallet and discard the underlying structure in which the user interface was relaying.
 *
 * For that reason it is necessary this middle man, to get sure any incoming payment for any wallet that ever existed is
 * not lost.
 *
 * It can send the funds to a default wallet if some is defined or stored itself until the user manually release them.
 */

public class IncomingDeviceUserTransactionPluginRoot implements Service, IncomingDeviceUserManager, DealsWithEvents, DealsWithErrors, DealsWithPluginFileSystem, Plugin {


    /**
     * PlatformService Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;
    List<EventListener> listenersAdded = new ArrayList<>();

    /**
     * UsesFileSystem Interface member variables.
     */
    PluginFileSystem pluginFileSystem;

    /**
     * DealWithEvents Interface member variables.
     */
    EventManager eventManager;

    /**
     * Plugin Interface member variables.
     */
    UUID pluginId;

    /**
     * PlatformService Interface implementation.
     */


    @Override
    public void start() {
        /**
         * I will initialize the handling of platform events.
         */

        EventListener eventListener;
        EventHandler eventHandler;

        eventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_IDENTIFIED_FROM_DEVICE_USER);
        eventHandler = new IncomingCryptoIdentifiedFromDeviceUserEventHandler();
        ((IncomingCryptoIdentifiedFromDeviceUserEventHandler) eventHandler).setIncomingDeviceUserManager(this);
        eventListener.setEventHandler(eventHandler);
        eventManager.addListener(eventListener);
        listenersAdded.add(eventListener);


        eventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_RECEIVED_FROM_DEVICE_USER);
        eventHandler = new IncomingCryptoReceivedFromDeviceUserEventHandler();
        ((IncomingCryptoReceivedFromDeviceUserEventHandler) eventHandler).setIncomingDeviceUserManager(this);
        eventListener.setEventHandler(eventHandler);
        eventManager.addListener(eventListener);
        listenersAdded.add(eventListener);


        eventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_RECEPTION_CONFIRMED_FROM_DEVICE_USER);
        eventHandler = new IncomingCryptoReceptionConfirmedFromDeviceUserEventHandler();
        ((IncomingCryptoReceptionConfirmedFromDeviceUserEventHandler) eventHandler).setIncomingDeviceUserManager(this);
        eventListener.setEventHandler(eventHandler);
        eventManager.addListener(eventListener);
        listenersAdded.add(eventListener);

        eventListener = eventManager.getNewListener(EventType.INCOMING_CRYPTO_REVERSED_FROM_DEVICE_USER);
        eventHandler = new IncomingCryptoReversedFromDeviceUserEventHandler();
        ((IncomingCryptoReversedFromDeviceUserEventHandler) eventHandler).setIncomingDeviceUserManager(this);
        eventListener.setEventHandler(eventHandler);
        eventManager.addListener(eventListener);
        listenersAdded.add(eventListener);

        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void pause() {

        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {


        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {

        /**
         * I will remove all the event listeners registered with the event manager.
         */

        for (EventListener eventListener : listenersAdded) {
            eventManager.removeListener(eventListener);
        }

        listenersAdded.clear();
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }

    /**
     * IncomingDeviceUserManager interface implementation.
     */


    /**
     * UsesFileSystem Interface implementation.
     */
    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    /**
     * DealWithEvents Interface implementation.
     */

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     *DealWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {

    }

    /**
     * DealsWithPluginIdentity methods implementation.
     */

    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }



}
