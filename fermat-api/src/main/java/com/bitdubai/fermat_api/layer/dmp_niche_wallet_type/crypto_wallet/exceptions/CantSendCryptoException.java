package com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions;

/**
 * The interface <code>com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetBalanceException</code>
 * is thrown when i cant get balance of the wallet.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/06/15.
 * @version 1.0
 */
public class CantSendCryptoException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T SEND CRYPTO EXCEPTION";

    public CantSendCryptoException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantSendCryptoException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantSendCryptoException(final String message) {
        this(message, null);
    }

    public CantSendCryptoException() {
        this(DEFAULT_MESSAGE);
    }
}
