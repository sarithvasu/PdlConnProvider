package com.effone.pdlconnprovider.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.adapter.FaqListAdapter;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.Faq;


public class FaqFragment extends Fragment {


    private ListView mFaqList;
    private SelectDbHelper mSelectDbHelper;
    public FaqFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faq, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mFaqList=(ListView)root.findViewById(R.id.faq_list);
        FaqListAdapter faqListAdapter= new FaqListAdapter(getActivity(),R.layout.faq_list_item,mSelectDbHelper.getFaq());
        mFaqList.setAdapter(faqListAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
