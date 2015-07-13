package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure;

/**
 * Created by ciencias on 3/30/15.
 * Modified by Arturo Vallone 25/04/2015
 */


import com.bitdubai.fermat_api.FermatException;
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
import com.bitdubai.fermat_cry_api.layer.crypto_router.incoming_crypto.DealsWithIncomingCrypto;
import com.bitdubai.fermat_cry_api.layer.crypto_router.incoming_crypto.IncomingCryptoManager;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantAcknowledgeTransactionException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantReadEventException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantSaveEventException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.DealsWithRegistry;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.TransactionAgent;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantStartAgentException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util.SourceAdministrator;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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


public class IncomingExtraUserMonitorAgent implements DealsWithIncomingCrypto, DealsWithErrors, DealsWithRegistry, TransactionAgent {

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithIncomingCrypto Interface member variables.
     */
    private IncomingCryptoManager incomingCryptoManager;

    /**
     * DealsWithRegistry Interface member variables.
     */
    private IncomingExtraUserRegistry registry;


    /**
     * TransactionAgent Member Variables.
     */
    private Thread agentThread;
    private MonitorAgent monitorAgent;


    public IncomingExtraUserMonitorAgent(final ErrorManager errorManager, final IncomingCryptoManager incomingCryptoManager, final IncomingExtraUserRegistry registry){
        this.errorManager = errorManager;
        this.incomingCryptoManager = incomingCryptoManager;
        this.registry = registry;
    }

    /**
     *DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     *DealsWithIncomingCrypto Interface implementation.
     */
    @Override
    public void setIncomingCryptoManager(IncomingCryptoManager incomingCryptoManager) {
        this.incomingCryptoManager = incomingCryptoManager;
    }

    /**
     *DealsWithRegistry Interface implementation.
     */
    @Override
    public void setRegistry(IncomingExtraUserRegistry registry) {
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
            this.monitorAgent.setIncomingCryptoManager(this.incomingCryptoManager);
            this.monitorAgent.initialize();

            this.agentThread = new Thread(this.monitorAgent);
            this.agentThread.start();
        }
        catch (Exception exception) {
            throw new CantStartAgentException(CantStartAgentException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, "You should inspect the cause");
        }

    }

    @Override
    public void stop() {
        //this.agentThread.interrupt();
        this.monitorAgent.stop();
    }

    public boolean isRunning(){
        return this.monitorAgent.isRunning();
    }



    private static class MonitorAgent implements DealsWithIncomingCrypto, DealsWithErrors, DealsWithRegistry, Runnable  {

        private AtomicBoolean running = new AtomicBoolean(false);

        public boolean isRunning(){
            return running.get();
        }

        public void stop(){
            running.set(false);
        }
        /**
         * DealsWithCryptoVault Interface member variables.
         */
        private IncomingCryptoManager incomingCryptoManager;

        /**
         * DealsWithErrors Interface member variables.
         */
        private ErrorManager errorManager;


        /**
         * DealsWithRegistry Interface member variables.
         */
        private IncomingExtraUserRegistry registry;


        /*
         * MonitorAgent member variables
         */
        private SourceAdministrator sourceAdministrator;



        private static final int SLEEP_TIME = 5000;

        /**
         *DealsWithCryptoVault Interface implementation.
         */
        @Override
        public void setIncomingCryptoManager(IncomingCryptoManager incomungCryptoManager) {
            this.incomingCryptoManager = incomungCryptoManager;
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
        public void setRegistry(IncomingExtraUserRegistry registry){
            this.registry = registry;
        }

        /**
         * MonitorAgent methods.
         */
        private void initialize () {
            this.sourceAdministrator = new SourceAdministrator();
            this.sourceAdministrator.setIncomingCryptoManager(this.incomingCryptoManager);
        }

        /**
         * Runnable Interface implementation.
         */
        @Override
        public void run() {
            /**
             * Infinite loop.
             */
            running.set(true);

            while (running.get()) {
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
                    break;
                }
            }
            cleanResources();
        }

        private void doTheMainTask() {

            IncomingExtraUserRegistry.EventWrapper eventWrapper = null;
            try {
                eventWrapper = this.registry.getNextPendingEvent();
            } catch (CantReadEventException cantReadEvent) {
                // we can report the exception and try again in next call.
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantReadEvent);
                return;
            }
            if(eventWrapper != null){
                System.out.println("TTF - EXTRA USER MONITOR: NEW EVENT DETECTED");
                // We have here new pending transactions, we will check the source and ask for the right
                // TransactionSender

                TransactionProtocolManager<CryptoTransaction> source = null;
                try {
                    source = this.sourceAdministrator.getSourceAdministrator(EventSource.getByCode(eventWrapper.eventSource));
                } catch (InvalidParameterException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                }

                // Now we ask for the pending transactions
                try {
                    List<Transaction<CryptoTransaction>> transactionList = source.getPendingTransactions(Specialist.EXTRA_USER_SPECIALIST);
                    System.out.println("TTF - EXTRA USER MONITOR: " + transactionList.size() + " TRAMSACTION(s) DETECTED");
                    // Now we save the list in the registry
                    this.registry.acknowledgeTransactions(transactionList);
                    System.out.println("TTF - EXTRA USER MONITOR: " + transactionList.size() + " TRAMSACTION(s) ACKNOWLEDGED");
                } catch (CantDeliverPendingTransactionsException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    //if somethig wrong happenned we try in the next round
                    return;
                } catch (CantAcknowledgeTransactionException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    //if somethig wrong happenned we try in the next round
                    return;
                }

                // Now we take all the transactions in state (ACKNOWLEDGE,TO_BE_NOTIFIED)
                // Remember that this list can be more extensive than the one we saved, this is
                // because the system could have shut down in this step of the protocol making old
                // transactions to be stored but not precessed.
                List<Transaction<CryptoTransaction>> acknowledgedTransactions = this.registry.getAcknowledgedTransactions();


                // An finally, for each transaction we confirm it and then register responsibility.
                for(Transaction<CryptoTransaction> transaction : acknowledgedTransactions){
                    try {
                        source.confirmReception(transaction.getTransactionID());
                        System.out.println("TTF - EXTRA USER MONITOR: TRANSACTION RESPONSIBILITY ACQUIRED");
                        this.registry.acquireResponsibility(transaction);
                    } catch (CantConfirmTransactionException exception) {
                        // TODO: Consultar si esto hace lo que pienso, si falla no registra en base de datos
                        //       la transacción
                        // We will inform the exception and try again in the next round
                        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
                    }
                }
                // After finishing all the steps we mark the event as seen.
                try {
                    this.registry.disableEvent(eventWrapper.eventId);
                    System.out.println("TTF - EXTRA USER MONITOR: EVENT DISABLED");
                } catch (CantReadEventException | CantSaveEventException exception) { // There are two exceptions and we react in the same way to both
                    // We will inform the exception and try again in the next round
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
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
