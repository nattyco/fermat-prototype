package unit.com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure.NicheWalletTypeCryptoWallet;

import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetBalanceException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.WalletAddressBookManager;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure.NicheWalletTypeCryptoWallet;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class GetBalanceTest extends TestCase {

    /**
     * DealsWithActorAddressBook interface Mocked
     */
    @Mock
    ActorAddressBookManager actorAddressBookManager;

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    ErrorManager errorManager;

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

    /**
     * DealsWithBitcoinWallet interface Mocked
     */
    @Mock
    BitcoinWalletManager bitcoinWalletManager;


    @Mock
    BitcoinWalletWallet bitcoinWalletWallet;

    UUID walletId;

    NicheWalletTypeCryptoWallet nicheWalletTypeCryptoWallet;

    @Before
    public void setUp() throws Exception {
        walletId = UUID.randomUUID();
        nicheWalletTypeCryptoWallet = new NicheWalletTypeCryptoWallet();
        nicheWalletTypeCryptoWallet.setActorAddressBookManager(actorAddressBookManager);
        nicheWalletTypeCryptoWallet.setErrorManager(errorManager);
        nicheWalletTypeCryptoWallet.setWalletAddressBookManager(walletAddressBookManager);
        nicheWalletTypeCryptoWallet.setWalletContactsManager(walletContactsManager);
        nicheWalletTypeCryptoWallet.setBitcoinWalletManager(bitcoinWalletManager);
        nicheWalletTypeCryptoWallet.initialize();
    }

    @Test
    public void testGetBalance_Success() throws Exception {
        doReturn(bitcoinWalletWallet).when(bitcoinWalletManager).loadWallet(any(UUID.class));
        nicheWalletTypeCryptoWallet.getBalance(walletId);
    }

    @Test(expected=CantGetBalanceException.class)
    public void testGetBalance_CantLoadWalletException() throws Exception {
        doThrow(new CantLoadWalletException("gasdil", null, null, null))
                .when(bitcoinWalletManager).loadWallet(any(UUID.class));

        nicheWalletTypeCryptoWallet.getBalance(walletId);
    }

    @Test(expected=CantGetBalanceException.class)
    public void testGetBalance_CantCalculateBalanceException() throws Exception {
        doReturn(bitcoinWalletWallet).when(bitcoinWalletManager).loadWallet(any(UUID.class));
        doThrow(new CantCalculateBalanceException("gasdil", null, null, null))
        .when(bitcoinWalletWallet).getBalance();

        nicheWalletTypeCryptoWallet.getBalance(walletId);
    }
}
