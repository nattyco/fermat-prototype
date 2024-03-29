package com.bitdubai.fermat_api.layer.osa_android.database_system;

import java.util.List;

/**
 *  <p>The abstract class <code>DatabaseTableFilterGroup</code> is a interface
 *     that define the methods to set and get the filter group of query.
 *
 *
 *  @author  Leon Acosta
 *  @version 1.0.0
 *  @since   15/05/15.
 * */
public interface DatabaseTableFilterGroup {
    
    
    public void setFilters(List<DatabaseTableFilter> filters);

    public void setSubGroups(List<DatabaseTableFilterGroup> subGroups);

    public void setOperator(DatabaseFilterOperator operator);

    public List<DatabaseTableFilter>  getFilters();

    public List<DatabaseTableFilterGroup> getSubGroups();

    public DatabaseFilterOperator getOperator();

}
