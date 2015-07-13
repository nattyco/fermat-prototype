package com.bitdubai.sub_app.developer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bitdubai.sub_app.developer.R;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.pip_actor.developer.DatabaseTool;
import com.bitdubai.fermat_api.layer.pip_actor.developer.ToolManager;
import com.bitdubai.sub_app.developer.common.Databases;
import com.bitdubai.sub_app.developer.common.Resource;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments.DatabaseToolsDatabaseListFragment</code>
 * haves all methods for the database tools activity of a developer
 * <p/>
 * <p/>
 * Created by MAti
 *
 * @version 1.0
 */
public class DatabaseToolsDatabaseTableRecordListFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    View rootView;

    private DatabaseTool databaseTools;

    private DeveloperDatabase developerDatabase;
    private DeveloperDatabaseTable developerDatabaseTable;

    List<DeveloperDatabaseTableRecord> developerDatabaseTableRecordList;

    private static Platform platform = new Platform();




    private Resource resource;

    LinearLayout base;
    TableLayout tableLayout;

    public static DatabaseToolsDatabaseTableRecordListFragment newInstance(int position) {
        DatabaseToolsDatabaseTableRecordListFragment f = new DatabaseToolsDatabaseTableRecordListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        try {
            ToolManager toolManager = platform.getToolManager();
            try {
                databaseTools = toolManager.getDatabaseTool();
            } catch (Exception e) {
                showMessage("CantGetToolManager - " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception ex) {
            showMessage("Unexpected error get Transactions - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.database_table_record, container, false);
        List<String> columnNames = null;
        List<List<String>> values=null;

        try {
            if (resource.type== Databases.TYPE_ADDON) {
                Addons addon = Addons.getByKey(resource.resource);
                this.developerDatabaseTableRecordList = databaseTools.getAddonTableContent(addon, developerDatabase, developerDatabaseTable);
            } else if (resource.type== Databases.TYPE_PLUGIN) {
                Plugins plugin = Plugins.getByKey(resource.resource);
                this.developerDatabaseTableRecordList = databaseTools.getPluginTableContent(plugin, developerDatabase, developerDatabaseTable);
            }

            columnNames = developerDatabaseTable.getFieldNames();
            int i=0;


            if (developerDatabaseTableRecordList.size() > 0) {
                i=0;
                values =new ArrayList<List<String>>();
                for(DeveloperDatabaseTableRecord developerDatabaseTableRecord:developerDatabaseTableRecordList){
                    values.add(developerDatabaseTableRecord.getValues());
                    i++;
                }
            } else {
                //listString = new String[0];

            }

        } catch (Exception e) {
            showMessage("DatabaseTools Database Table List Fragment onCreateView Exception - " + e.getMessage());
            e.printStackTrace();
        }


        base=(LinearLayout)rootView.findViewById(R.id.base);


        ScrollView scrollView = new ScrollView(getActivity());
        if(developerDatabaseTableRecordList!=null){
            TableLayout tableLayout = createTable(values, columnNames, developerDatabaseTableRecordList.size(), columnNames.size());
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());

            horizontalScrollView.addView(tableLayout);
            scrollView.addView(horizontalScrollView);
            base.addView(scrollView);
        }

        return rootView;
    }

    private TableLayout createTable(List<List<String>> lstRows, List<String> lstColumns,int rowCount, int columnCount){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CaviarDreams.ttf");

        TableLayout tableLayout= new TableLayout(getActivity());
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayout.setBackgroundColor(Color.BLACK);
        tableLayout.setPadding(3,3,3,3);
        try {
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(5, 5, 5, 5);
            tableRowParams.weight = 1;

            TableRow tableRow = new TableRow(getActivity());
            if(lstColumns!=null) {

                for (int i = 0; i < lstColumns.size(); i++) {
                    TextView textView = new TextView(getActivity());
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setText(lstColumns.get(i));
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(10, 10, 10, 10);
                    textView.setTypeface(tf);
                    // 2) create tableRow params

                    tableRow.addView(textView, tableRowParams);
                }
            }
            tableLayout.addView(tableRow);
            if(lstRows!=null) {
                //tableRow = new TableRow(getActivity());
                for (List<String> lstValues:lstRows){
                    tableRow = new TableRow(getActivity());
                    for (String values:lstValues){
                        TextView textView = new TextView(getActivity());
                        textView.setBackgroundColor(Color.WHITE);
                        textView.setText(values);
                        textView.setTextColor(Color.BLACK);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setTypeface(tf);
                        tableRow.addView(textView, tableRowParams);
                    }
                    tableLayout.addView(tableRow,tableLayoutParams);
                }

            }

        }catch (Exception e){
            showMessage("Table layout create Exception - " + e.getMessage());
            e.printStackTrace();
        }
        return tableLayout;
    }


    //show alert
    private void showMessage(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(text);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // aquí puedes añadir funciones
            }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    public void setDeveloperDatabase(DeveloperDatabase developerDatabase) {
        this.developerDatabase = developerDatabase;
    }

    public void setDeveloperDatabaseTable(DeveloperDatabaseTable developerDatabaseTable) {
        this.developerDatabaseTable = developerDatabaseTable;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}