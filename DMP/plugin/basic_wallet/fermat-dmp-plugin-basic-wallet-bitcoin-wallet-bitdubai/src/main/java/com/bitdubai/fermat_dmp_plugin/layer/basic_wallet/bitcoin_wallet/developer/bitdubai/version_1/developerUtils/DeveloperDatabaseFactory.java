package com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.developerUtils;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure.BitcoinWalletDatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eze on 2015.06.29..
 */
public class DeveloperDatabaseFactory {

    String pluginId;
    List<String> walletsIds;

    public DeveloperDatabaseFactory (String pluginId, List<String> walletsIds){
        this.pluginId = pluginId;
        this.walletsIds = walletsIds;
    }


    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        /**
         * We have one database for each walletId, so we will return all their names.
         * Remember that a database name in this plug-in is the internal wallet id.
         */
        List<DeveloperDatabase> databases = new ArrayList<DeveloperDatabase>();
        for(String databaseName : this.walletsIds)
            databases.add(developerObjectFactory.getNewDeveloperDatabase(databaseName, this.pluginId));
        return databases;
    }

    public static List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory) {
        List<DeveloperDatabaseTable> tables = new ArrayList<DeveloperDatabaseTable>();

        /*
         * We only have one table in each database, let's complete it
         */
        List<String> basicWalletBitcoinWalletColumns = new ArrayList<>();
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_ID_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_ADDRESS_FROM_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_ADDRESS_TO_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_AMOUNT_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_TYPE_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_STATE_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_TIME_STAMP_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_MEMO_COLUMN_NAME);
        basicWalletBitcoinWalletColumns.add(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_TRANSACTION_HASH_COLUMN_NAME);

        /**
         * basicWalletBitcoinWalletColumns table
         */
        DeveloperDatabaseTable  cryptoTransactionsTable = developerObjectFactory.getNewDeveloperDatabaseTable(BitcoinWalletDatabaseConstants.BITCOIN_WALLET_TABLE_NAME, basicWalletBitcoinWalletColumns);
        tables.add(cryptoTransactionsTable);

        return tables;
    }


    public static List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory,  Database database, DeveloperDatabaseTable developerDatabaseTable) {
        /**
         * Will get the records for the given table
         */
        List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<DeveloperDatabaseTableRecord>();


        /**
         * I load the passed table name from the SQLite database.
         */
        DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());
        try {
            selectedTable.loadToMemory();
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            /**
             * if there was an error, I will returned an empty list.
             */
            return returnedRecords;
        }

        List<DatabaseTableRecord> records = selectedTable.getRecords();
        for (DatabaseTableRecord row: records){
            /**
             * for each row in the table list
             */
            List<String> developerRow = new ArrayList<String>();
            for (DatabaseRecord field : row.getValues()){
                /**
                 * I get each row and save them into a List<String>
                 */
                developerRow.add(field.getValue());
            }
            /**
             * I create the Developer Database record
             */
            returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));

        }


        /**
         * return the list of DeveloperRecords for the passed table.
         */
        return returnedRecords;
    }


}
