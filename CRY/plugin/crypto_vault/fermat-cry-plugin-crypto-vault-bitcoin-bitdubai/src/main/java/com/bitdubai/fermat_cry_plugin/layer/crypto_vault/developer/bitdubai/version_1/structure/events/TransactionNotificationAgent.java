package com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.layer.all_definition.developer.DealsWithLogManagers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.event.DealWithEventMonitor;
import com.bitdubai.fermat_api.layer.all_definition.event.EventMonitor;
import com.bitdubai.fermat_api.layer.all_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer.dmp_world.Agent;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantInitializeMonitorAgentException;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantStartAgentException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
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
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventSource;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.events.IncomingCryptoTransactionsWaitingTransferenceEvent;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultTransactionNotificationAgent;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.LimitReachedTransactionNotificationAgentException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.BitcoinCryptoVaultPluginRoot;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.TransactionProtocolAgentMaxIterationsReachedException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure.BitcoinCryptoVault;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure.CryptoVaultDatabaseActions;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure.CryptoVaultDatabaseFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rodrigo on 2015.06.18..
 */
public class TransactionNotificationAgent implements Agent,DealsWithLogger,DealsWithEvents,DealsWithErrors, DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {

        /**
     * TransactionNotificationAgent variables
     */
    Database database;
    UUID walletId;

    /**
     * Agent interface member variables
     */
    Thread agentThread;
    MonitorAgent monitorAgent;

    /**
     * DealWithLogger interface member variable
     */
    LogManager logManager;



    /**
     * DealsWithEvents interface member variables
     */
    EventManager eventManager;

    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;

    /**
     * DealsWithPluginDatabaseSystem interface member variable
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginIdentity interfaz member variable
     */
    UUID pluginId;


    /**
     * Constructor
     */
    public TransactionNotificationAgent(EventManager eventManager, PluginDatabaseSystem pluginDatabaseSystem, ErrorManager errorManager, UUID pluginId, UUID walletId){
        this.eventManager = eventManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
        this.pluginId = pluginId;
        this.walletId = walletId;
    }

    /**
     * Agent interface implementation
     * @throws CantStartAgentException
     */
    @Override
    public void start() throws CantStartAgentException {
        /**
         * I initialize the monitor agent private class that will run in a new thread
         */
        monitorAgent = new MonitorAgent();

        ((DealsWithPluginDatabaseSystem) this.monitorAgent).setPluginDatabaseSystem(this.pluginDatabaseSystem);
        ((DealsWithErrors) this.monitorAgent).setErrorManager(this.errorManager);

        /**
         * I will create or load the DB if it already exists.
         */
        try {
            ((MonitorAgent) this.monitorAgent).Initialize();
        }
        catch (CantInitializeMonitorAgentException cantInitializeCryptoRegistryException) {
            /**
             * I cant continue if this happens.
             */
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantInitializeCryptoRegistryException);
        }

