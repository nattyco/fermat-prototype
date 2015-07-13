package com.bitdubai.android_core.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_core.app.common.version_1.tabbed_dialog.PagerSlidingTabStrip;

import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.AvailableBalanceFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ChatOverTransactionFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ChatWithContactFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ContactsFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.DailyDiscountsFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ReceiveAllFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ReceiveFromNewContactFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.SendAllFragment;
import com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.SendToNewContactFragment;


import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.App;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.AppRuntimeManager;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.Fragment;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.MainMenu;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.SideMenu;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.SubApp;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.TabStrip;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.TitleBar;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.enums.Fragments;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_runtime.WalletRuntimeManager;
import com.bitdubai.fermat_core.CorePlatformContext;
import com.bitdubai.fermat_dmp_plugin.layer.module.wallet_runtime.developer.bitdubai.version_1.structure.RuntimeFragment;
import com.bitdubai.fermat.R;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Natalia on 24/02/2015.
 */
public class FragmentActivity  extends Activity {


    // TODO: Raul: Esto no se de donde salio y para que se usa. Posiblemente tenga que volar .. Luis.

    public CharSequence Title;
    private Menu menu;
    private PagerSlidingTabStrip tabStrip;
    private App app;
    private SubApp subApp;
    private com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.Activity activity;
    private Map<Fragments, Fragment> fragments;

    private AppRuntimeManager appRuntimeMiddleware;
    private static WalletRuntimeManager walletRuntimeMiddleware;

    private CorePlatformContext platformContext;
    private ViewPager pager;

    private  TextView abTitle;
    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private MainMenu mainMenumenu;
    private SideMenu sidemenu;
    private String walletStyle = "";
    private TabStrip tabs;
    private TitleBar titleBar; // Comment
    private String tagParam  = ApplicationSession.getChildId(); //get param with data for fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtime_app_activity_fragment);
        try{
            // get instances of Runtime middleware object

            this.appRuntimeMiddleware =  ApplicationSession.getAppRuntime(); //(AppRuntimeManager)platformContext.getPlugin(Plugins.BITDUBAI_APP_RUNTIME_MIDDLEWARE);

            this.app = appRuntimeMiddleware.getLastApp();
            this.subApp = appRuntimeMiddleware.getLastSubApp();

            walletRuntimeMiddleware = ApplicationSession.getwalletRuntime();

            //get actual activity to execute
            this.activity = walletRuntimeMiddleware.getLasActivity();


            // Fragment fragment = appRuntimeMiddleware.getLastFragment();
            ApplicationSession.setActivityId(activity.getType().getKey());

            //get activity settings
            this.tabs = activity.getTabStrip();
            this.fragments =activity.getFragments();
            this.titleBar = activity.getTitleBar();

            this.mainMenumenu= activity.getMainMenu();



            if(fragments.size() == 1){
                List<android.support.v4.app.Fragment> fragments = new Vector<android.support.v4.app.Fragment>();
                Iterator<Map.Entry<Fragments, Fragment>> efragments = this.fragments.entrySet().iterator();

                while (efragments.hasNext()) {
                    Map.Entry<Fragments, Fragment> fragmentEntry = efragments.next();

                    RuntimeFragment fragment = (RuntimeFragment) fragmentEntry.getValue();
                    Fragments type = fragment.getType();
                    switch (type) {

                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS:
                            if (savedInstanceState == null) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.container, new ContactsFragment())
                                        .commit();
                            }
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_CHAT:
                            if (savedInstanceState == null) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.container, new ChatWithContactFragment())
                                        .commit();
                            }
                            break;
                        case CWP_WALLET_RUNTIME_ADULTS_ALL_AVAILABLE_BALANCE:
                            if (savedInstanceState == null) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.container, new AvailableBalanceFragment())
                                        .commit();
                            }
                            break;
                        case  CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_SEND:
                            if (savedInstanceState == null) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.container, new SendToNewContactFragment())
                                        .commit();
                            }
                            break;
                        case  CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_RECEIVE:
                            if (savedInstanceState == null) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.container, new ReceiveFromNewContactFragment())
                                        .commit();
                            }
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_SEND :
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.SendToContactFragment())
                                    .commit();
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_RECEIVE:
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment.ReceiveFromContactFragment())
                                    .commit();
                            break;
                        case CWP_SHOP_MANAGER_MAIN:
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS:
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS:
                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL:
                            break;
                        case CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED:
                            break;
                        case CWP_WALLET_ADULTS_ALL_REQUEST_SEND:
                            break;
                        case CWP_WALLET_ADULTS_ALL_SEND_HISTORY:
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new SendAllFragment())
                                    .commit();

                            break;
                        case CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED_HISTORY:
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new ReceiveAllFragment())
                                    .commit();

                            break;
                        case CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CHAT_TRX:
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new ChatOverTransactionFragment())
                                    .commit();
                            break;
                        case CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT:
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, new DailyDiscountsFragment())
                                    .commit();
                            break;
                        case CWP_WALLET_STORE_MAIN:
                            break;
                        case CWP_WALLET_FACTORY_MAIN:
                            break;

                    }
                }

            }

            int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
            this.abTitle = (TextView) findViewById(titleId);

            ApplicationSession.setActivityProperties(this, getWindow(), getResources(), tabStrip, getActionBar(), titleBar, abTitle, Title);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Can't Create Fragment: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        switch ( this.activity.getType()) {

            case CWP_SHELL_LOGIN:
                break;
            case CWP_SHOP_MANAGER_MAIN:

                break;
            case CWP_WALLET_MANAGER_MAIN:
                break;
            case CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_VERSION_1_MAIN:

                break;

            case CWP_WALLET_RUNTIME_STORE_MAIN:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_AVAILABLE_BALANCE:
                getMenuInflater().inflate(R.menu.wallet_framework_activity_available_balance_menu, menu);

            case CWP_WALLET_RUNTIME_ADULTS_ALL_MAIN:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_ACCOUNTS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_BANKS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_COUPONS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_DISCOUNTS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_VOUCHERS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_GIFT_CARDS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_CLONES:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_CHILDS:
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS:
                getMenuInflater().inflate(R.menu.wallet_framework_activity_contacts_menu, menu);
                break;
            case CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_CHAT:
                getMenuInflater().inflate(R.menu.wallet_framework_activity_sent_all_menu, menu);

            case CWP_WALLET_ADULTS_ALL_SHOPS:

                break;
            case CWP_WALLET_ADULTS_ALL_REFFILS:
                break;
            case CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED:
                break;
            case CWP_WALLET_ADULTS_ALL_REQUEST_SEND:
                break;
            case CWP_WALLET_FACTORY_MAIN:

                break;
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
