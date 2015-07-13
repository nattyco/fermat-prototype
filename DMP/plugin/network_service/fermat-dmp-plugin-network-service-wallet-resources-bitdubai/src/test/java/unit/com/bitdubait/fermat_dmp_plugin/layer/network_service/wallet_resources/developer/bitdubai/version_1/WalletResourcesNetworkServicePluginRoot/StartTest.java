package unit.com.bitdubait.fermat_dmp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.WalletResourcesNetworkServicePluginRoot;

import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.enums.Wallets;

import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventListener;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;
import com.bitdubai.fermat_dmp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.WalletResourcesNetworkServicePluginRoot;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by natalia on 03/07/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class StartTest extends TestCase {

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    private ErrorManager mockErrorManager;

    /**
     * UsesFileSystem Interface member variables.
     */
    @Mock
    private PluginFileSystem mockPluginFileSystem;

    /**
     * DealWithEvents Iianterface member variables.
     */
    @Mock
    private EventManager mockEventManager;
    @Mock
    private EventListener mockEventListener;

    private WalletResourcesNetworkServicePluginRoot testWalletResourcePluginRoot;

    @Before
    public void setUp() throws Exception {
        testWalletResourcePluginRoot = new WalletResourcesNetworkServicePluginRoot();
        testWalletResourcePluginRoot.setwalletType(Wallets.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI);
        testWalletResourcePluginRoot.setPluginFileSystem(mockPluginFileSystem);
        testWalletResourcePluginRoot.setEventManager(mockEventManager);
        testWalletResourcePluginRoot.setErrorManager(mockErrorManager);
    }

    @Test
    public void teststart_ThePlugInHasStartedOk_ThrowsCantStartPluginException() throws Exception {
        when(mockEventManager.getNewListener(EventType.BEGUN_WALLET_INSTALLATION)).thenReturn(mockEventListener);
        catchException(testWalletResourcePluginRoot).start();
        assertThat(caughtException()).isNull();
        assertThat(testWalletResourcePluginRoot.getStatus()).isEqualTo(ServiceStatus.STARTED);
    }

}
