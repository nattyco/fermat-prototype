package com.bitdubai.android_core.app.subapp.shop.version_1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitdubai.android_core.app.ApplicationSession;
import com.bitdubai.fermat.R;

/**
 * Created by Natalia on 09/01/2015.
 */
public class ShopHistoryFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    View rootView;
    ExpandableListView lv;
    private String[] contacts;
    private String[] amounts;
    private String[] totalAmount;
    private String[] dates;
    private String[] historyCount;
    private String[][] names;
    private String[] client;
    private String[] whens;
    private String[] notes;
    private String[][] items;
    private String[] pictures;
    private String[][] transactions;
    String[][] transactions_amounts;
    private String[][] transactions_whens;



    public static ShopHistoryFragment newInstance(int position) {
        ShopHistoryFragment f = new ShopHistoryFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts = new String[]{"14 units"          ,"5 units",          "6",                "12 units",           "8 units",            "24 units"};
        amounts = new String[]{"$12.00"             ,"$32.00",           "$9.00",            "$21.50",             "$12.00",             "$34.00"};
        whens = new String[]{"brand discount"       ,"brand discount",   "brand discount",   "brand discount",     "brand discount",     "brand discount",};
        notes = new String[]{ "total paid "         ,"total paid",       "total paid "   ,   "total paid ",        "total paid ",        "total paid "};
        totalAmount = new String[]{"$16.00"         ,"$37.50",           "$7.00",            "$24.00",             "$15.50",             "$36.00"};
        historyCount = new String[] {"-$4.00"       ,"-$5.50",           "-$2.00",           "-$2.50",             "-$2.50",             "-$2.00"};
        pictures = new String[]{"product_14_history","product_3_history","product_6_history","product_8_history",  "product_13_history", "product_2_history"};
        dates = new String[]{"today"                ,"today",            "today",            "today",              "today",              "today"};
        client = new String[]{"Victoria Gandit"     ,"Mariana Duyos",    "Jennifer Johnson" ,"Teddy Truchot",      "Caroline Mignaux",   "Brant Cryder"};



        transactions = new String[][]{
                {"$3.00","$2.00"},
                {"$7.50"},
                {"$2.00","$1.00"},
                {"$2.00","$1.00","$1.00"},
                {"$2.50","$2.00"},
                {"$1.50"},
        };

        items = new String[][]{
                {"2 units","12 units"},
                {"5 units"},
                {"3 units", "3 units"},
                {"4 units","4 units","4 units"},
                {"4 units","4 units"},
                {"24 units"}
        };

        names = new String[][]{

                {"French Roll","Chocolate chips"},
                {"French Roll "},
                {"Peanut Butter Combo","Classic Glazed Strawberry"},
                {"Caramel Chocolate Crunch","Chocolate with sparkles","Classic Glazed Chocolate"},
                {"Cinnamon Cake","Honey bran raisins"},
                {"Classic Iced Pink"},
        };

        transactions_amounts = new String[][]{
                {"$6.00","$24.00"},
                {"$37.50"},
                {"$6.00","$3.00"},
                {"$8.00","$4.00","$4.00"},
                {"$10.00","$8.00"},
                {"$36.00"},
        };

        transactions_whens = new String[][]{

                {"Quantity","Quantity"},
                {"Quantity",},
                {"Quantity",},
                {"Quantity","Quantity","Quantity"},
                {"Quantity","Quantity"},
                {"Quantity",},
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shop_fragment_history_store, container, false);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        lv.setAdapter(new ExpandableListAdapter(contacts, transactions));
        lv.setGroupIndicator(null);

        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
/*
                Intent intent;
                intent = new Intent(getActivity(), SentDetailActivity.class);
                startActivity(intent);*/
                return false;
            }
        });

        lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
