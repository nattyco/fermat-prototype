/*
 * @(#ItemByAccountDaoSupport.java 05/14/2015
 * Copyright 2015 bitDubai, Inc. All rights reserved.
 * BITDUBAI/CONFIDENTIAL
 * */

package com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.structure.dao;


// Packages and classes to import of jdk 1.7
import java.util.List;

// Packages and classes to import of bitDubai API.
import com.bitdubai.fermat_api.layer.osa_android.database_system.*;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;

// Packages and classes to import of Middleware Bank Notes API.
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.beans.dto.ItemByAccount;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.beans.dto.Record;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao.ItemByAccountDao;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao.DatabaseCallback;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.structure.BankNotesDatabaseConstants;


/**
 *
 *  <p>The class <code>com.bitdubai.fermat_dmp_plugin.layer._14_middleware.bank_notes.developer.bitdubai.version_1.structure.dao.ItemByAccountDaoSupport</code> is a object
 *     that implements the methods for management the CRUD operations for the ItemByAccount DTO.
 *
 *
 *  @author  Raul Geomar Pena (raul.pena@mac.com)
 *  @version 1.0.0
 *  @since   jdk 1.7
 *  @since   05/14/2015
 *  @see     {@link com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.structure.dao.DatabaseTemplate}
 *  @see     {@link ItemByAccountDao}
 *  @see     {@link ItemByAccount}
 * */
public class ItemByAccountDaoSupport extends DatabaseTemplate implements ItemByAccountDao<Long, ItemByAccount> {


    // Public constructor declarations.
    /**
      *
      *  <p>Constructor without arguments.
      * */
    public ItemByAccountDaoSupport () {

        // Call to super class.
        super ();
    }

    /**
     *
     *  <p>Constructor with arguments.
     *
     *  @param pluginDatabaseSystem Plugin database.
     *  @param database Database object.
     *  @param errorManager Error manager.
     * */
    public ItemByAccountDaoSupport (PluginDatabaseSystem pluginDatabaseSystem, Database database, ErrorManager errorManager) {

        // Call to super class.
        super (pluginDatabaseSystem, database, errorManager);
    }


    // Public instance methods declarations extends of com.bitdubai.fermat_dmp_plugin.layer._14_middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao.ItemByDepotDao
    /**
      * <p>Method that find an object by id.
      *
      * @param id Object id.
      * @return Object found.
      */
    @Override
    public ItemByAccount get (Long id) {
        return null;
    }

    /**
      * <p>Method that list the all objects.
      *
      * @return All objects.
      */
    @Override
    public List<ItemByAccount> findAll () {
        return null;
    }

    /**
      * <p>Method that create a new object.
      *
      * @param target
      */
    @Override
    public void create (final ItemByAccount target) {


        // Valid the arguments.
        if (target == null) {
        /*
         * Cancel the operation.
         * */
            return;
        }


        // 1) Create the callback for save the entity.
        DatabaseCallback call = new DatabaseCallback () {

            public Record doExecute (Database database) {

                // 1) Get the wallet table.
                DatabaseTable table = database.getTable (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_NAME);

                // 2) Create a new entity object.
                DatabaseTableRecord record = table.getEmptyRecord ();

                // 3) Set entity values.
                record.setLongValue(BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_ID, target.getId());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_ITEM, target.getItem ().getId ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_DEPOT, target.getDepot ().getId ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_CREATION, target.getCreation ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_UPDATE, target.getUpdate ());

                // Return the new record entity.
                return new Record (table, record);
            }
        };


        // 2) Create the new entity.
        this.doCreate (call);
    }

    /**
      * <p>Method that update an object.
      *
      * @param target
      */
    @Override
    public void update (final ItemByAccount target) {


        // Valid the arguments.
        if (target == null) {
        /*
         * Cancel the operation.
         * */
            return;
        }


        // 1) Create the callback for save the entity.
        DatabaseCallback call = new DatabaseCallback () {

            public Record doExecute (Database database) {

                // 1) Get the wallet table.
                DatabaseTable table = database.getTable (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_NAME);

                // 2) Create a new entity object.
                DatabaseTableRecord record = table.getEmptyRecord ();

                // 3) Set entity values.
                record.setLongValue(BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_ID, target.getId());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_ITEM, target.getItem ().getId ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_DEPOT, target.getDepot ().getId ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_CREATION, target.getCreation ());
                record.setLongValue (BankNotesDatabaseConstants.ITEM_BY_ACCOUNT_TABLE_COLUMN_UPDATE, target.getUpdate ());

                // Return the new record entity.
                return new Record (table, record);
            }
        };


        // 2) Update entity.
        this.doUpdate (call);
    }

    /**
      * <p>Method that delete an object.
      *
      * @param id Object id.
      */
    @Override
    public void delete (Long id) {

    }
}