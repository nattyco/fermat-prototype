package com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.enums;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;

/**
 * Created by eze on 2015.06.25..
 */
public enum TransactionStatus {
    NEW("NEW"),
    PERSISTED_IN_WALLET ("PIW"),
    SENT_TO_CRYPTO_VOULT("STCV"),
    SUCCESSFUL_SENT("SS"),
    CANCELED("CLD");


    private final String code;

    TransactionStatus(String code){
        this.code = code;
    }

    public String getCode() {return this.code;}

    public static TransactionStatus getByCode(String code) throws InvalidParameterException {
        switch (code){
            case "NEW": return TransactionStatus.NEW;
            case "PIW": return TransactionStatus.PERSISTED_IN_WALLET;
            case "STCV": return TransactionStatus.SENT_TO_CRYPTO_VOULT;
            case "SS": return TransactionStatus.SUCCESSFUL_SENT;
            case "CLD": return TransactionStatus.CANCELED;
        }
        /**
         * If we try to cpmvert am invalid string.
         */
        throw new InvalidParameterException(code);
    }
}
