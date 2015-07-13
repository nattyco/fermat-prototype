package com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.DealsWithEvents;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.DealsWithActorAddressBook;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.SpecialistNotRegisteredException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.DealsWithRegistry;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.interfaces.TransactionAgent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.SpecialistSelector;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantSelectSpecialistException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.exceptions.CantStartAgentException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.util.EventsLauncher;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by ciencias on 3/30/15.
 * Modified by Arturo Vallone 25/04/2015
 */


/**
 * Este es un proceso que toma las transacciones registradas en el registry en un estado pendiente de anunciar, 
 * las lee una por una y dispara el evento que corresponda en cada caso.
 *
 * Para cada transaccion, consulta el Address Book enviandole la direccion en la que se recibio la crypto.
 * El Address book devolvera el User al cual esa direccion fue entregada. De esta manera esta clase podra determinar
 * contra que tipo de usuario se esta ejecutando esta transaccion y a partir de ahi podra disparar el evento que 
 * corresponda para cada tipo de usuario.
 *
 * Al ser un Agent, la ejecucion de esta clase es en su propio Thread. Seguir el patron de diseño establecido para esto.
 * *
 * * * * * * * 
 *
 * * * * * * public
 */


public class IncomingCryptoRelayAgent implements DealsWithActorAddressBook , DealsWithErrors, DealsWithEvents, DealsWithRegistry , TransactionAgent {


    /**
     * DealsWithActorAddressBook Interface member variables.
     */
    private ActorAddressBookManager actorAddressBook;

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithEvents Interface member variables.
     */
    private EventManager eventManager;

    /**
     * DealsWithRegistry Interface member variables.
     */
    private IncomingCryptoRegistry registry;


    /**
     * TransactionAgent Member Variables.
     */
    private Thread agentThread;
    private RelayAgent relayAgent;

    /**
     * DealsWithActorAddressBook Interface implementation.
     */
    @Override
    public void setActorAddressBookManager(ActorAddressBookManager actorAddressBook) {
        this.actorAddressBook = actorAddressBook;
    }

    /**
     *DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }


    /**
     * DealWithEvents Interface implementation.
     */
    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    /**
     * DealWithRegistry Interface implementation.
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

        this.relayAgent = new RelayAgent ();
        try {
            this.relayAgent.setActorAddressBookManager(this.actorAddressBook);
            this.relayAgent.setErrorManager(this.errorManager);
            this.relayAgent.setEventManager(this.eventManager);
            this.relayAgent.setRegistry(this.registry);
            this.relayAgent.initialize();

            this.agentThread = new Thread(this.relayAgent);
            this.agentThread.start();
        }
        catch (Exception exception) {
            throw new CantStartAgentException("Agent failed to start",exception,"","");
        }

    }

    @Override
    public void stop() {

    }



    private static class RelayAgent implements DealsWithActorAddressBook , DealsWithErrors, DealsWithEvents, DealsWithRegistry , Runnable  {

        /**
         * DealsWithActorAddressBook Interface member variables.
         */
        private ActorAddressBookManager actorAddressBook;

        /**
         * DealsWithErrors Interface member variables.
         */
        private ErrorManager errorManager;

        /**
         * DealsWithEvents Interface member variables.
         */
        private EventManager eventManager;

        /**
         * DealsWithRegistry Interface member variables.
         */
        private IncomingCryptoRegistry registry;




        private SpecialistSelector specialistSelector;
        private EventsLauncher eventsLauncher;

        private static final int SLEEP_TIME = 5000;



        /**
         * DealsWithActorAddressBook Interface implementation.
         */
        @Override
        public void setActorAddressBookManager(ActorAddressBookManager actorAddressBook) {
            this.actorAddressBook = actorAddressBook;
        }

        /**
         *DealsWithErrors Interface implementation.
         */
        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }


        /**
         * DealWithEvents Interface implementation.
         */
        @Override
        public void setEventManager(EventManager eventManager) {
            this.eventManager = eventManager;
        }


        /**
         * DealWithRegistry Interface implementation.
         */
        @Override
        public void setRegistry(IncomingCryptoRegistry registry) {
            this.registry = registry;
        }


        /**
         * MonitorAgent interface implementation.
         */
        private void initialize () {
            this.eventsLauncher = new EventsLauncher();
            this.eventsLauncher.setEventManager(this.eventManager);

            this.specialistSelector = new SpecialistSelector();
            this.specialistSelector.setActorAddressBookManager(actorAddressBook);
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

            /*
            El RelayAgent del IncomingCrypto analizará las transacciones con estado (RESPONSIBLE,NO_ACTION_REQUIRED).
            */
            List<Transaction<CryptoTransaction>> responsibleTransactionList = null;
            try {
                responsibleTransactionList = this.registry.getResponsibleNARTransactions();
            } catch (InvalidParameterException e) {
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
                return;
            }

            if(responsibleTransactionList.isEmpty())
                return;

            System.out.println("TTF - INCOMING CRYPTO RELAY: " + responsibleTransactionList.size() + " TRANSACTION(s) DETECTED");

            // Por cada una de ellas haría los siguientes pasos en el orden enunciado:
            // Deduciría a partir de la información de las mismas su Specialist y lo marcaría.
            // Pasaría la transacción al estado (RESPONSIBLE,TO_BE_NOTIFIED)
            for (Transaction<CryptoTransaction> transaction : responsibleTransactionList) {
                try {
                    this.registry.setToNotify(transaction.getTransactionID(),
                            this.specialistSelector.getSpecialist(transaction.getInformation()));
                    System.out.println("TTF - INCOMING CRYPTO RELAY: SPECIALIST SETTED");
                } catch (CantSelectSpecialistException e) {
                    // TODO: MANAGE EXCEPTION
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }
            }

            /*
            Cuando termina de recorrer la lista recorre ahora todas las que están con TransactonStatus RESPONSIBLE y ProtocolStatus TO_BE_NOTIFIED o SENDING_NOTIFIED. Registra todos los especialistas que vio en este recoorido (no intentar optimizar usando el recorrido anterior porque puede perderse si el sistema se cae) y realiza los siguente pasos en el orden enunciado:
            Por cada Specialist registrado en el recorrido anterior lanza el evento correspondiente (IncomingCryptTransactionsWaitingTransferenceSpecalistEvent)
            */
            EnumSet<Specialist> specialistSet;
            try {
                specialistSet = this.registry.getSpecialists();
            } catch (InvalidParameterException e) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                System.out.println("TTF - INCOMING CRYPTO RELAY: GETSPECIALISTS FAILED");
                return;
            }

            System.out.println("TTF - INCOMING CRYPTO RELAY: SPECIALIST LIST CALCULATED");
            System.out.println("TTF - INCOMING CRYPTO RELAY: " + specialistSet.size() + " SPECIALIST(s) TO CALL");


            try {
                this.eventsLauncher.sendEvents(specialistSet);
            } catch (SpecialistNotRegisteredException e) {
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
                return;
            }

            System.out.println("TTF - INCOMING CRYPTO RELAY: SPECIALIST(s) INFORMED");


            //  Pasa cada transacción con ProtocolStatus TO_BE_NOTIFIED a SENDING_NOTIFED.
            this.registry.setToSendingNotified();
            //System.out.println("TTF - INCOMING CRYPTO RELAY: TRANSACTION(s) SETTED TO NOTIFIED");


            // Aquí termina su tarea, será el receptor de las transacciones quien las confirmará
            // al recibirlas

        }

        private void cleanResources() {

            /**
             * Disconnect from database and explicitly set all references to null.
             */

        }


    }

}
