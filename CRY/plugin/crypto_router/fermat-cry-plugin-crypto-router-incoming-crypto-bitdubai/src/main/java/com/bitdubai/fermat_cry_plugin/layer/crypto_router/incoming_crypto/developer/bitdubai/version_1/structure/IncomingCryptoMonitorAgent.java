package com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure;

/**
 * Created by ciencias on 3/30/15.
 * Modified by Arturo Vallone 25/04/2015
 */


import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionProtocolManager;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventSource;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantIdentifyEventSourceException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantReadEvent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.DealsWithRegistry;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.TransactionAgent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantStartAgentException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.SourceAdministrator;

import java.util.List;

/**
 * Este agente corre en su propio Thread.
 *
 * Se despierta cada unos segundo a ver si se han registrado eventos de incoming crypto.
 *
 * Si se han registrado, entonces se activa y procede a ir a buscar al plugin que corresponda la transaccion entrante.
 *
 * Si no se han registrado, igual cada cierto tiempo va y verifica contra la lista de plugins que pueden recibir incoming crypto.
 *
 * Cuando hace la verificacion contra un plugin, registra la transaccion en su base de datos propia y le confirma al plugin la recepcion.
 *
 *
 * * * * * * * * * * * * * * * * * * * * * * * 
 */


public class IncomingCryptoMonitorAgent implements DealsWithCryptoVault , DealsWithErrors, DealsWithRegistry, TransactionAgent {


    /**
     * DealsWithCryptoVault Interface member variables.
     */
    private CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;


    /**
     * DealsWithRegistry Interface member variables.
     */
    private IncomingCryptoRegistry registry;


    /**
     * TransactionAgent Member Variables.
     */
    private Thread agentThread;
    private MonitorAgent monitorAgent;



    /**
     *DealsWithCryptoVault Interface implementation.
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
     *DealsWithRegistry Interface implementation.
     */
    @Override
    public void setRegistry(IncomingCryptoRegistry registry) {
        this.registry = registry;
    }


    /**
     * TransactionAgent Interface implementation.
     */
    @Override
    public void start() throws CantStartAgentException {



        this.monitorAgent = new MonitorAgent ();
        try {
            this.monitorAgent.setErrorManager(this.errorManager);
            this.monitorAgent.setRegistry(this.registry);
            this.monitorAgent.setCryptoVaultManager(this.cryptoVaultManager);
            this.monitorAgent.initialize();

            this.agentThread = new Thread(this.monitorAgent);
            this.agentThread.start();
        }
        catch (Exception exception) {
            throw new CantStartAgentException("Agent failed to start",exception,"","");
        }

    }

    @Override
    public void stop() {
        
        this.agentThread.interrupt();
        
    }




    private static class MonitorAgent implements DealsWithCryptoVault, DealsWithErrors, DealsWithRegistry, Runnable  {

        /**
         * DealsWithCryptoVault Interface member variables.
         */
        private CryptoVaultManager cryptoVaultManager;

        /**
         * DealsWithErrors Interface member variables.
         */
        private ErrorManager errorManager;


        /**
         * DealsWithRegistry Interface member variables.
         */
        private IncomingCryptoRegistry registry;


        /*
         * MonitorAgent member variables
         */
        private SourceAdministrator sourceAdministrator;



        private static final int SLEEP_TIME = 5000;

        /**
         *DealsWithCryptoVault Interface implementation.
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
         *DealsWithRegistry Interface implementation.
         */
        @Override
        public void setRegistry(IncomingCryptoRegistry registry){
            this.registry = registry;
        }

        /**
         * MonitorAgent methods.
         */
        private void initialize () {
            this.sourceAdministrator = new SourceAdministrator();
            this.sourceAdministrator.setCryptoVaultManager(this.cryptoVaultManager);
        }