        /**
         * I start the thread that will get from the DB the confirmation
         * that there are or not pending transactions to notify
         */
        this.agentThread = new Thread(monitorAgent);
        this.agentThread.start();
    }

    @Override
    public void stop() {
        this.agentThread.interrupt();
    }

    /**
     * DealsWithEvents interface implementation
     */
    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interfaz implementation
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * DealsWithErrors interface implementation
     */
     @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPlugInIdentity interface implementation
     * @param pluginId
     */
    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * Private class which implements runnable and is started by the Agent
     */
    private class MonitorAgent implements CryptoVaultTransactionNotificationAgent, DealsWithPluginDatabaseSystem, DealsWithErrors, Runnable{

        /**
         * how often I will search for transactions to notify
         */
        public final int SLEEP_TIME = CryptoVaultTransactionNotificationAgent.AGENT_SLEEP_TIME;

        /**
         * PluginDatabaseSystem interfaz member variables
         */
        PluginDatabaseSystem pluginDatabaseSystem;

        /**
         * DealsWithErrors interfaz member variables
         */
        ErrorManager errorManager;

        /**
         * DealsWithErrors interface implementation
         * @param errorManager
         */
        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
        }

        public void Initialize() throws CantInitializeMonitorAgentException {
            /**
             * I open the database
             */
            try {

                database = this.pluginDatabaseSystem.openDatabase(pluginId, walletId.toString());
            }
            catch (DatabaseNotFoundException databaseNotFoundException) {
                /**
                 * if the database doesnt exists, I will create it.
                 */
                CryptoVaultDatabaseFactory databaseFactory = new CryptoVaultDatabaseFactory();
                databaseFactory.setPluginDatabaseSystem(this.pluginDatabaseSystem);

                /**
                 * I will create the database where I am going to store the information of this wallet.
                 */
                try {

                    database =  databaseFactory.createDatabase(pluginId, walletId.toString());

                }
                catch (CantCreateDatabaseException cantCreateDatabaseException){

                    /**
                     * The database cannot be created. I can not handle this situation.
                     */
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantCreateDatabaseException);
                    throw new CantInitializeMonitorAgentException();

                }
            }
            catch (CantOpenDatabaseException cantOpenDatabaseException){

                /**
                 * The database exists but cannot be open. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
                throw new CantInitializeMonitorAgentException();
            }

        }

        @Override
        public void run() {
            /**
             * this will run in an infinite loop
             */
            //logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass("com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure.events.TransactionNotificationAgent"), "Transaction Protocol Notification Agent: running...", "Transaction Protocol Notification Agent: running...", "Transaction Protocol Notification Agent: running...");
            int iteration = 0;
            while (true)
            {
                /**
                 * Increase the iteration counter
                 */
                iteration++;
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    return;
                }

                /**
                 * now I will check if there are pending transactions to raise the event
                 */
                try {

                    //logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), null, "Iteration number " + iteration, "Iteration number " + iteration);
                    doTheMainTask();
                } catch (CantExecuteQueryException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
               } catch (FermatException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }
            }

        }
        /**
         * Implements the agent
         */
        private void doTheMainTask() throws CantExecuteQueryException, TransactionProtocolAgentMaxIterationsReachedException {
            /**
             * I search for transactions not yet notified. If I found something, Ill raise an event
             */
            CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);

            if (isTransactionToBeNotified()){
                PlatformEvent event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_TRANSACTIONS_WAITING_TRANSFERENCE);
                event.setSource(EventSource.CRYPTO_VAULT);

                //logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), null, "Found transactions pending to be notified! Raising INCOMING_CRYPTO_TRANSACTIONS_WAITING_TRANSFERENCE event.","Found transactions pending to be notified! Raising INCOMING_CRYPTO_TRANSACTIONS_WAITING_TRANSFERENCE event.");
                eventManager.raiseEvent(event);

                /**
                 * I need to increase the counter of the iterations. If the value excedes the threashold, then there might be
                 * an error in the platform, so I will raise an error.
                 */
                int iterations = db.updateTransactionProtocolStatus(true);

                //logManager.log(BitcoinCryptoVaultPluginRoot.getLogLevelByClass(this.getClass().getName()), "No other plugin is consuming Vault transactions.", "Transaction Protocol Notification Agent: iteration number " + iterations + " without other plugins consuming transaction.","Transaction Protocol Notification Agent: iteration number " + iterations + " without other plugins consuming transaction.");
                if (ITERATIONS_THRESHOLD < iterations){
                    throw new TransactionProtocolAgentMaxIterationsReachedException("The max limit configured for the Transaction Protocol Agent has been reached.", null,"Iteration Limit: " + ITERATIONS_THRESHOLD, "Notify developer.");
                }

            } else
            {
                /**
                 * there are no transactions pending. I will reset the counter to 0.
                 */
                db.updateTransactionProtocolStatus(false);
            }

        }


        private boolean isTransactionToBeNotified() throws CantExecuteQueryException {
            CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);
            boolean isPending =db.isPendingTransactions();
           return isPending;

        }
    }
}
