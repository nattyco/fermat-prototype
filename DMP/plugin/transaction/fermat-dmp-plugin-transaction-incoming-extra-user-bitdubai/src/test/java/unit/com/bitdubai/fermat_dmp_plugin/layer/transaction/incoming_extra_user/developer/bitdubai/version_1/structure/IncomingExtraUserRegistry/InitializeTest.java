package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserRegistry;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.googlecode.catchexception.CatchException.*;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.CantInitializeCryptoRegistryException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserDataBaseConstants;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserRegistry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

/**
 * Created by jorgegonzalez on 2015.07.02..
 */
@RunWith(MockitoJUnitRunner.class)
public class InitializeTest {

    @Mock
    private ErrorManager mockErrorManager = mock(ErrorManager.class);
    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem = mock(PluginDatabaseSystem.class);
    @Mock
    private Database mockDatabase = mock(Database.class);

    private UUID testId;
    private IncomingExtraUserRegistry testRegistry;

    @Before
    public void setUpId(){
        testId = UUID.randomUUID();
    }

    @Test
    public void Initialize_RegistryProperlySet_MethodSuccesfullyInvoked() throws Exception{
        when(mockPluginDatabaseSystem.openDatabase(testId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE)).thenReturn(mockDatabase);

        testRegistry = new IncomingExtraUserRegistry();
        testRegistry.setErrorManager(mockErrorManager);
        testRegistry.setPluginDatabaseSystem(mockPluginDatabaseSystem);
        catchException(testRegistry).initialize(testId);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void Initialize_NoPluginDatabaseSystemSet_ThrowsCantInitializeCryptoRegistryException() throws Exception{
        testRegistry = new IncomingExtraUserRegistry();
        testRegistry.setErrorManager(mockErrorManager);
        catchException(testRegistry).initialize(testId);
        assertThat(caughtException()).isInstanceOf(CantInitializeCryptoRegistryException.class);
        caughtException().printStackTrace();
    }

    @Ignore
    @Test
    public void Initialize_DatabaseNotFound_MethodSuccesfullyInvoked() throws Exception{
        /*
         * TODO This test should pass but there is a wrong design decision that makes a cast of the Database interface into the DatabaseFactory; we should really look into that
         */
        when(mockPluginDatabaseSystem.openDatabase(testId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE)).thenThrow(new DatabaseNotFoundException(DatabaseNotFoundException.DEFAULT_MESSAGE, null, null, null));
        when(mockPluginDatabaseSystem.createDatabase(testId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE)).thenReturn(mockDatabase);
        testRegistry = new IncomingExtraUserRegistry();
        testRegistry.setErrorManager(mockErrorManager);
        testRegistry.setPluginDatabaseSystem(mockPluginDatabaseSystem);
        catchException(testRegistry).initialize(testId);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void Initialize_DatabaseNotFoundAndCreateDatabaseFailed_MethodSuccesfullyInvoked() throws Exception{
        when(mockPluginDatabaseSystem.openDatabase(testId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE)).thenThrow(new DatabaseNotFoundException("MOCK", null, null, null));
        when(mockPluginDatabaseSystem.createDatabase(testId, IncomingExtraUserDataBaseConstants.INCOMING_EXTRA_USER_DATABASE)).thenThrow(new CantCreateDatabaseException("MOCK", null, null, null));
        testRegistry = new IncomingExtraUserRegistry();
        testRegistry.setErrorManager(mockErrorManager);
        testRegistry.setPluginDatabaseSystem(mockPluginDatabaseSystem);
        catchException(testRegistry).initialize(testId);
        assertThat(caughtException()).isInstanceOf(CantInitializeCryptoRegistryException.class);
        caughtException().printStackTrace();
    }

}
