package com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions;

/**
 * The interface <code>com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetCryptoWalletException</code>
 * is thrown when i cant RETURN the wallet.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/06/15.
 * @version 1.0
 */
public class CantGetCryptoWalletException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T GET CRYPTO WALLET EXCEPTION";

    public CantGetCryptoWalletException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantGetCryptoWalletException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantGetCryptoWalletException(final String message) {
        this(message, null);
    }

    public CantGetCryptoWalletException() {
        this(DEFAULT_MESSAGE);
    }
}
