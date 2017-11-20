package com.effone.pdlconnprovider.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.db.SelectDbHelper;


public class TestCatDetailFragment extends Fragment {



    private int mCatId;
    private String mTestCatName;
    private String mTestCatDisplayCode;
    private TextView mTvCatName,mTvDisplayCode;

    private String js ="javascript: ("
            +"function () { "

            +"var css = 'html {-webkit-filter: invert(100%);' +"
            +"    '-moz-filter: invert(100%);' + "
            +"    '-o-filter: invert(100%);' + "
            +"    '-ms-filter: invert(100%); }',"

            +"head = document.getElementsByTagName('head')[0],"
            +"style = document.createElement('style');"

            +"if (!window.counter) { window.counter = 1;} else  { window.counter ++;"
            +"if (window.counter % 2 == 0) { var css ='html {-webkit-filter: invert(0%); -moz-filter:    invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'}"
            +"};"

            +"style.type = 'text/css';"
            +"if (style.styleSheet){"
            +"style.styleSheet.cssText = css;"
            +"} else {"
            +"style.appendChild(document.createTextNode(css));"
            +"}"

            //injecting the css to the head
            +"head.appendChild(style);"
            +"}());";

    private SelectDbHelper mSelectDbHelper;
    private WebView mWvTestCatDetails;



    public TestCatDetailFragment() {
        // Required empty public constructor
    }

    public void setmCatId(int mCatId,String catName,String displayCode) {
        this.mCatId = mCatId;
        this.mTestCatName=catName;
        this.mTestCatDisplayCode=displayCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_cat_detail, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mTvCatName=(TextView)root.findViewById(R.id.test_cat_name);
        mTvDisplayCode=(TextView)root.findViewById(R.id.test_cat_display_code);
        mWvTestCatDetails=(WebView)root.findViewById(R.id.wv_test_cat_detail);
        mWvTestCatDetails.setBackgroundColor(0x00000000);
        mWvTestCatDetails.getSettings().setJavaScriptEnabled(true);
        mWvTestCatDetails.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvTestCatDetails.loadUrl(js);
        mWvTestCatDetails.loadData(mSelectDbHelper.getTestCatDetails(mCatId), "text/html", "UTF-8");
        mTvCatName.setText(mTestCatName);
        mTvDisplayCode.setText("Test Code: "+mTestCatDisplayCode);

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
