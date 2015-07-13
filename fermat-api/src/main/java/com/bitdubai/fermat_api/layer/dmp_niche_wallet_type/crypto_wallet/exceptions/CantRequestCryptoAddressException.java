package com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions;

/**
 * The interface <code>com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantRequestCryptoAddressException</code>
 * is thrown when i cant request an address of the wallet.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/06/15.
 * @version 1.0
 */
public class CantRequestCryptoAddressException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T REQUEST CRYPTO ADDRESS EXCEPTION";

    public CantRequestCryptoAddressException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantRequestCryptoAddressException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantRequestCryptoAddressException(final String message) {
        this(message, null);
    }

    public CantRequestCryptoAddressException() {
        this(DEFAULT_MESSAGE);
    }
}
