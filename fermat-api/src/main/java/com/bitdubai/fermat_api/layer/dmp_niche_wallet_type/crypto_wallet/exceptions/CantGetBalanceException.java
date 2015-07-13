package com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions;

/**
 * The interface <code>com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetBalanceException</code>
 * is thrown when i cant get balance of the wallet.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/06/15.
 * @version 1.0
 */
public class CantGetBalanceException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T GET BALANCE EXCEPTION";

    public CantGetBalanceException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantGetBalanceException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantGetBalanceException(final String message) {
        this(message, null);
    }

    public CantGetBalanceException() {
        this(DEFAULT_MESSAGE);
    }
}
