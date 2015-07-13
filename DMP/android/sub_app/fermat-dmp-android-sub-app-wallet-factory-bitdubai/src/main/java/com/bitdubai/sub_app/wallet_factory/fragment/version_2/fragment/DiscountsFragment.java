package com.bitdubai.sub_app.wallet_factory.fragment.version_2.fragment;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitdubai.sub_app.wallet_factory.R;
import com.bitdubai.sub_app.wallet_factory.common.MyApplication;


public class DiscountsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<DiscountsFragment.Model>> {

    CustomArrayAdapter mAdapter;

    private static final String ARG_POSITION = "position";

    private int position;

    public static DiscountsFragment newInstance(int position) {
        DiscountsFragment f = new DiscountsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }



    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("DiscountsFragment.onActivityCreated");

        // Initially there is no data
        setEmptyText("No Data Here");

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new CustomArrayAdapter(getActivity());
        setListAdapter(mAdapter);

        // Start out with a progress indicator.
        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Model>> onCreateLoader(int arg0, Bundle arg1) {
        System.out.println("DiscountsFragment.onCreateLoader");
        return new DataListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Model>> arg0, List<Model> data) {
        mAdapter.setData(data);
        System.out.println("DiscountsFragment.onLoadFinished");
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Model>> arg0) {
        mAdapter.setData(null);
    }




    public static class DataListLoader extends AsyncTaskLoader<List<Model>> {

        List<Model> mModels;

        public DataListLoader(Context context) {
            super(context);
        }

        @Override
        public List<Model> loadInBackground() {
            System.out.println("DataListLoader.loadInBackground");

            // You should perform the heavy task of getting data from
            // Internet or database or other source
            // Here, we are generating some Sample data

            // Create corresponding array of entries and load with data.
            List<Model> entries = new ArrayList<Model>(5);
            entries.add(new Model("Java", "2"));
            entries.add(new Model("C++", "9"));
            entries.add(new Model("Python", "6"));


            return entries;
        }

        /**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
        @Override public void deliverResult(List<Model> listOfData) {
            if (isReset()) {
                // An async query came in while the loader is stopped.  We
                // don't need the result.
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<Model> oldApps = listOfData;
            mModels = listOfData;

            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                super.deliverResult(listOfData);
            }

            // At this point we can release the resources associated with
            // 'oldApps' if needed; now that the new result is delivered we
            // know that it is no longer in use.
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override protected void onStartLoading() {
            if (mModels != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mModels);
            }


            if (takeContentChanged() || mModels == null) {
                // If the data has changed since the last time it was loaded
                // or is not currently available, start a load.
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * Handles a request to cancel a load.
         */
        @Override public void onCanceled(List<Model> apps) {
            super.onCanceled(apps);

            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }

        /**
         * Handles a request to completely reset the Loader.
         */
        @Override protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            // At this point we can release the resources associated with 'apps'
            // if needed.
            if (mModels != null) {
                onReleaseResources(mModels);
                mModels = null;
            }
        }

        /**
         * Helper function to take care of releasing resources associated
         * with an actively loaded data set.
         */
        protected void onReleaseResources(List<Model> apps) {}

    }





    public class CustomArrayAdapter extends ArrayAdapter<Model> {
        private final LayoutInflater mInflater;

        public CustomArrayAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<Model> data) {
            clear();
            if (data != null) {
                for (Model appEntry : data) {
                    add(appEntry);
                }
            }
        }

        /**
         * Populate new items in the list.
         */
        @Override public View getView(int position, View convertView, ViewGroup parent) {
            String[] account_types;
            String[] balances;
            String[] balances_available;
            String[] account_aliases;


            account_types = new String[]{"This week discounts", "This month discounts", "This year discounts"};
            account_aliases = new String[]{"My savings", "Pocket money", "Holiday savings"};
            balances = new String[]{"$129.00", "$735.20", "$3,211.80"};
            balances_available = new String[]{"2.36% average", "3.32 % average", "9.05 % average"};

            View view;

            //if (convertView == null) {
            view = mInflater.inflate(R.layout.wallets_factory_fragment_discounts_list_item, parent, false);
            //} else {
            //    view = convertView;
            //}

            ImageView account_picture;

            account_picture = (ImageView) view.findViewById(R.id.account_picture);

            switch (position)
            {
                case 0:
                    account_picture.setImageResource(R.drawable.discounts_week);
                    break;
                case 1:
                    account_picture.setImageResource(R.drawable.discounts_month);
                    break;
                case 2:
                    account_picture.setImageResource(R.drawable.discounts_year);
                    break;
            }

            TextView tv;

            tv = ((TextView)view.findViewById(R.id.balance));
            tv.setText(balances[position]);
            tv.setTypeface(MyApplication.getDefaultTypeface());

            tv = ((TextView)view.findViewById(R.id.balance_available));
            tv.setText(balances_available[position]);
            tv.setTypeface(MyApplication.getDefaultTypeface());



            tv = ((TextView)view.findViewById(R.id.account_type));
            tv.setText(account_types[position]);
            tv.setTypeface(MyApplication.getDefaultTypeface());

            return view;
        }
    }


    public static class Model {

        private String name;
        private String id;

        public Model(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

}