        /**
         * Runnable Interface implementation.
         */
        @Override
        public void run() {

            /**
             * Infinite loop.
             */
            while (true) {

                /**
                 * Sleep for a while.
                 */
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    cleanResources();
                    return;
                }

                /**
                 * Now I do the main task.
                 */
                doTheMainTask();
            
                /**
                 * Check if I have been Interrupted.
                 */
                if (Thread.currentThread().isInterrupted()) {
                    cleanResources();
                    return;
                }
            }
        }

        private void doTheMainTask() {

            // TODO: delete this. Every tune registry is initialized we add a new transaction
            // this.registry.proofTransaction();

            IncomingCryptoRegistry.EventWrapper eventWrapper = null;
            try {
                eventWrapper = this.registry.getNextPendingEvent();
            } catch (CantReadEvent cantReadEvent) {
                // we can report the exception and try again in next call.
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantReadEvent);
                return;
            }
            if(eventWrapper != null){
                System.out.println("TTF - INCOMING CRYPTO MONITOR: NEW EVENT READ");

                // We have here new pending transactions, we will check the source and ask for the right
                // TransactionSender

                TransactionProtocolManager<CryptoTransaction> source = null;
                try {
                    source = this.sourceAdministrator.getSourceAdministrator(EventSource.getByCode(eventWrapper.eventSource));
                } catch (InvalidParameterException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                } catch (CantIdentifyEventSourceException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                }

                System.out.println("TTF - INCOMING CRYPTO MONITOR: Source Identified");

                // Now we ask for the pending transactions
                List<Transaction<CryptoTransaction>> transactionList = null;

                try {
                    transactionList = source.getPendingTransactions(Specialist.CRYPTO_ROUTER_SPECIALIST);
                } catch (CantDeliverPendingTransactionsException e) {
                    System.out.println("TTF - INCOMING CRYPTO MONITOR: cryptoVault raised CantDeliverPendingTransactionsException");
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    //if somethig wrong happenned we try in the next round
                    return;
                }

                // Now we save the list in the registry
                if(transactionList != null){
                    this.registry.acknowledgeTransactions(transactionList);
                    System.out.println("TTF - INCOMING CRYPTO MONITOR: " + transactionList.size() +" TRANSACTION(S) ACKNOWLEDGE");

                } else {
                  // if sombething failed we try in next round
                    //errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, );
                    return;
                }

                // Now we take all the transactions in state (ACKNOWLEDGE,TO_BE_NOTIFIED)
                // Remember that this list can be more extensive than the one we saved, this is
                // because the system could have shut down in this step of the protocol making old
                // transactions to be stored but not precessed.
                List<Transaction<CryptoTransaction>> acknowledgedTransactions = null;
                try {
                    acknowledgedTransactions = this.registry.getAcknowledgedTransactions();
                } catch (InvalidParameterException e) {
                    this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
                    return;
                }


                // An finally, for each transaction we confirm it and then register responsibility.
                for(Transaction<CryptoTransaction> transaction : acknowledgedTransactions){
                    try {
                        source.confirmReception(transaction.getTransactionID());
                        System.out.println("TTF - INCOMING CRYPTO MONITOR: RESPONSIBILITY ACQUIRED");
                        this.registry.acquireResponsibility(transaction);
                    } catch (CantConfirmTransactionException e) {
                        // TODO: Consultar si esto hace lo que pienso, si falla no registra en base de datos
                        //       la transacción
                        // We will inform the exception and try again in the next round
                        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    }
                }
                // After finishing all the steps we mark the event as seen.
                try {
                    this.registry.disableEvent(eventWrapper.eventId);
                    System.out.println("TTF - INCOMING CRYPTO MONITR: EVENT DISABLED");

                } catch (Exception e) { // There are two exceptions and we react in the same way to both
                    // We will inform the exception and try again in the next round
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }
            }
        }

        
        private void cleanResources() {
            
            /**
             * Disconnect from database and explicitly set all references to null.
             */
            
        }
    }
}
