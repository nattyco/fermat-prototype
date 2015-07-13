package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.middleware.wallet_contacts.WalletContactRecord</code>
 * indicates the functionality of a WalletContactRecord and its attributes
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 08/06/15.
 * @version 1.0
 * @since Java JDK 1.7
 */
public interface WalletContactRecord {

    /**
     * Return the contactId
     *
     * @return UUID
     */
    UUID getContactId();

    /**
     * Return the walletId
     *
     * @return UUID
     */
    UUID getWalletId();


    /**
     * Return the actorType
     *
     * @return Actors
     */
    Actors getActorType();

    /**
     * Return the deliveredCryptoAddress
     *
     * @return CryptoAddress
     */
    CryptoAddress getReceivedCryptoAddress();

    /**
     * Return the actorId
     *
     * @return UUID
     */
    UUID getActorId();

    /**
     * Return the actorName
     *
     * @return String
     */
    String getActorName();

}
