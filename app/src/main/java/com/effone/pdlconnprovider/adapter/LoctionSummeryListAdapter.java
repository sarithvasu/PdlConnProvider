package com.effone.pdlconnprovider.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.LocationSummery;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarith.vasu on 08-03-2016.
 */
public class LoctionSummeryListAdapter extends ArrayAdapter<LocationSummery> {
    private ArrayList<LocationSummery> values;
    private LayoutInflater inflater;

    public LoctionSummeryListAdapter(Context context, int resource, List<LocationSummery> values) {
        super(context, resource, values);
        this.values =(ArrayList<LocationSummery>) values;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        FilterViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.location_summery_ilist_tem, null);
            holder = new FilterViewHolder();
            holder.tv_location_name = (TextView ) vi.findViewById(R.id.tv_location_name);
            holder.tv_address1 = (TextView ) vi.findViewById(R.id.tv_address1);
            holder.tv_address2 = (TextView ) vi.findViewById(R.id.tv_address2);

            vi.setTag( holder );
        }
        else
            holder = (FilterViewHolder) vi.getTag();

            if (values.size() <= 0) {
                holder.tv_location_name.setText("No Data");

            } else {
                /***** Get each Model object from Arraylist ********/

                LocationSummery value = (LocationSummery) values.get(position);

                /************  Set Model values in Holder elements ***********/

                holder.tv_location_name.setText(value.locationName);
                holder.tv_address1.setText(value.locationAdress1);
                holder.tv_address2.setText(value.locationAddress2);



            }

            return vi;



    }
    public static  class FilterViewHolder {
        TextView tv_location_name;
        TextView tv_address1;
        TextView tv_address2;
    }


}
