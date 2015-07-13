package unit.com.bitdubai.fermat_dmp_plugin.layer.module.wallet_runtime.developer.bitdubai.version_1.WalletRuntimeModulePluginRoot;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventListener;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventType;
import com.bitdubai.fermat_dmp_plugin.layer.module.wallet_runtime.developer.bitdubai.version_1.WalletRuntimeModulePluginRoot;

import junit.framework.TestCase;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static org.mockito.Mockito.when;

/**
 * Created by natalia on 06/07/15.
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

    @Mock
    private EventManager mockEventManager1;
    @Mock
    private EventListener mockEventListener1;

    private WalletRuntimeModulePluginRoot testWalletRuntimeModulePluginRoot;

    @Before
    public void setUp() throws Exception {
        testWalletRuntimeModulePluginRoot = new WalletRuntimeModulePluginRoot();
        testWalletRuntimeModulePluginRoot.setPluginFileSystem(mockPluginFileSystem);
        testWalletRuntimeModulePluginRoot.setEventManager(mockEventManager);
        testWalletRuntimeModulePluginRoot.setErrorManager(mockErrorManager);
    }


    @Test
    public void teststart_ThePlugInHasStartedOk_ThrowsCantStartPluginException() throws Exception {


        when(mockEventManager.getNewListener(EventType.WALLET_OPENED)).thenReturn(mockEventListener);

        catchException(testWalletRuntimeModulePluginRoot).start();

        Assertions.assertThat(testWalletRuntimeModulePluginRoot.getStatus()).isEqualTo(ServiceStatus.CREATED);
    }

}
