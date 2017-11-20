package com.effone.pdlconnprovider.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.PdlApiUtil;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;


public class AboutFragment extends Fragment implements View.OnClickListener {
    private SelectDbHelper mSelectDbHelper;
    private WebView mWvAboutDetails;
    private ImageView mIvWebLink, mIvAboutEmailLink;
    private String mContent;
    private String js = "javascript: ("
            + "function () { "

            + "var css = 'html {-webkit-filter: invert(100%);' +"
            + "    '-moz-filter: invert(100%);' + "
            + "    '-o-filter: invert(100%);' + "
            + "    '-ms-filter: invert(100%); }',"

            + "head = document.getElementsByTagName('head')[0],"
            + "style = document.createElement('style');"

            + "if (!window.counter) { window.counter = 1;} else  { window.counter ++;"
            + "if (window.counter % 2 == 0) { var css ='html {-webkit-filter: invert(0%); -moz-filter:    invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'}"
            + "};"

            + "style.type = 'text/css';"
            + "if (style.styleSheet){"
            + "style.styleSheet.cssText = css;"
            + "} else {"
            + "style.appendChild(document.createTextNode(css));"
            + "}"

            //injecting the css to the head
            + "head.appendChild(style);"
            + "}());";


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {

        mIvWebLink = (ImageView) root.findViewById(R.id.iv_pdl_web_link);
        mIvAboutEmailLink = (ImageView) root.findViewById(R.id.iv_about_email_link);
        mIvWebLink.setOnClickListener(this);
        mIvAboutEmailLink.setOnClickListener(this);
        mWvAboutDetails = (WebView) root.findViewById(R.id.wv_about_detail);
        mWvAboutDetails.setBackgroundColor(0x00000000);
        mWvAboutDetails.getSettings().setJavaScriptEnabled(true);
        mWvAboutDetails.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvAboutDetails.loadUrl(js);
        mContent = mSelectDbHelper.getAboutUsContent(8);
        mWvAboutDetails.loadData(mContent, "text/html", "UTF-8");

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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_pdl_web_link) {
            if(PdlUtils.isInternetOn(getActivity())) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(PdlApiUtil.PDL_WEB_URL));
                startActivity(i);
            }
            else{
                PdlUtils.createOKAlert(getActivity(),"","Please check your Internet connectivity and try again.");
            }
        } else if (v.getId() == R.id.iv_about_email_link) {
            if(PdlUtils.isInternetOn(getActivity())) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/html");
                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About PDL");
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mSelectDbHelper.getAboutUsContentForMail(8)));
                startActivity(emailIntent);
            }
            else{
                PdlUtils.createOKAlert(getActivity(),"","Please check your Internet connectivity and try again.");
            }
        }
    }
}
