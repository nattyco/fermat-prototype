package com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPlatformDatabaseSystem;

import com.bitdubai.fermat_api.layer.osa_android.database_system.PlatformDatabaseSystem;

import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedAddonsExceptionSeverity;

import com.bitdubai.fermat_api.layer.pip_user.User;

import com.bitdubai.fermat_api.layer.pip_user.extra_user.UserRegistry;
import com.bitdubai.fermat_api.layer.pip_user.extra_user.exceptions.CantCreateExtraUserRegistry;
import com.bitdubai.fermat_api.layer.pip_user.extra_user.exceptions.CantGetExtraUserRegistry;
import com.bitdubai.fermat_api.layer.pip_user.extra_user.exceptions.CantInitializeExtraUserRegistryException;

import java.util.UUID;

/**
 * Created by ciencias on 3/18/15.
 * Modified by natalia
 */
public class ExtraUserRegistry implements DealsWithErrors,DealsWithPlatformDatabaseSystem,UserRegistry {


    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;

    /**
     * DealsWithPlatformDatabaseSystem Interface member variables.
     */
    PlatformDatabaseSystem platformDatabaseSystem;


    /**
     * UserRegistry Interface member variables.
     */


    private Database database;


    /**
     *DealsWithErrors Interface implementation.
     */

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }


    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */

    @Override
    public void setPlatformDatabaseSystem(PlatformDatabaseSystem platformDatabaseSystem) {
        this.platformDatabaseSystem = platformDatabaseSystem;
    }




    public void initialize() throws CantInitializeExtraUserRegistryException {

        /**
         * I will try to open the users' database..
         */
        try
        {

            this.database = this.platformDatabaseSystem.openDatabase("ExtraUser");
        }
        catch (DatabaseNotFoundException databaseNotFoundException) {

            ExtraUserDatabaseFactory databaseFactory = new ExtraUserDatabaseFactory();
            databaseFactory.setPlatformDatabaseSystem(this.platformDatabaseSystem);
            databaseFactory.setErrorManager(this.errorManager);

            /**
             * I will create the database where I am going to store the information of this wallet.
             */

            try {

                this.database =  databaseFactory.createDatabase();

            }
            catch (CantCreateDatabaseException cantCreateDatabaseException){

                /**
                 * The database cannot be created. I can not handle this situation.
                 */
/*Francisco Arce
        Exception in the context Fermat Context
        *
        * */
                String message =CantInitializeExtraUserRegistryException.DEFAULT_MESSAGE;
                FermatException cause = cantCreateDatabaseException.getCause();
                String context = "DataBase Factory: "+  cantCreateDatabaseException.getContext();
                String possibleReason  = "The exception occurred when calling  'databaseFactory.createDatabase()': " + cantCreateDatabaseException.getPossibleReason();

                errorManager.reportUnexpectedAddonsException(Addons.EXTRA_USER, UnexpectedAddonsExceptionSeverity.DISABLES_THIS_ADDONS, cantCreateDatabaseException);
               /*Francisco Arce*/
                throw new CantInitializeExtraUserRegistryException(message, cause, context, possibleReason);
            }
        }
        catch (CantOpenDatabaseException cantOpenDatabaseException){

            /**
             * The database exists but cannot be open. I can not handle this situation.
             */
/*Francisco Arce
        Exception in the context Fermat Context
        *
        * */
            String message =  CantInitializeExtraUserRegistryException.DEFAULT_MESSAGE;
            FermatException cause = cantOpenDatabaseException.getCause();
            String context = "Create Database:" + cantOpenDatabaseException.getContext();
            String possibleReason = "The exception occurred while trying to open the database of users 'this.database = this.platformDatabaseSystem.openDatabase (\"ExtraUser\")': " + cantOpenDatabaseException.getPossibleReason();

            errorManager.reportUnexpectedAddonsException(Addons.EXTRA_USER, UnexpectedAddonsExceptionSeverity.DISABLES_THIS_ADDONS, cantOpenDatabaseException);
            /*
            Modified by Francisco Arce
            */
            throw new CantInitializeExtraUserRegistryException(message, cause, context, possibleReason);
        }

    }

    /**
     * UserRegistry interface implementation.
     */


    /**
     *<p>Create a new Extra User, insert new table record.
     *
     * @param userName
     * @return Object user
     * @throws CantCreateExtraUserRegistry
     */
    @Override
    public User createUser(String userName) throws CantCreateExtraUserRegistry {

        /**
         *  I create new ExtraUser instance
         */

        User user = new ExtraUser();

        UUID userId = UUID.randomUUID();

        user.setId(userId);
        user.setName(userName);
        /**
         * Here I create a new Extra User record .
         */
        long unixTime = System.currentTimeMillis() / 1000L;

        DatabaseTable extrauserTable = database.getTable(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_NAME);
        DatabaseTableRecord extrauserRecord = extrauserTable.getEmptyRecord();

        extrauserRecord.setUUIDValue(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_ID_COLUMN_NAME , userId);
        extrauserRecord.setStringValue(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_NAME_COLUMN_NAME, userName);
        extrauserRecord.setLongValue(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_TIME_STAMP_COLUMN_NAME, unixTime);


        try{
            extrauserTable.insertRecord(extrauserRecord);
        }catch(CantInsertRecordException cantInsertRecord)
        {
            /**
             * I can not solve this situation.
             */
        /*Francisco Arce
        Exception in the context Fermat Context
        *
        * */
            String message =  CantCreateExtraUserRegistry.DEFAULT_MESSAGE;
            FermatException cause = cantInsertRecord.getCause();
            String context = "Extra User Record: " + cantInsertRecord.getContext();
            String possibleReason = "The exception occurred when recording the Extra User extrauserTable.insertRecord(extrauserRecord): " +  cantInsertRecord.getPossibleReason();
            errorManager.reportUnexpectedAddonsException(Addons.EXTRA_USER, UnexpectedAddonsExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_ADDONS, cantInsertRecord);
            /*
          modified by Francisco Arce
            */
            throw new CantCreateExtraUserRegistry(message, cause, context, possibleReason);
        }

        return user;
    }

    /**
     * <p>Return a specific user, looking for registered user id.
     *
     * @param  userId
     * @return Object user
     * @throws CantGetExtraUserRegistry
     */
    @Override
    public User getUser(UUID userId) throws CantGetExtraUserRegistry {


        /**
         * Reads the user data table , in this case only the name , creates an instance and returns
         */

        DatabaseTable table;

        /**
         *  I will load the information of table into a memory structure, filter by user id .
         */
        table = this.database.getTable(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_NAME);
        table.setUUIDFilter(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_ID_COLUMN_NAME, userId, DatabaseFilterType.EQUAL);
        try {
            table.loadToMemory();
        }
        catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            /**
             * I can not solve this situation.
             */
            /*Francisco Arce
            Exception in the context Fermat Context
            *
            * */
            String message = CantGetExtraUserRegistry.DEFAULT_MESSAGE;
            FermatException cause = cantLoadTableToMemory.getCause();
            String context = "table Memory: " + cantLoadTableToMemory.getContext();
            String possibleReason = "The exception occurred when calling table.loadToMemory (): " + cantLoadTableToMemory.getPossibleReason();

            errorManager.reportUnexpectedAddonsException(Addons.EXTRA_USER, UnexpectedAddonsExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_ADDONS, cantLoadTableToMemory);
            throw new CantGetExtraUserRegistry(message, cause, context, possibleReason);
        }


        /**
         * Will go through the records getting each extra user.
         */

        UUID user_id ;
        User user = new ExtraUser();
        user.setId(userId);
        for (DatabaseTableRecord record : table.getRecords()) {
            user.setName(record.getStringValue(ExtraUserDatabaseConstants.EXTRA_USER_TABLE_NAME_COLUMN_NAME));

        }


        return user;
    }
}
