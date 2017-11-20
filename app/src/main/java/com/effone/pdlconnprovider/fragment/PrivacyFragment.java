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
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;


public class PrivacyFragment extends Fragment implements View.OnClickListener {

    private SelectDbHelper mSelectDbHelper;
    private WebView mWvPrivacyDetail;
    private ImageView  mIvPrivacyEmailLink;
    public PrivacyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mIvPrivacyEmailLink=(ImageView)root.findViewById(R.id.iv_privacy_email_link);
        mWvPrivacyDetail=(WebView)root.findViewById(R.id.wv_privacy_detail);
        mWvPrivacyDetail.setBackgroundColor(0x00000000);
        mWvPrivacyDetail.loadData(mSelectDbHelper.getAboutUsContent(9), "text/html", "UTF-8");
        mIvPrivacyEmailLink.setOnClickListener(this);

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
        if (v.getId() == R.id.iv_privacy_email_link) {
            if(PdlUtils.isInternetOn(getActivity())) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PDL Privacy Notice");
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mSelectDbHelper.getAboutUsContentForMail(9)));
                startActivity(emailIntent);
            }
            else{
                PdlUtils.createOKAlert(getActivity(),"","Please check your Internet connectivity and try again.");
            }
        }

    }
}
