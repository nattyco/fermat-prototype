package com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment;

import android.graphics.Color;
import android.os.Bundle;

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
 * Created by Natalia on 23/12/2014.
 */
public class SendAllFragment extends android.app.Fragment {

    private static final String ARG_POSITION = "position";

    View rootView;
    ExpandableListView lv;
    private String[] contacts;
    private String[] amounts;
    private String[] totalAmount;
    private String[] historyCount;
    private String[] whens;
    private String[] notes;
    private String[] pictures;
    private String[][] transactions;
    String[][] transactions_amounts;
    private String[][] transactions_whens;


    public static SendAllFragment newInstance() {
        SendAllFragment f = new SendAllFragment();
        return f;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /* Custom Action Bar with Icon and Text */
        String[] contacts = new String[]{ "", "Luis Fernando Molina", "Guillermo Villanueva", "Pedro Perrotta", "Mariana Duyos"};

        final ViewGroup actionBarLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(
                R.layout.wallet_framework_activity_sent_history_action_bar,
                null);

        // Set up your ActionBar


        int tagId = ApplicationSession.getTagId();
        TextView tv;

        tv = (TextView) actionBarLayout.findViewById(R.id.contact_name);
        tv.setTypeface(ApplicationSession.getDefaultTypeface());
        tv.setText(contacts[tagId].toString());

        tv = (TextView) actionBarLayout.findViewById(R.id.activity_name);
        tv.setTypeface(ApplicationSession.getDefaultTypeface());

        ApplicationSession.changeColor(Color.parseColor("#F0E173"), super.getActivity().getResources());

        ImageView profile_picture = (ImageView) actionBarLayout.findViewById(R.id.profile_picture);
        switch (tagId)
        {
            case 1:
                profile_picture.setImageResource(R.drawable.luis_profile_picture);

                break;
            case 2:
                profile_picture.setImageResource(R.drawable.guillermo_profile_picture);

                break;
            case 3:
                profile_picture.setImageResource(R.drawable.pedro_profile_picture);
                break;
            case 4:
                profile_picture.setImageResource(R.drawable.mariana_profile_picture);

                break;
        }
//find de tag id,
        contacts = new String[]{ "", "Luis Fernando Molina", "Guillermo Villanueva", "Pedro Perrotta", "Mariana Duyos"};

        amounts = new String[]{ "", "$325.00", "$1,400.00", "$0.50", "$25.00"};

        transactions = new String[][]{
                {},
                 {"Electricity bill","New chair","New desk"},
                {"Flat rent","Flat rent","Flat rent","interest paid :(","Flat rent","Car repair","Invoice #2,356 that should have been paid on August"},
                {"Test address"},
                {"More pictures"}
        };

        transactions_amounts = new String[][]{
                {},
  {"$325.00","$55.00","$420.00"},
                {"$1,400.00","$1,200.00","$1,400.00","$40.00","$1,900.00","$10,550.00","$1.00"},
                {"$0.50"},
                {"$25.00"}
        };

        transactions_whens = new String[][]{
                {},
                {"3 min ago","15 min ago","yesterday"},
                {"2 hours ago","yesterday","last Friday","last Friday","14 May 14","11 May 14","5 Jan 14"},
                {"today 9:24 AM"},
                {"yesterday"}
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallets_teens_fragment_send_all, container, false);
        int tagId = ApplicationSession.getTagId();

        amounts = transactions_amounts[tagId];
        whens = transactions_whens[tagId];
        notes = transactions[tagId];

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        lv.setAdapter(new ExpandableListAdapter(notes, transactions));
        lv.setGroupIndicator(null);

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] notes;
        private String[][] transactions;

        public ExpandableListAdapter(String[] notes, String[][] transactions) {
            this.notes = notes;
            this.transactions = transactions;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        public int getGroupCount() {
            return notes.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return transactions[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return notes[groupPosition];
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


            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ViewHolder amount;
            ViewHolder when;
            ViewHolder note;

            int tagId = ApplicationSession.getTagId();
            if (groupPosition == 0)
            {
                convertView = inf.inflate(R.layout.wallets_teens_fragment_send_all_list_header, parent, false);

                ImageView  send_profile_picture = (ImageView) convertView.findViewById(R.id.icon_send_to_contact);
                send_profile_picture.setTag("SendToContactActivity|" + tagId + "|" + groupPosition);



                holder = new ViewHolder();

                amount = new ViewHolder();
                amount.text = (TextView) convertView.findViewById(R.id.amount);
                amount.text.setTypeface(ApplicationSession.getDefaultTypeface());

                amount.text.setText(amounts[groupPosition].toString());

                when = new ViewHolder();
                when.text = (TextView) convertView.findViewById(R.id.when);
                when.text.setTypeface(ApplicationSession.getDefaultTypeface());

                when.text.setText(whens[groupPosition].toString());

                note = new ViewHolder();
                note.text = (TextView) convertView.findViewById(R.id.notes);
                note.text.setTypeface(ApplicationSession.getDefaultTypeface());



            }
            else {

                //*** Seguramente por una cuestion de performance lo hacia asi, yo lo saque para que ande el prototippo
                // if (convertView == null) {
                if (1 == 1) {
                    convertView = inf.inflate(R.layout.wallets_teens_fragment_send_all_list_header, parent, false);

                    ImageView  send_profile_picture = (ImageView) convertView.findViewById(R.id.icon_send_to_contact);
                    send_profile_picture.setTag("SendToContactActivity|" +tagId + "|" + groupPosition);

                    ImageView  icon_chat_over_trx = (ImageView) convertView.findViewById(R.id.icon_chat_over_trx);
                    icon_chat_over_trx.setTag("ChatOverTrxActivity|" + tagId + "|" + groupPosition);

                    holder = new ViewHolder();

                    amount = new ViewHolder();
                    amount.text = (TextView) convertView.findViewById(R.id.amount);
                    amount.text.setTypeface(ApplicationSession.getDefaultTypeface());

                    amount.text.setText(amounts[groupPosition].toString());

                    when = new ViewHolder();
                    when.text = (TextView) convertView.findViewById(R.id.when);
                    when.text.setTypeface(ApplicationSession.getDefaultTypeface());

                    when.text.setText(whens[groupPosition].toString());

                    note = new ViewHolder();
                    note.text = (TextView) convertView.findViewById(R.id.notes);
                    note.text.setTypeface(ApplicationSession.getDefaultTypeface());

                    note.text.setText(notes[groupPosition]);



                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

               // holder.text.setText(getGroup(groupPosition).toString());
            }

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