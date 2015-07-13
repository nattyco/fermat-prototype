package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Action;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;

import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.osa_android.database_system.*;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.*;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantAcknowledgeTransactionException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantInitializeCryptoRegistryException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantReadEventException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantSaveEventException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.ExpectedTransactionNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ciencias on 3/30/15.
 * Modified by Arturo Vallone 25/04/2015
 */

/**
 * Esta clase maneja una tabla en su base de datos con la siguiente estructura:
 *
 * Tabla: IncomingExtraUserRegistry
 *
 * Campos:
 *
 * Id: Transaction ID
 * AddressTo
 * CryptoCurrency
 * CryptoAmount
 * AdressFrom
 * ReceptorType (call method getName() to ReceptorType enum member)
 * CryptoStatus (call method getName() to CryptoStatus enum member) (Identified, Received, Confirmed, Reversed)
 * NotificationStatus (call method getName() to NotificationStatus enum member) (NO_ACTION_REQUIRED, ETC)
 * TransactionStatus (call method getName() to TransactionStatus enum member) (PENDING_TRANSFER, IN_PROGRESS, PENDING_NOTIFICATION, NOTIFICATED, FINALIZED)
 * Timestamp
 *
 * La clase basicamente maneja consultas a su tabla IncomingExtraUserRegistry.
 *
 *
 * Tabla: EventsRecorded
 *
 * Campos;
 *
 * Id
 * Event  (enum EventType.name())
 * Source (enum EventSource.name())
 * Status
 * Timestamp
 *
 * Nota: Definir los status de cada tabla en la capa numero 1 - Definiciones
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

public class IncomingExtraUserRegistry implements DealsWithErrors, DealsWithPluginDatabaseSystem {

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;


    /**
     * IncomingExtraUserRegistry member variables.
     */
    private Database database;

    /**
     * DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * IncomingExtraUserRegistry member methods.
     */
    public void initialize(UUID pluginId) throws CantInitializeCryptoRegistryException {
        if(pluginDatabaseSystem == null)
            throw new CantInitializeCryptoRegistryException(CantInitializeCryptoRegistryException.DEFAULT_MESSAGE, null, "Plugin Database System: null", "You have to set the PluginDatabaseSystem before initializing");

        try {
            this.database = this.pluginDatabaseSystem.openDatabase(pluginId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE);
        } catch (DatabaseNotFoundException e) {
            IncomingExtraUserDataBaseFactory databaseFactory = new IncomingExtraUserDataBaseFactory();
            databaseFactory.setPluginDatabaseSystem(this.pluginDatabaseSystem);

            try {
                this.database = databaseFactory.createDatabase(pluginId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE);
            } catch (CantCreateDatabaseException exception) {
                throw new CantInitializeCryptoRegistryException(CantInitializeCryptoRegistryException.DEFAULT_MESSAGE, exception, "", "Check the cause to see why the Database couldn't be created");
            }
        } catch (CantOpenDatabaseException exception) {
            throw new CantInitializeCryptoRegistryException(CantInitializeCryptoRegistryException.DEFAULT_MESSAGE, exception, "", "Check the cause to see why we couldn't open the database");
        }
    }

    // Used by the Monitor Agent
    // Las coloca en (A,TBN)
    public void acknowledgeTransactions(List<Transaction<CryptoTransaction>> transactionList) throws CantAcknowledgeTransactionException{ // throws CantAcknowledgeTransactionException
        DatabaseTable registryTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_NAME);
        //DatabaseTransaction databaseTransaction = this.database.newTransaction();
        for(Transaction<CryptoTransaction> transaction : transactionList) {
            // We first check if we have this transaction registered as (A,TBN). This would not be
            // a mistake. It just mean that the system shut down before we could confirm reception to
            // the sender and the sender sent it again.
            registryTable.setUUIDFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ID_COLUMN.columnName, transaction.getTransactionID(), DatabaseFilterType.EQUAL);

            try {
                registryTable.loadToMemory();
            } catch (CantLoadTableToMemoryException exception) {
                //TODO: MANAGE EXCEPTION
                throw new CantAcknowledgeTransactionException(CantDeliverPendingTransactionsException.DEFAULT_MESSAGE, exception, null, "We cant load the table to memory, there is no way we can circunvent this problem");
            }

            List<DatabaseTableRecord> records = registryTable.getRecords();
            if (records.isEmpty()) {
                // if it is empty this is a new transaction
                // If it is a new transaction we save it as usual
                DatabaseTableRecord transactionRecord = registryTable.getEmptyRecord();
                fillRegistryTableRecord(transactionRecord, transaction, TransactionStatus.ACKNOWLEDGED, ProtocolStatus.TO_BE_NOTIFIED);
                try {
                    registryTable.insertRecord(transactionRecord);
                } catch (CantInsertRecordException cantInsertRecord) {
                    // TODO: MANAGE EXCEPTION.
                    throw new CantAcknowledgeTransactionException(CantAcknowledgeTransactionException.DEFAULT_MESSAGE, cantInsertRecord, null, "This is a database level issue, check the cause to see the reason");
                }
            }

            // if it is not empty we ignore the transaction because we already have it.

            registryTable.clearAllFilters();
        }
    }

    protected void saveNewEvent(String eventType, String eventSource) throws CantSaveEventException {
        try {
            DatabaseTransaction dbTrx = this.database.newTransaction();
            DatabaseTable eventsTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_NAME);
            DatabaseTableRecord eventRecord = eventsTable.getEmptyRecord();

            UUID eventRecordID = UUID.randomUUID();
            long unixTime = System.currentTimeMillis();
            eventRecord.setUUIDValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_ID_COLUMN.columnName, eventRecordID);
            eventRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_EVENT_COLUMN.columnName, eventType);
            eventRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_SOURCE_COLUMN.columnName, eventSource);
            eventRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_STATUS_COLUMN.columnName, "PENDING");
            eventRecord.setLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_TIMESTAMP_COLUMN.columnName, unixTime);
            dbTrx.addRecordToInsert(eventsTable, eventRecord);
            this.database.executeTransaction(dbTrx);
        } catch (DatabaseTransactionFailedException databaseTransactionFailedException) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, databaseTransactionFailedException);
            throw new CantSaveEventException();
        }
    }

    protected EventWrapper getNextPendingEvent() throws CantReadEventException {
        try {
            DatabaseTable eventsTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_NAME);
            eventsTable.setStringFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_STATUS_COLUMN.columnName, "PENDING", DatabaseFilterType.EQUAL);
            try {
                eventsTable.loadToMemory();
            } catch (CantLoadTableToMemoryException exception) {
                throw new CantReadEventException(CantReadEventException.DEFAULT_MESSAGE, exception, null, "There is no way to gracefully handle this, check the cause");
            }

            List<DatabaseTableRecord> events = eventsTable.getRecords();

            if (events != null && !events.isEmpty()) {
                DatabaseTableRecord event = events.get(0);
                return new EventWrapper(
                        event.getUUIDValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_ID_COLUMN.columnName),
                        event.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_EVENT_COLUMN.columnName),
                        event.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_SOURCE_COLUMN.columnName),
                        event.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_STATUS_COLUMN.columnName),
                        event.getLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_TIMESTAMP_COLUMN.columnName)
                );
            }
            return null;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantReadEventException();
        }
    }

    protected void disableEvent(UUID eventId) throws CantReadEventException, CantSaveEventException {
        try {
            DatabaseTransaction dbTrx = this.database.newTransaction();
            DatabaseTable eventsTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_NAME);
            eventsTable.setUUIDFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_ID_COLUMN.columnName, eventId, DatabaseFilterType.EQUAL);
            try {
                eventsTable.loadToMemory();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
                throw new CantReadEventException();
            }
            List<DatabaseTableRecord> records = eventsTable.getRecords();
            if (records == null || records.isEmpty()) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new Exception(String.format("I could not find the event with Id: %s", eventId)));
                throw new CantReadEventException();
            } else if (records.size() > 1) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new Exception(String.format("More than one event with Id: %s", eventId)));
                throw new CantSaveEventException();
            }

            DatabaseTableRecord eventRecord = records.get(0);
            eventRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_STATUS_COLUMN.columnName, "DISABLED");
            dbTrx.addRecordToUpdate(eventsTable, eventRecord);
            this.database.executeTransaction(dbTrx);
        } catch (DatabaseTransactionFailedException databaseTransactionFailedException) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, databaseTransactionFailedException);
            throw new CantSaveEventException();
        }
    }

    // Retorna las que están en (A,TBN)
    protected  List<Transaction<CryptoTransaction>> getAcknowledgedTransactions() {//throws CantGetTransactionsException

        DatabaseTable registryTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_NAME);
        registryTable.setStringFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_STATUS_COLUMN.columnName ,
                                      TransactionStatus.ACKNOWLEDGED.getCode(),
                                      DatabaseFilterType.EQUAL);
        registryTable.setStringFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN.columnName,
                ProtocolStatus.TO_BE_NOTIFIED.getCode(),
                DatabaseFilterType.EQUAL);

        List<Transaction<CryptoTransaction>> tbaList = new ArrayList<>();
        try {
            registryTable.loadToMemory();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
            //TODO: MANAGE EXCEPTION
            return tbaList;
        }

        List<DatabaseTableRecord> records = registryTable.getRecords();

        for(DatabaseTableRecord r : records)
            tbaList.add(getTransactionFromRecord(r));

        registryTable.clearAllFilters();

        return tbaList;
    }

    // Pasa una a (R,TBA)
    protected void acquireResponsibility(Transaction<CryptoTransaction> transaction) { // throws CantAcquireResponsibility
        DatabaseTable registryTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_NAME);

        // We look for the record to update
        registryTable.setUUIDFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ID_COLUMN.columnName, transaction.getTransactionID(), DatabaseFilterType.EQUAL);

        try {
            registryTable.loadToMemory();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
            //TODO: MANAGE EXCEPTION
        }

        List<DatabaseTableRecord> records = registryTable.getRecords();
        if (records.size() != 1) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new ExpectedTransactionNotFoundException());
            //TODO: MANAGE EXCEPTION
        } else {
            DatabaseTableRecord recordToUpdate = records.get(0);
            recordToUpdate.setStringValue(
                    IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_STATUS_COLUMN.columnName,
                    TransactionStatus.RESPONSIBLE.getCode()
                                         );

            recordToUpdate.setStringValue(
                    IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN.columnName,
                    ProtocolStatus.TO_BE_APPLIED.getCode()
                                         );
            try {
                registryTable.updateRecord(recordToUpdate);
            } catch (CantUpdateRecordException cantUpdateRecord) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantUpdateRecord);
                //TODO: MANAGE EXCEPTION
            }
        }

    }


    // Used by Relay Agent
    // Retorna las (R,TBA)
    protected List<Transaction<CryptoTransaction>> getResponsibleTBATransactions() { //throws CantAccessTransactionsException
        return getAllTransactionsInState(TransactionStatus.RESPONSIBLE,ProtocolStatus.TO_BE_APPLIED);
    }

    // Pasa la transacción a APPLIED.
    protected void setToApplied(UUID id) {
        DatabaseTable registryTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_NAME);

        // We look for the record to update
        registryTable.setUUIDFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ID_COLUMN.columnName, id, DatabaseFilterType.EQUAL);

        try {
            registryTable.loadToMemory();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
            //TODO: MANAGE EXCEPTION
        }

        List<DatabaseTableRecord> records = registryTable.getRecords();

        if (records.size() != 1) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, new ExpectedTransactionNotFoundException());
            //TODO: MANAGE EXCEPTION
        } else {
            DatabaseTableRecord recordToUpdate = records.get(0);

            recordToUpdate.setStringValue(
                    IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN.columnName,
                    ProtocolStatus.APPLIED.getCode()
            );

            try {
                registryTable.updateRecord(recordToUpdate);
            } catch (CantUpdateRecordException cantUpdateRecord) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantUpdateRecord);
                //TODO: MANAGE EXCEPTION
            }
        }
    }

    private void fillRegistryTableRecord(DatabaseTableRecord databaseTableRecord,
                                         Transaction<CryptoTransaction> transaction,
                                         TransactionStatus transactionStatus,
                                         ProtocolStatus protocolStatus){

        databaseTableRecord.setUUIDValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ID_COLUMN.columnName, transaction.getTransactionID());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_HASH_COLUMN.columnName, transaction.getInformation().getTransactionHash());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ADDRESS_FROM_COLUMN.columnName, transaction.getInformation().getAddressFrom().getAddress());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ADDRESS_TO_COLUMN.columnName, transaction.getInformation().getAddressTo().getAddress());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_CURRENCY_COLUMN.columnName, String.valueOf(transaction.getInformation().getCryptoCurrency()));
        databaseTableRecord.setLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_AMOUNT_COLUMN.columnName, transaction.getInformation().getCryptoAmount());

        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ACTION_COLUMN.columnName,transaction.getAction().getCode());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_STATUS_COLUMN.columnName, transaction.getInformation().getCryptoStatus().getCode());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_STATUS_COLUMN.columnName, transactionStatus.getCode());
        databaseTableRecord.setStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN.columnName, protocolStatus.getCode());

        databaseTableRecord.setLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_EVENTS_RECORDED_TABLE_TIMESTAMP_COLUMN.columnName, transaction.getTimestamp());

    }

    private Transaction<CryptoTransaction> getTransactionFromRecord(DatabaseTableRecord databaseTableRecord){
        CryptoAddress cryptoAddressFrom = new CryptoAddress();
        cryptoAddressFrom.setAddress(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ADDRESS_FROM_COLUMN.columnName));
        cryptoAddressFrom.setCryptoCurrency(CryptoCurrency.getByCode(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_CURRENCY_COLUMN.columnName)));
        CryptoAddress cryptoAddressTo = new CryptoAddress();
        cryptoAddressTo.setAddress(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ADDRESS_TO_COLUMN.columnName));
        cryptoAddressTo.setCryptoCurrency(CryptoCurrency.getByCode(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_CURRENCY_COLUMN.columnName)));

        CryptoStatus cryptoStatus = null;
        try {
            cryptoStatus = CryptoStatus.getByCode(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_STATUS_COLUMN.columnName));
        } catch (InvalidParameterException e) {
            // TODO: Manage exception
            e.printStackTrace();
        }

        CryptoTransaction cryptoTransaction = new CryptoTransaction(
                  databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_HASH_COLUMN.columnName),
                  cryptoAddressFrom,
                  cryptoAddressTo,
                  CryptoCurrency.getByCode(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_CURRENCY_COLUMN.columnName)),
                  databaseTableRecord.getLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_CRYPTO_AMOUNT_COLUMN.columnName),
                  cryptoStatus
                                                                     );
        Action action = null;
        try {
            action = Action.getByCode(databaseTableRecord.getStringValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ACTION_COLUMN.columnName));
        } catch (InvalidParameterException e) {
            // Manage Exceotion
            e.printStackTrace();
        }

        return new Transaction<>(
                  databaseTableRecord.getUUIDValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_ID_COLUMN.columnName),
                  cryptoTransaction,
                  action,
                  databaseTableRecord.getLongValue(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TIMESTAMP_COLUMN.columnName)
                                  );
    }

    private List<DatabaseTableRecord> getAllRecordsInState(TransactionStatus transactionStatus, ProtocolStatus protocolStatus) {
        DatabaseTable registryTable = this.database.getTable(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_NAME);

        registryTable.setStringFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_TRANSACTION_STATUS_COLUMN.columnName ,
                transactionStatus.getCode(),
                DatabaseFilterType.EQUAL
        );

        registryTable.setStringFilter(IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN.columnName ,
                protocolStatus.getCode(),
                DatabaseFilterType.EQUAL
        );

        try {
            registryTable.loadToMemory();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
            //TODO: MANAGE EXCEPTION
        }

        registryTable.clearAllFilters();

        return registryTable.getRecords();
    }

    private List<Transaction<CryptoTransaction>> getAllTransactionsInState(TransactionStatus transactionStatus, ProtocolStatus protocolStatus) {

        List<Transaction<CryptoTransaction>> returnList = new ArrayList<>();

        List<DatabaseTableRecord> records = getAllRecordsInState(transactionStatus,protocolStatus);

        for(DatabaseTableRecord r : records)
            returnList.add(getTransactionFromRecord(r));

        return returnList;
    }

    protected static class EventWrapper {
        final UUID eventId;
        final String eventType;
        final String eventSource;
        final String eventStatus;
        final long eventTimeStamp;

        public EventWrapper(UUID eventId, String eventType, String eventSource, String eventStatus, long eventTimeStamp) {
            this.eventId = eventId;
            this.eventType = eventType;
            this.eventSource = eventSource;
            this.eventStatus = eventStatus;
            this.eventTimeStamp = eventTimeStamp;
        }
    }
}
