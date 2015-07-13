package com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by eze on 09/06/15.
 */
public enum Specialist {
    CRYPTO_ROUTER_SPECIALIST ("CPR"),
    EXTRA_USER_SPECIALIST ("EXU"),
    DEVICE_USER_SPECIALIST ("DVU"),
    INTRA_USER_SPECIALIST ("INU"),
    UNKNOWN_SPECIALIST ("UNK");

    private final String code;

    Specialist(String Code) {
        this.code = Code;
    }

    public String getCode()   { return this.code ; }

    public static Specialist getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "CPR": return Specialist.CRYPTO_ROUTER_SPECIALIST;
            case "EXU": return Specialist.EXTRA_USER_SPECIALIST;
            case "DVU": return Specialist.DEVICE_USER_SPECIALIST;
            case "INU": return Specialist.INTRA_USER_SPECIALIST;
            case "UNK": return Specialist.UNKNOWN_SPECIALIST;
        }

        /**
         * If we try to cpmvert am invalid string.
         */
        throw new InvalidParameterException(code);
    }
}
