package com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantUpdateWalletContactException</code>
 * is thrown when an error occurs trying to update any contact from a wallet
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/06/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantUpdateWalletContactException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T UPDATE WALLET CONTACT EXCEPTION";

    public CantUpdateWalletContactException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantUpdateWalletContactException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantUpdateWalletContactException(final String message) {
        this(message, null);
    }

    public CantUpdateWalletContactException() {
        this(DEFAULT_MESSAGE);
    }
}
