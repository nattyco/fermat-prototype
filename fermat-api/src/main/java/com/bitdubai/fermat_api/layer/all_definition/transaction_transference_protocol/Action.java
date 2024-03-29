package com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by eze on 09/06/15.
 */
public enum Action {
    APPLY ("APP"),
    REVERT ("REV");

    private final String code;

    Action(String Code) {
        this.code = Code;
    }

    public String getCode()   { return this.code ; }

    public static Action getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "APP": return Action.APPLY;
            case "REV": return Action.REVERT;
        }

        /**
         * If we try to cpmvert am invalid string.
         */
        throw new InvalidParameterException(code);
    }
}
