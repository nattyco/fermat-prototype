package com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionProtocolManager;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
//import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.ActorAddressBookManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.DealsWithEvents;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.DealsWithActorAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_router.incoming_crypto.IncomingCryptoManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.developerUtils.IncomingCryptoDeveloperDatabaseFactory;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantDeliverDatabaseException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantInitializeCryptoRegistryException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantStartAgentException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantStartServiceException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.DealsWithRegistry;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.TransactionAgent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.TransactionService;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoDataBaseConstants;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoEventRecorderService;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoMonitorAgent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoRegistry;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoRelayAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by loui on 18/03/15.
 * Modified by Arturo Vallone 25/04/2015
 */
public class IncomingCryptoTransactionPluginRoot implements IncomingCryptoManager,DatabaseManagerForDevelopers ,DealsWithActorAddressBook,DealsWithCryptoVault,DealsWithErrors, DealsWithEvents, DealsWithLogger, LogManagerForDevelopers, DealsWithPluginDatabaseSystem, Plugin, Service {

    /*
     * DealsWithActorAddressBook member variables
     */
    private ActorAddressBookManager actorAddressBook;

    /*
     * DealsWithCryptoVault member variables
     */
    private CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithEvents Interface member variables.
     */
    private EventManager eventManager;

    /**
     * DealsWithLogger inteface member variables
     */
    LogManager logManager;
    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;


    /**
     * IncomingCryptoManager Interface member variables.
     */
    private IncomingCryptoRegistry registry;

    /**
     * Plugin Interface member variables.
     */
    private UUID pluginId;

    /**
     * Service Interface member variables.
     */
    private ServiceStatus serviceStatus = ServiceStatus.CREATED;
    private TransactionAgent monitor;
    private TransactionAgent relay;
    private TransactionService eventRecorder;

