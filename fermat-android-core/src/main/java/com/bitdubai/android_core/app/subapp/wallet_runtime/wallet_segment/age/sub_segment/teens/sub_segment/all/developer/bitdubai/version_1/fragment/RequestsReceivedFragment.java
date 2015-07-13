package com.bitdubai.android_core.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment;

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


public  class RequestsReceivedFragment extends android.app.Fragment {

    private static final String ARG_POSITION = "position";

    View rootView;
    ExpandableListView lv;
    private String[] contacts;
    private String[] amounts;
    private String[] whens;
    private String[] notes;
    private String[] pictures;
    private String[][] transactions;
    private String[][] transactions_amounts;
    private String[][] transactions_whens;


    public static RequestsReceivedFragment newInstance() {
        RequestsReceivedFragment f = new RequestsReceivedFragment();
        return f;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts = new String[]{"Taylor Backus","Stephanie Himonidis","Kimberly Brown","Ginny Kaltabanis","Piper Faust","Deniz Caglar","Helen Nisbet","Dea Vanagan","Tim Hunter","Madeleine Jordan","Kate Bryan","Victoria Gandit","Jennifer Johnson","Robert Wint","Adrian Smith","Florence Kerns",};
        amounts = new String[]{ "$3.00","$290.00","$600.00","$50.00","$30.00","$500.00","$25.00","$250.00","$75.00","$300.00","$5.00","$100.00","$45.00","$35.00","$40.00"};
        whens = new String[]{ "24 Mar 14","3 Feb 14","1 year ago","1 year ago","2 years ago","2 years ago","2 years ago","3 years ago","3 years ago","3 years ago","3 years ago","4 years ago","4 years ago","5 years ago","5 years ago"};
        notes = new String[]{ "sandwich","conference ticket","computer monitor","bag","computer keyboard","new tv","t-shirt","pendrive","shoes","bed","color pencils","speakers","microphone","magazine","book"};


        //pictures = new String[]{"luis_profile_picture", "guillermo_profile_picture", "pedro_profile_picture", "mariana_profile_picture"};

        transactions = new String[][]{

                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
        };
        transactions_amounts = new String[][]{

                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},

        };

        transactions_whens = new String[][]{

                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},


        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallets_teens_multiple_fragments_expandable_list_view, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        lv.setAdapter(new ExpandableListAdapter(contacts, transactions));
        lv.setGroupIndicator(null);

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater mInflater;
        private String[] contacts;
        private String[][] transactions;

        public ExpandableListAdapter(String[] contacts, String[][] transactions) {
            this.contacts = contacts;
            this.transactions = transactions;
            mInflater = LayoutInflater.from(getActivity());
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


            return convertView;
        }




        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ViewHolder amount;
            ViewHolder when;
            ViewHolder note;
            ImageView profile_picture;
            ImageView account_picture;


            TextView tv;

            switch (groupPosition)
            {
                case 0:
                    convertView = mInflater.inflate(R.layout.wallets_teens_multiple_fragments_titles_list_item, parent, false);

                    tv = ((TextView)convertView.findViewById(R.id.title));
                    tv.setText("Requests waiting to be accepted");
                    tv.setTypeface(ApplicationSession.getDefaultTypeface());

                    break;


                case 5:
                    convertView = mInflater.inflate(R.layout.wallets_teens_multiple_fragments_titles_list_item, parent, false);

                    tv = ((TextView)convertView.findViewById(R.id.title));
                    tv.setText("Requests already accepted");
                    tv.setTypeface(ApplicationSession.getDefaultTypeface());

                    break;

                case 1: case 2: case 3:case 4 :case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:

                convertView = mInflater.inflate(R.layout.wallets_teens_multiple_fragments_request_received_list_item, parent, false);
                account_picture = (ImageView) convertView.findViewById(R.id.profile_picture);


                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.contact_name);
                holder.text.setTypeface(ApplicationSession.getDefaultTypeface());
                holder.text.setText(contacts[groupPosition].toString());

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

                note.text.setText(notes[groupPosition].toString());

                switch (groupPosition)
                {

                    case 1:
                        account_picture.setImageResource(R.drawable.stephani_profile_picture);
                        break;
                    case 2:
                        account_picture.setImageResource(R.drawable.kimberly_profile_picture);
                        break;
                    case 3:
                        account_picture.setImageResource(R.drawable.ginny_profile_picture);
                        break;
                    case 4:
                        account_picture.setImageResource(R.drawable.piper_profile_picture);
                        break;
                    case 5:
                        account_picture.setImageResource(R.drawable.deniz_profile_picture);
                        break;
                    case 6:
                        account_picture.setImageResource(R.drawable.helen_profile_picture);
                        break;
                    case 7:
                        account_picture.setImageResource(R.drawable.dea_profile_picture);
                        break;
                    case 8:
                        account_picture.setImageResource(R.drawable.tim_profile_picture);
                        break;
                    case 9:
                        account_picture.setImageResource(R.drawable.alexandra_profile_picture);
                        break;
                    case 10:
                        account_picture.setImageResource(R.drawable.madaleine_profile_picture);
                        break;
                    case 11:
                        account_picture.setImageResource(R.drawable.kate_profile_picture);
                        break;
                    case 12:
                        account_picture.setImageResource(R.drawable.victoria_profile_picture);
                        break;
                    case 13:
                        account_picture.setImageResource(R.drawable.richard_profile_picture);
                        break;
                    case 14:
                        account_picture.setImageResource(R.drawable.robert_profile_picture);
                        break;
                    case 15:
                        account_picture.setImageResource(R.drawable.adrian_profile_picture);
                        break;






                }


                if (groupPosition > 5 )
                {
                    ImageView iv;
                    iv = (ImageView) convertView.findViewById(R.id.action_accept);
                    iv.setVisibility(View.INVISIBLE);
                    iv = (ImageView) convertView.findViewById(R.id.action_cancel);
                    iv.setVisibility(View.INVISIBLE);
                }

                break;


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