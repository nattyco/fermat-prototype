package unit.com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure.NicheWalletTypeCryptoWallet;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformWalletType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactRecord;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactsRegistry;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantCreateWalletContactException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantRequestCryptoAddressException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_user.User;
import com.bitdubai.fermat_api.layer.pip_user.extra_user.ExtraUserManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.exceptions.CantRegisterActorAddressBookException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookRegistry;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.exceptions.CantRegisterWalletAddressBookException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.WalletAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.WalletAddressBookRegistry;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure.NicheWalletTypeCryptoWallet;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class CreateWalletContactTest extends TestCase {

    /**
     * DealsWithActorAddressBook interface Mocked
     */
    @Mock
    ActorAddressBookManager actorAddressBookManager;

    /**
     * DealsWithExtraUserManager interface Mocked
     */
    @Mock
    CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    ErrorManager errorManager;

    /**
     * DealsWithExtraUserManager interface Mocked
     */
    @Mock
    ExtraUserManager extraUserManager;

    /**
     * DealsWithWalletAddressBook interface Mocked
     */
    @Mock
    WalletAddressBookManager walletAddressBookManager;

    /**
     * DealsWithWalletContacts interface Mocked
     */
    @Mock
    WalletContactsManager walletContactsManager;

    @Mock
    ActorAddressBookRegistry actorAddressBookRegistry;

    @Mock
    WalletAddressBookRegistry walletAddressBookRegistry;

    @Mock
    WalletContactsRegistry walletContactsRegistry;

    @Mock
    User user;

    @Mock
    CryptoAddress cryptoAddress;

    @Mock
    WalletContactRecord walletContactRecord;

    String actressName;
    Actors actorType;
    CryptoAddress deliveredCryptoAddress;
    PlatformWalletType platformWalletType;
    UUID walletId;

    NicheWalletTypeCryptoWallet nicheWalletTypeCryptoWallet;

    @Before
    public void setUp() throws Exception {
        actressName = "Penelope Cruz";
        actorType = Actors.EXTRA_USER;
        deliveredCryptoAddress = new CryptoAddress("asdasd ", CryptoCurrency.BITCOIN);
        platformWalletType = PlatformWalletType.BASIC_WALLET_BITCOIN_WALLET;
        walletId = UUID.randomUUID();
        nicheWalletTypeCryptoWallet = new NicheWalletTypeCryptoWallet();
        nicheWalletTypeCryptoWallet.setActorAddressBookManager(actorAddressBookManager);
        nicheWalletTypeCryptoWallet.setErrorManager(errorManager);
        nicheWalletTypeCryptoWallet.setExtraUserManager(extraUserManager);
        nicheWalletTypeCryptoWallet.setWalletAddressBookManager(walletAddressBookManager);
        nicheWalletTypeCryptoWallet.setWalletContactsManager(walletContactsManager);
        nicheWalletTypeCryptoWallet.setCryptoVaultManager(cryptoVaultManager);
        doReturn(actorAddressBookRegistry).when(actorAddressBookManager).getActorAddressBookRegistry();
        doReturn(walletAddressBookRegistry).when(walletAddressBookManager).getWalletAddressBookRegistry();
        doReturn(walletContactsRegistry).when(walletContactsManager).getWalletContactsRegistry();
        doReturn(user).when(extraUserManager).createUser(anyString());
        doReturn(cryptoAddress).when(cryptoVaultManager).getAddress();
        doReturn(walletContactRecord).when(walletContactsRegistry).createWalletContact(any(UUID.class), anyString(), any(Actors.class), any(CryptoAddress.class), any(UUID.class));
        nicheWalletTypeCryptoWallet.initialize();
    }

    @Test
    public void testCreateWalletContact_NotNull() throws Exception {
        WalletContactRecord walletContactRecord = nicheWalletTypeCryptoWallet.createWalletContact(deliveredCryptoAddress, actressName, actorType, platformWalletType, walletId);
        assertNotNull(walletContactRecord);
    }

    // TYPE OF ACTOR NOT RECOGNIZED BY THE PLUGIN
    @Test(expected=CantCreateWalletContactException.class)
    public void testCreateWalletContact_ActorTypeNotRecognized() throws Exception {
        actorType = Actors.INTRA_USER;

        nicheWalletTypeCryptoWallet.createWalletContact(deliveredCryptoAddress, actressName, actorType, platformWalletType, walletId);
    }

    /**
     * TODO: CANT CREATE USER TEST / EXTRA USER EXCEPTION DOESN'T EXIST
     */


    // CANT REGISTER ACTOR ADDRESS BOOK TEST
    @Test(expected=CantCreateWalletContactException.class)
    public void testCreateWalletContact_CantRegisterActorAddressBookException() throws Exception {
        doThrow(new com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantCreateWalletContactException("gasdil", null, null, null))
                .when(walletContactsRegistry).createWalletContact(any(UUID.class), anyString(), any(Actors.class), any(CryptoAddress.class), any(UUID.class));

        nicheWalletTypeCryptoWallet.createWalletContact(deliveredCryptoAddress, actressName, actorType, platformWalletType, walletId);
    }
}
