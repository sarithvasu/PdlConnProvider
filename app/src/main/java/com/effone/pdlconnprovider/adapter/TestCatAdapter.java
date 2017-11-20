package com.effone.pdlconnprovider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.StringMatcher;
import com.effone.pdlconnprovider.model.LocationSummery;
import com.effone.pdlconnprovider.model.TestCat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarith.vasu on 23-05-2016.
 */
public class TestCatAdapter extends ArrayAdapter<TestCat> implements SectionIndexer {
    private ArrayList<TestCat> values;
    private LayoutInflater inflater;
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public TestCatAdapter(Context context, int resource, List<TestCat> values) {
        super(context, resource, values);
        this.values = (ArrayList<TestCat>) values;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;


        if (convertView == null) {
            vi = inflater.inflate(R.layout.test_cat_list_item, null);
            holder = new ViewHolder();
            holder.tv_display_code = (TextView) vi.findViewById(R.id.tv_display_code);
            holder.tv_test_name = (TextView) vi.findViewById(R.id.tv_test_name);
            holder.tv_test_location = (TextView) vi.findViewById(R.id.tv_test_location);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (values.size() <= 0) {
            holder.tv_test_name.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/

            TestCat value = (TestCat) values.get(position);

            /************  Set Model values in Holder elements ***********/
            if (value != null) {
                holder.tv_display_code.setText(value.displayCode);
                holder.tv_test_name.setText(value.name);
                holder.tv_test_location.setText(value.location);
            }


        }

        return vi;


    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = sectionIndex; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(getItem(j).displayCode.charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(getItem(j).displayCode.charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void updateData(ArrayList<TestCat> values){
        this.values=values;
    }

    public static class ViewHolder {
        TextView tv_display_code;
        TextView tv_test_name;
        TextView tv_test_location;
    }
}