/*
                if (groupPosition == 0) {
                    Intent intent;
                    intent = new Intent(getActivity(), SendToNewContactActivity.class);
                    startActivity(intent);
                    return false;
                }
                else*/
                {
                    return false;
                }
            }
        });


    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] contacts;
        private String[][] transactions;

        public ExpandableListAdapter(String[] contacts, String[][] transactions) {
            this.contacts = contacts;
            this.transactions = transactions;
            inf = LayoutInflater.from(getActivity());
        }


        @Override
        public int getGroupCount() {
            return contacts.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return transactions[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return contacts[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return transactions[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder holder;
            ViewHolder amount;
            ViewHolder when;
            ViewHolder item;
            ViewHolder name;
            ImageView profile_picture;


            //*** Seguramente por una cuestion de performance lo hacia asi, yo lo saque para que ande el prototippo
            // if (convertView == null) {
            if (1 == 1) {
                convertView = inf.inflate(R.layout.shop_fragment_history_list_detail, parent, false);

                profile_picture = (ImageView) convertView.findViewById(R.id.profile_picture);
                switch (groupPosition)
                {
                    case 0:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_14_history);
                                break;
                            case 1:
                                profile_picture.setImageResource(R.drawable.product_11_history);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_14_history);
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_6_history);
                                break;
                            case 1:
                                profile_picture.setImageResource(R.drawable.product_4_history);
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_8_history);
                                break;
                            case 1:
                                profile_picture.setImageResource(R.drawable.product_9_history);
                                break;
                            case 2:
                                profile_picture.setImageResource(R.drawable.product_3_history);
                                break;
                        }

                        break;
                    case 4:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_13_history);
                                break;
                            case 1:
                                profile_picture.setImageResource(R.drawable.product_12_history);
                                break;
                        }
                        break;
                    case 5:
                        switch (childPosition)
                        {
                            case 0:
                                profile_picture.setImageResource(R.drawable.product_2_history);
                                break;
                        }
                        break;
                }

                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.notes);
                holder.text.setTypeface(ApplicationSession.getDefaultTypeface());
                convertView.setTag(holder);

                amount = new ViewHolder();
                amount.text = (TextView) convertView.findViewById(R.id.amount);
                amount.text.setTypeface(ApplicationSession.getDefaultTypeface());

                amount.text.setText(transactions_amounts[groupPosition][childPosition].toString());

                name = new ViewHolder();
                name.text = (TextView) convertView.findViewById(R.id.contact_name);
                name.text.setTypeface(ApplicationSession.getDefaultTypeface());
                name.text.setText(names[groupPosition][childPosition].toString());

                item = new ViewHolder();
                item.text = (TextView) convertView.findViewById(R.id.name);
                item.text.setTypeface(ApplicationSession.getDefaultTypeface());
                item.text.setText(items[groupPosition][childPosition].toString());



            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getChild(groupPosition, childPosition).toString());

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ViewHolder amount;
            ViewHolder when;
            ViewHolder note;
            ImageView profile_picture;
            ImageView send_picture;
            ViewHolder total;
            ViewHolder date;
            ViewHolder clients;
            ViewHolder history;



            //*** Seguramente por una cuestion de performance lo hacia asi, yo lo saque para que ande el prototippo
            // if (convertView == null) {
            if (1 == 1) {
                convertView = inf.inflate(R.layout.shop_fragment_history_list_header, parent, false);

                profile_picture = (ImageView) convertView.findViewById(R.id.profile_picture);

                switch (groupPosition)
                {
                    case 0:
                        profile_picture.setImageResource(R.drawable.victoria_profile_picture);
                        break;
                    case 1:
                        profile_picture.setImageResource(R.drawable.mariana_profile_picture);
                        break;
                    case 2:
                        profile_picture.setImageResource(R.drawable.jennifer_profile_picture);
                        break;
                    case 3:
                        profile_picture.setImageResource(R.drawable.teddy_profile_picture);
                        break;
                    case 4:
                        profile_picture.setImageResource(R.drawable.caroline_profile_picture);
                        break;
                    case 5:
                        profile_picture.setImageResource(R.drawable.brant_profile_picture);
                        break;
                }





                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.contact_name);
                holder.text.setTypeface(ApplicationSession.getDefaultTypeface());
                convertView.setTag(holder);

                date = new ViewHolder();
                date.text = (TextView) convertView.findViewById(R.id.date);
                date.text.setTypeface(ApplicationSession.getDefaultTypeface());
                date.text.setText(dates[groupPosition].toString());

                amount = new ViewHolder();
                amount.text = (TextView) convertView.findViewById(R.id.amount);
                amount.text.setTypeface(ApplicationSession.getDefaultTypeface());
                amount.text.setText(amounts[groupPosition].toString());

                clients = new ViewHolder();
                clients.text = (TextView) convertView.findViewById(R.id.client_name);
                clients.text.setTypeface(ApplicationSession.getDefaultTypeface());
                clients.text.setText(client[groupPosition].toString());

                when = new ViewHolder();
                when.text = (TextView) convertView.findViewById(R.id.when);
                when.text.setTypeface(ApplicationSession.getDefaultTypeface());
                when.text.setText(whens[groupPosition].toString());

                total = new ViewHolder();
                total.text = (TextView) convertView.findViewById(R.id.total_amount);
                total.text.setTypeface(ApplicationSession.getDefaultTypeface());
                total.text.setText(totalAmount[groupPosition].toString());

                history = new ViewHolder();
                history.text = (TextView) convertView.findViewById(R.id.history_count);
                history.text.setTypeface(ApplicationSession.getDefaultTypeface());
                history.text.setText(historyCount[groupPosition].toString());

                note = new ViewHolder();
                note.text = (TextView) convertView.findViewById(R.id.notes);
                note.text.setTypeface(ApplicationSession.getDefaultTypeface());
                note.text.setText(notes[groupPosition].toString());

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getGroup(groupPosition).toString());




            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        private class ViewHolder {
            TextView text;
        }
    }



}


