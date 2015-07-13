package com.bitdubai.fermat_dmp_plugin.layer.module.wallet_runtime.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.layer.dmp_module.wallet_runtime.exceptions.WalletRuntimeExceptions;

/**
 * Created by natalia on 01/07/15.
 */
public class CantFactoryReset extends WalletRuntimeExceptions {

    /**
     *
     */
    private static final long serialVersionUID = 4841032427648911456L;

    public static final String DEFAULT_MESSAGE = "CAN'T LOAD WALLET STRUCTURE";

    /**
     * This is the constructor that every inherited FermatException must implement
     *
     * @param message        the short description of the why this exception happened, there is a public static constant called DEFAULT_MESSAGE that can be used here
     * @param cause          the exception that triggered the throwing of the current exception, if there are no other exceptions to be declared here, the cause should be null
     * @param context        a String that provides the values of the variables that could have affected the exception
     * @param possibleReason an explicative reason of why we believe this exception was most likely thrown
     */

    public CantFactoryReset(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }


}
