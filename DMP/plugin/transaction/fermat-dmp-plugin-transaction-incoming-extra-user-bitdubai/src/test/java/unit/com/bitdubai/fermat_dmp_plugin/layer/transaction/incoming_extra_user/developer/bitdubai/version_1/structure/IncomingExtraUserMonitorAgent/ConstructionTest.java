package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserMonitorAgent;

import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_cry_api.layer.crypto_router.incoming_crypto.IncomingCryptoManager;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserMonitorAgent;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserRegistry;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.googlecode.catchexception.CatchException.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
/**
 * Created by jorgegonzalez on 2015.07.02..
 */
public class ConstructionTest {

    @Mock
    private ErrorManager mockErrorManager;

    @Mock
    private IncomingCryptoManager mockIncomingCryptoManager;

    @Mock
    private IncomingExtraUserRegistry mockRegistry;

    private IncomingExtraUserMonitorAgent testMonitorAgent;

    public void Construction_ValidParameters_NewObjectCreated() {
        testMonitorAgent = new IncomingExtraUserMonitorAgent(mockErrorManager, mockIncomingCryptoManager, mockRegistry);
        assertThat(testMonitorAgent).isNotNull();
    }
}