    /*
     * DatabaseManagerForDevelopers interface implementation
     */
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        IncomingCryptoDeveloperDatabaseFactory dbFactory = new IncomingCryptoDeveloperDatabaseFactory(pluginId.toString(), IncomingCryptoDataBaseConstants.INCOMING_CRYPTO_DATABASE);
        return dbFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return IncomingCryptoDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        Database database;
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, IncomingCryptoDataBaseConstants.INCOMING_CRYPTO_DATABASE);
            return IncomingCryptoDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, database, developerDatabaseTable);
        }catch (CantOpenDatabaseException cantOpenDatabaseException){
                /**
                 * The database exists but cannot be open. I can not handle this situation.
                 */
                FermatException e = new CantDeliverDatabaseException("I can't open database",cantOpenDatabaseException,"WalletId: " + developerDatabase.getName(),"");
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            }
            catch (DatabaseNotFoundException databaseNotFoundException) {
                FermatException e = new CantDeliverDatabaseException("Database does not exists",databaseNotFoundException,"WalletId: " + developerDatabase.getName(),"");
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            }
            // If we are here the database could not be opened, so we return an empry list
            return new ArrayList<>();
    }



    /*
     * DealsWithActorAddressBook Interface implementation.
     */
    @Override
    public void setActorAddressBookManager(ActorAddressBookManager actorAddressBook) {
        this.actorAddressBook = actorAddressBook;
    }


    /**
     * DealsWithCryptoVault Interface implementation.
     */
    @Override
    public void setCryptoVaultManager(CryptoVaultManager cryptoVaultManager) {
        this.cryptoVaultManager = cryptoVaultManager;
    }

    /**
     *DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }


    /**
     * DealsWithLogger interface implementation
     */
    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * DealsWithLogger interface implementation
     */
    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();

        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.SourceAdministrator");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.EventsLauncher");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.SpecialistSelector");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.SourceAdministrator");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.developerUtils.IncomingCryptoDeveloperDatabaseFactory");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.IncomingCryptoTransactionPluginRoot");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoRegistry");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoEventRecorderService");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoSampleAgent");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoRelayAgent");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoDataBaseConstants");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoDataBaseFactory");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure.IncomingCryptoMonitorAgent");
        returnedClasses.add("com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.event_handlers.IncomingCryptoTransactionsWaitingTransferenceEventHandler");
        /**
         * I return the values.
         */
        return returnedClasses;
    }

    /**
     * DealsWithLogger interface implementation
     */
    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /**
         * I will check the current values and update the LogLevel in those which is different
         */

        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
            /**
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (IncomingCryptoTransactionPluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                IncomingCryptoTransactionPluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                IncomingCryptoTransactionPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                IncomingCryptoTransactionPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    /**
     * Static method to get the logging level from any class under root.
     * @param className
     * @return
     */
    public static LogLevel getLogLevelByClass(String className){
        try{
            /**
             * sometimes the classname may be passed dinamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split((Pattern.quote("$")));
            return IncomingCryptoTransactionPluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct loggin level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }

    /**
     * DealWithEvents Interface implementation.
     */
    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * IncomingCryptoManager interface implementation.
     */
    @Override
    public TransactionProtocolManager<CryptoTransaction> getTransactionManager(){
        return this.registry;
    }

    /**
     * Plugin interface implementation.
     */
    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }


    /**
     * Service Interface implementation.
     */
    @Override
    public void start()  throws CantStartPluginException {


        /**
         * I will initialize the Registry, which in turn will create the database if necessary.
         */
        this.registry = new IncomingCryptoRegistry();

        try {
            this.registry.setErrorManager(this.errorManager);
            this.registry.setPluginDatabaseSystem(this.pluginDatabaseSystem);
            this.registry.initialize(this.pluginId);
        }
        catch (CantInitializeCryptoRegistryException cantInitializeCryptoRegistryException) {

            /**
             * If I can not initialize the Registry then I can not start the service.
             */
            this.serviceStatus = ServiceStatus.STARTED;
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantInitializeCryptoRegistryException);
            throw new CantStartPluginException("Registry failed to start",cantInitializeCryptoRegistryException, Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION.getKey(),"");
        }

        /**
         * I will start the Event Recorder.
         */
        this.eventRecorder = new IncomingCryptoEventRecorderService();
        ((DealsWithEvents) this.eventRecorder).setEventManager(this.eventManager);
        ((DealsWithRegistry) this.eventRecorder).setRegistry(this.registry);

        try {
            this.eventRecorder.start();
        }
        catch (CantStartServiceException cantStartServiceException) {
           
            /**
             * I cant continue if this happens.
             */
            this.serviceStatus = ServiceStatus.STARTED;
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantStartServiceException);
            throw new CantStartPluginException("Event Recorded could not be started",cantStartServiceException, Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION.getKey(),"");
        }

        /**
         * I will start the Relay Agent.
         */
        this.relay = new IncomingCryptoRelayAgent();

        try {
            ((DealsWithActorAddressBook) this.relay).setActorAddressBookManager(this.actorAddressBook);
            ((DealsWithErrors) this.relay).setErrorManager(this.errorManager);
            ((DealsWithEvents) this.relay).setEventManager(this.eventManager);
            ((DealsWithRegistry) this.relay).setRegistry(this.registry);
            this.relay.start();
        }
        catch (CantStartAgentException cantStartAgentException) {

            /**
             * Note that I stop previously started services and agents.
             */
            this.eventRecorder.stop();

            /**
             * I cant continue if this happens.
             */
            this.serviceStatus = ServiceStatus.STARTED;
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantStartAgentException);

            throw new CantStartPluginException("Relay Agent could not be started",cantStartAgentException, Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION.getKey(),"");
        }

        /**
         * I will start the Monitor Agent.
         */
        this.monitor = new IncomingCryptoMonitorAgent();
        try {
            ((DealsWithCryptoVault) this.monitor).setCryptoVaultManager(this.cryptoVaultManager);
            ((DealsWithErrors) this.monitor).setErrorManager(this.errorManager);
            ((DealsWithRegistry) this.monitor).setRegistry(this.registry);
            this.monitor.start();
        }
        catch (CantStartAgentException cantStartAgentException) {

            /**
             * Note that I stop previously started services and agents.
             */
            this.eventRecorder.stop();
            this.relay.stop();

            /**
             * I cant continue if this happens.
             */
            this.serviceStatus = ServiceStatus.STARTED;
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantStartAgentException);

            throw new CantStartPluginException("Monitor agent could not be started",cantStartAgentException, Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION.getKey(),"");
        }
        
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

        this.eventRecorder.stop();
        this.relay.stop();
        this.monitor.stop();
        
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return this.serviceStatus;
    }

}
