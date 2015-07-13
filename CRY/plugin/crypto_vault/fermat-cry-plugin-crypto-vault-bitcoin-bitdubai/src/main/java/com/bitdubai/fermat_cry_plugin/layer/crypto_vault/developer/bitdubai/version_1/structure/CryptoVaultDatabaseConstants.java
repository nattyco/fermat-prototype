package com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure;

/**
 * Created by rodrigo on 11/06/15.
 */
public class CryptoVaultDatabaseConstants {

    /**
     * Crypto Transactions database table definition.
     */
    public static final String CRYPTO_TRANSACTIONS_TABLE_NAME = "crypto_transactions";
    public static final String CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME = "trx_id";
    public static final String CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME = "trx_hash";
    public static final String CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME = "protocol_status";
    public static final String CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME = "transaction_status";


    /**
     * Fermat transaction database table definition
     */
    public static final String FERMAT_TRANSACTIONS_TABLE_NAME = "fermat_transactions";
    public static final String FERMAT_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME = "trx_id";

    /**
     * TransitionProtocol_Status table definition
     */
    public static final String TRANSITION_PROTOCOL_STATUS = "transition_protocol_status";
    public static final String TRANSITION_PROTOCOL_STATUS_TABLE_TIMESTAMP_COLUMN_NAME = "timestamp";
    public static final String TRANSITION_PROTOCOL_STATUS_TABLE_ocurrences_COLUMN_NAME = "ocurrences";
}
