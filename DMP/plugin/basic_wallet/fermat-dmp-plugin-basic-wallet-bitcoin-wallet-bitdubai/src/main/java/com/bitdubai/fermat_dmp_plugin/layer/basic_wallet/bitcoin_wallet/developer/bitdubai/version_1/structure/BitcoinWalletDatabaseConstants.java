package com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure;

/**
 * Created by eze on 2015.06.23..
 */
public class BitcoinWalletDatabaseConstants {

    /**
     * Bitcoin Wallet database table definition.
     */
    public static final String BITCOIN_WALLET_TABLE_NAME = "BitcoinWalletWallet";
    public static final String BITCOIN_WALLET_TABLE_ID_COLUMN_NAME = "Id";
    public static final String BITCOIN_WALLET_TABLE_ADDRESS_FROM_COLUMN_NAME = "addressFrom";
    public static final String BITCOIN_WALLET_TABLE_ADDRESS_TO_COLUMN_NAME = "addressTo";
    public static final String BITCOIN_WALLET_TABLE_AMOUNT_COLUMN_NAME = "amount";
    public static final String BITCOIN_WALLET_TABLE_TYPE_COLUMN_NAME = "type";
    public static final String BITCOIN_WALLET_TABLE_BALANCE_TYPE_COLUMN_NAME = "balanceType";
    public static final String BITCOIN_WALLET_TABLE_STATE_COLUMN_NAME = "state";
    public static final String BITCOIN_WALLET_TABLE_TIME_STAMP_COLUMN_NAME = "timestamp";
    public static final String BITCOIN_WALLET_TABLE_MEMO_COLUMN_NAME = "memo";
    public static final String BITCOIN_WALLET_TABLE_TRANSACTION_HASH_COLUMN_NAME = "transactionHash";
    public static final String BITCOIN_WALLET_TABLE_RUNNING_BOOK_BALANCE_COLUMN_NAME = "runningBookBalance";
    public static final String BITCOIN_WALLET_TABLE_RUNNING_AVILABLE_BALANCE_COLUMN_NAME = "runningAvailableBalance";


    // tabla nueva movimientos- balance y book balance, id

    public static final String BITCOIN_WALLET_BALANCE_TABLE_NAME = "BitcoinWalletWalletTotalBalances";
    public static final String BITCOIN_WALLET_BALANCE_TABLE_ID_COLUMN_NAME = "Id";
    public static final String BITCOIN_WALLET_BALANCE_TABLE_AVILABLE_BALANCE_COLUMN_NAME = "avilableBalance";
    public static final String BITCOIN_WALLET_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME = "bookBalance";

}
