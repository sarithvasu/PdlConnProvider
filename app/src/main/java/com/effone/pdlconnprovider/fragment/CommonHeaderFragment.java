package com.effone.pdlconnprovider.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;

public class CommonHeaderFragment extends Fragment implements View.OnClickListener {

    private String mTitle;
    private TextView mTitleTv;
    private ImageView mBackBtn, mHomeBtn;
    private HomeFragment mHomeFragment;
    private boolean mChange;
    private CommonHeaderFragment mCommonHeaderFragment;
    private LocationSearchFragment mLocationSearchFragment;


    public CommonHeaderFragment() {
        // Required empty public constructor
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void changeToSearchIcon(boolean change) {
        this.mChange = change;
        if(mHomeBtn!=null){
            mHomeBtn.setBackgroundResource(R.drawable.search_a_48x48px);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_common_header, container, false);
        init(root);
        return root;

    }

    public void init(View root) {
        mTitleTv = (TextView) root.findViewById(R.id.tv_title);
        mBackBtn = (ImageView) root.findViewById(R.id.iv_back_btn);
        mHomeBtn = (ImageView) root.findViewById(R.id.iv_home_btn);
        if (mChange) {
            mHomeBtn.setBackgroundResource(R.drawable.search_a_48x48px);
        }
        mBackBtn.setOnClickListener(this);
        mHomeBtn.setOnClickListener(this);
        mTitleTv.setText(mTitle);
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
        if (v.getId() == R.id.iv_back_btn) {

            getActivity().onBackPressed();


        } else if (v.getId() == R.id.iv_home_btn) {

            if (mChange) {
                setTitle("Search PSC Locations");
                mHomeBtn.setBackgroundResource(R.drawable.rsz_home_a_48x48px);
                mLocationSearchFragment = new LocationSearchFragment();
                mCommonHeaderFragment = new CommonHeaderFragment();
                mCommonHeaderFragment.setTitle("Search PSC Locations");
                mCommonHeaderFragment.changeToSearchIcon(false);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.header_fragment, mCommonHeaderFragment, "location_search").add(R.id.content_fragment, mLocationSearchFragment, "locationSearch_frag").addToBackStack(null).commit();
                mChange=false;

            } else {

                mHomeFragment = new HomeFragment();
                FragmentManager fm = getActivity().getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();


                Fragment common = getFragmentManager().findFragmentByTag("header");
                if (common != null && common.isVisible()) {
                    getFragmentManager().beginTransaction().remove(common).commit();
                }

                ft1.add(R.id.content_fragment, mHomeFragment).commit();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
