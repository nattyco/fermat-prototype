package com.bitdubai.android_core.app.subapp.shop.version_1.fragment;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bitdubai.fermat.R;
import com.bitdubai.android_core.app.ApplicationSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natalia on 09/01/2015.
 */
public class ShopProductsFragment extends Fragment {


    private static final String ARG_POSITION = "position";
    private ArrayList<App> mlist;

    private int position;

    public static ShopProductsFragment newInstance(int position) {
        ShopProductsFragment f = new ShopProductsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        String[] company_names = {"Sweet Heart",
                "Classic Iced Pink",
                "Classic Glazed Chocolate",
                "Classic Glazed Strawberry",
                "Chocolate Custard Filled",
                "Peanut Butter Combo",
                "Caramel Kreme Crunch",
                "Caramel Chocolate Crunch",
                "Chocolate With Sprinkles",
                "Authentic Goodness",
                "Chocolate Chip",
                "Honey Bran Raisin",
                "Cinnamon Cake",
                "French Roll",
                "",
                "",
                "",
                "",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa" };
        String[] company_addresses = {"Donut",
                "Donut",
                "Donut",
                "Donut",
                "Donut",
                "Donut",
                "Donut",
                "Donut",
                "Donut",
                "Bagels",
                "Cookies",
                "Muffins",
                "Munchkins",
                "Other Bakery",
                "5 E 38th St",
                "27 W 38th St",
                "72 W 38th St",
                "1271 Broadway #1",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa" };
        String[] company_horario = {"Today 8:00 am – 9:00 pm",
                "Today 10:30 am – 8:00 pm",
                "Today 11:00 am – 10:30 pm",
                "Today 11:00 am – 10:30 pm",
                "Today 11:30 am – 11:00 pm",
                "Today 9:30 am – 7:30 pm",
                "Today Open 24 hours",
                "Today Open 24 hours",
                "Today 5:30 am – 10:00 pm",
                "Today 11:30 am – 12:00 am",
                "Today 6:00 am – 10:00 pm",
                "Today 11:30 am – 11:00 pm",
                "Today 6:30 am – 10:30 pm",
                "Today 8:00 am – 9:00 pm",
                "Today 11:30 am – 10:00 pm",
                "Today 11:30 am – 10:30 pm",
                "Today 7:00 am – 7:00 pm",
                "Today Open 24 hours",
                "Today",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa" };
        String[] company_telefono = {"+1 800-671-4332",
                "+1 212-714-0858",
                "+1 212-594-9462",
                "+1 212-239-7019",
                "+1 646-964-4964",
                "+1 212-792-6999",
                "+1 212-268-8444",
                "+1 212-971-0101",
                "+1 212-395-9280",
                "+1 212-869-8601",
                "+1 212-938-6950",
                "+1 212-354-5522",
                "+1 212-401-8686",
                "+1 212-719-4044",
                "+1 212-679-0646",
                "+1 212-944-0990",
                "+1 212-302-0200",
                "+1 212-683-8997",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa" };
        String[] company_descriptions = {"Classic, redefined. See what's new at our Westchester location!.",
                "Smoking is allowed inside this clubby shop known for its hand-rolled cigars made ...",
                "Basic pizzeria serving a variety of NYC-style slices in a small, counter-service space.",
                "Pasta dishes, sandwiches & salads for take-out or eating in at this pint-sized Italian deli.",
                "Sophisticated Greek dishes, happy hour small plates & organic boutique wines in a ...",
                "Hair Salon, Day Spa",
                "Standard 24/7 diner with retro looks, basic fare & close proximity to Madison Square...",
                "Huge historic Jazz Age (1929) hotel offers over 900 rooms and suites across from Penn Station.",
                "Long-running chain serving signature donuts, breakfast sandwiches & a variety of coffee ...",
                "Home-away-from-home Australian pub offering grilled kangaroo, Aussie beers & rugby ...",
                "Counter-serve bakery/cafe chain serving sandwiches, salads & more, known for its ...",
                "Counter-serve setup offering deli sandwiches & wraps, hot entrees, salads & breakfast items.",
                "Counter-serve chain for ready-made sandwiches plus breakfast, coffee, soups & ...",
                "Casual counter-serve chain for build-your-own sandwiches & salads, with health-conscious ...",
                "Buzzy after-work-geared bar/eatery serving drinks & American fare in a cozy, ... ",
                "A bar pours mojitos up front & a brick-lined dining room in the back serves traditional ...",
                "Tiny, art-adorned coffeehouse pairing its brews with baked goods, sandwiches & a ...",
                "All-day breakfast, sandwiches & other basic American fare are offered for takeout ...",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa" };
        String[] company_sites = {"dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "dunkindonuts.com",
                "butterfield8nyc.com",
                "havanany.com",
                "cultureespresso.com",
                "speedys.com",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa",
                "aaa", };
        String[] likes = {"614",
                "1346",
                "614",
                "34",
                "614",
                "234",
                "652",
                "1543",
                "234",
                "646",
                "859",
                "964",
                "456",
                "575",
                "324",
                "254",
                "334",
                "624",
                "275",
                "373",
                "735"};
        String[] dislikes = {"124",
                "132",
                "41",
                "43",
                "23",
                "54",
                "652",
                "53",
                "63",
                "43",
                "978",
                "667",
                "856",
                "57",
                "87",
                "55",
                "67",
                "99",
                "45",
                "85"};
        String[] price = {
                "$3.00",
                "$1.50",
                "$1.00",
                "$1.00",
                "$2.75",
                "$2,00",
                "$3.50",
                "$2.00",
                "$1.50",
                "$2.00",
                "$3.00",
                "$2.00",
                "$2.50",
                "$7.50",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""


        };



        if (mlist == null)
        {

            mlist = new ArrayList<App>();

            for (int i = 0; i < 14; i++) {
                App item = new App();
                item.title = company_sites[i];
                item.like = likes[i];
                item.dislike = dislikes[i];
                item.Price = price[i];
                item.description = company_descriptions[i];
                item.company = company_names[i];
                item.Open_hours = company_horario[i];
                item.Phone = company_telefono[i];
                item.Address = company_addresses[i];
                item.rate = (float) Math.random() * 5;
                item.value = (int) Math.floor((Math.random() * (500 - 80 + 1))) + 80;
                item.favorite = (float) Math.random() * 5;
                mlist.add(item);
            }
        }


        GridView gridView = new GridView(getActivity());

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(3);
        } else {
            gridView.setNumColumns(2);
        }

        //@SuppressWarnings("unchecked")
        //ArrayList<App> list = (ArrayList<App>) getArguments().get("list");
        gridView.setAdapter(new AppListAdapter(getActivity(), R.layout.shop_fragment_shop_item, mlist));





        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
             /*   Intent intent;
                intent = new Intent(getActivity(), StoreActivity.class);
                startActivity(intent);
*/
                return ;
            }
        });


        return gridView;
    }





    public class App implements Serializable {

        private static final long serialVersionUID = -8730067026050196758L;

        public String title;

        public String description;

        public String like;

        public String dislike;

        public String company;

        public String Open_hours;

        public String Address;

        public String Phone;

        public float rate;

        public int value;

        public float favorite;

        public String Price;

    }



    public class AppListAdapter extends ArrayAdapter<App> {


        public AppListAdapter(Context context, int textViewResourceId, List<App> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            App item = getItem(position);


            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.shop_fragment_shop_products_item, parent, false);
                holder = new ViewHolder();

                holder.dislike= (ImageView) convertView.findViewById(R.id.bad);
                holder.like= (ImageView) convertView.findViewById(R.id.good);

                holder.star1= (ImageView) convertView.findViewById(R.id.star_1);
                holder.star2= (ImageView) convertView.findViewById(R.id.star_2);
                holder.star3= (ImageView) convertView.findViewById(R.id.star_3);
                holder.star4= (ImageView) convertView.findViewById(R.id.star_4);
                holder.star5= (ImageView) convertView.findViewById(R.id.star_5);



                holder.star1.setAdjustViewBounds(true);
                holder.star2.setAdjustViewBounds(true);
                holder.star3.setAdjustViewBounds(true);
                holder.star4.setAdjustViewBounds(true);
                holder.star5.setAdjustViewBounds(true);

                holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);

                holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
                holder.Prices = (TextView) convertView.findViewById(R.id.price);
                holder.dislikeAmount = (TextView) convertView.findViewById(R.id.dislike_amount);
                holder.likeAmount = (TextView) convertView.findViewById(R.id.like_amount);
                holder.companyTextView = (TextView) convertView.findViewById(R.id.company_text_view);
                holder.companyDescription = (TextView) convertView.findViewById(R.id.company_description);
                //holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
                holder.valueTextView = (TextView) convertView.findViewById(R.id.value_text_view);

                holder.openHours = (TextView) convertView.findViewById(R.id.open_hours);
                holder.timeToArrive = (TextView) convertView.findViewById(R.id.time_to_arrive);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.dislikeAmount.setText(item.dislike);
            holder.likeAmount.setText(item.like);
            holder.titleTextView.setText(item.title);
            holder.Prices.setText(item.Price);
            holder.companyTextView.setText(item.company);
            holder.companyDescription.setText(item.Address);
            //holder.ratingBar.setRating(item.rate);
            holder.valueTextView.setText(  item.value + " reviews");

            holder.openHours.setText(  item.Open_hours);


            holder.timeToArrive.setText( position + " min");


            holder.openHours.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.timeToArrive.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.titleTextView.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.likeAmount.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.dislikeAmount.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.companyTextView.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.Prices.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.companyDescription.setTypeface(ApplicationSession.getDefaultTypeface());
            holder.valueTextView.setTypeface(ApplicationSession.getDefaultTypeface());

            if (item.rate >= 0)
            {
                holder.star1.setImageResource(R.drawable.grid_background_star_full);
            }
            if (item.rate >= 1)
            {
                holder.star2.setImageResource(R.drawable.grid_background_star_full);
            }
            if (item.rate >= 2)
            {
                holder.star3.setImageResource(R.drawable.grid_background_star_full);
            }
            if (item.rate >= 3)
            {
                holder.star4.setImageResource(R.drawable.grid_background_star_full);
            }
            if (item.rate >= 4)
            {
                holder.star5.setImageResource(R.drawable.grid_background_star_full);
            }

            if (item.favorite > 2)
            {
                holder.favorite.setImageResource(R.drawable.grid_background_favorite);
            }
            else
            {
                holder.favorite.setImageResource(R.drawable.grid_background_not_favorite);
            }


            switch (position)
            {
                case 3:
                    holder.like.setImageResource(R.drawable.ic_action_good_gold);
                    break;
                case 6:
                    holder.like.setImageResource(R.drawable.ic_action_good_gold);
                    break;
                case 9:
                    holder.dislike.setImageResource(R.drawable.ic_action_bad_gold);
                    break;

            }


            switch (position)
            {
                case 0:
                    holder.imageView.setImageResource(R.drawable.product_1);
                    break;
                case 1:
                    holder.imageView.setImageResource(R.drawable.product_2);
                    break;
                case 2:
                    holder.imageView.setImageResource(R.drawable.product_3);
                    break;
                case 3:
                    holder.imageView.setImageResource(R.drawable.product_4);
                    break;
                case 4:
                    holder.imageView.setImageResource(R.drawable.product_5);
                    break;
                case 5:
                    holder.imageView.setImageResource(R.drawable.product_6);
                    break;
                case 6:
                    holder.imageView.setImageResource(R.drawable.product_7);
                    break;
                case 7:
                    holder.imageView.setImageResource(R.drawable.product_8);
                    break;
                case 8:
                    holder.imageView.setImageResource(R.drawable.product_9);
                    break;
                case 9:
                    holder.imageView.setImageResource(R.drawable.product_10);
                    break;
                case 10:
                    holder.imageView.setImageResource(R.drawable.product_11);
                    break;
                case 11:
                    holder.imageView.setImageResource(R.drawable.product_12);
                    break;
                case 12:
                    holder.imageView.setImageResource(R.drawable.product_13);
                    break;
                case 13:
                    holder.imageView.setImageResource(R.drawable.product_14);
                    break;




            }


            return convertView;
        }

        /**
         * ViewHolder.
         */
        private class ViewHolder {


            public ImageView star1;
            public ImageView star2;
            public ImageView star3;
            public ImageView star4;
            public ImageView star5;

            public ImageView like;
            public ImageView dislike;

            public TextView Prices;

            public ImageView favorite;

            public TextView openHours;
            public TextView timeToArrive;
            public TextView Phone;
            public TextView Address;

            public ImageView imageView;

            public TextView likeAmount;

            public TextView dislikeAmount;

            public TextView titleTextView;

            public TextView companyTextView;

            public TextView companyDescription;

            public RatingBar ratingBar;

            public TextView valueTextView;

        }

    }

}

