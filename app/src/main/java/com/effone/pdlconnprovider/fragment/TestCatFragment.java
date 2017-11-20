package com.effone.pdlconnprovider.fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.adapter.LoctionSummeryListAdapter;
import com.effone.pdlconnprovider.adapter.TestCatAdapter;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.LocationSummery;
import com.effone.pdlconnprovider.model.TestCat;
import com.effone.pdlconnprovider.widget.IndexableListView;

import java.util.ArrayList;


public class TestCatFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private IndexableListView mTestCatList;

    private SelectDbHelper mSelectDbHelper;

    private TextView mTvCount;

    private TestCatDetailFragment mTestCatDetailFragment;

    private CommonHeaderFragment mCommonHeaderFragment;

    private EditText mEtSearch;

    private int mCatId;

    private Drawable mDrawableRight;

    private String mSearchText;

    private ArrayList<TestCat> mTestCats;

    private ImageView mIvSearchIcon;

    private TestCatAdapter mTestCatAdapter;


    public TestCatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_cat, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mTestCatList = (IndexableListView) root.findViewById(R.id.test_cat_list);

        mTvCount = (TextView) root.findViewById(R.id.tv_count);
        mEtSearch = (EditText) root.findViewById(R.id.et_search_test_cat);
        mIvSearchIcon = (ImageView) root.findViewById(R.id.iv_search_icon);
        mTestCats = mSelectDbHelper.getTestCatList();

        mTestCatAdapter = new TestCatAdapter(getActivity(), R.layout.test_cat_list_item, mTestCats);
        mTvCount.setText(mTestCats.size() + " result(s) found.");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mDrawableRight = getActivity().getDrawable(R.drawable.close_button);
        } else {
            mDrawableRight = getActivity().getResources().getDrawable(R.drawable.close_button);
        }
        mEtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                mEtSearch.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mEtSearch.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                        if (event.getRawX() >= (mEtSearch.getRight() - mEtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            mEtSearch.setText("");
                            mEtSearch.clearFocus();
                            mEtSearch.setCursorVisible(false);

                            return true;
                        }
                    }
                }
                return false;
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mEtSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableRight, null);
                } else {

                    mTestCats = mSelectDbHelper.getTestCatList();
                    mTestCatAdapter = new TestCatAdapter(getActivity(), R.layout.test_cat_list_item, mTestCats);
                    mTestCatList.setAdapter(mTestCatAdapter);
                    if (mTestCats.size() < 10) {
                        mTestCatList.getScroller().hideForPDL();
                    } else {
                        mTestCatList.getScroller().showForPDL();
                    }
                    mTvCount.setText(mTestCats.size() + " result(s) found.");
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null && inputManager != null) {
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    mEtSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTestCatList.setAdapter(mTestCatAdapter);
        if (mTestCats.size() < 10) {
            mTestCatList.getScroller().hideForPDL();
        } else {
            mTestCatList.getScroller().showForPDL();
        }
        mTestCatList.setFastScrollEnabled(true);
        mIvSearchIcon.setOnClickListener(this);
        mTestCatList.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        mCatId = ((TestCat) parent.getAdapter().getItem(position)).testCatalogID;
        String name = ((TestCat) parent.getAdapter().getItem(position)).name;
        String displayCode = ((TestCat) parent.getAdapter().getItem(position)).displayCode;
        mTestCatDetailFragment = new TestCatDetailFragment();
        mCommonHeaderFragment = new CommonHeaderFragment();
        mCommonHeaderFragment.setTitle("Test Details");
        mTestCatDetailFragment.setmCatId(mCatId, name, displayCode);
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.add(R.id.header_fragment, mCommonHeaderFragment, "header").add(R.id.content_fragment, mTestCatDetailFragment, "test cat details").addToBackStack(null).commit();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_search_icon) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mEtSearch.clearFocus();
            mEtSearch.setCursorVisible(false);
            if (getActivity().getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            mSearchText = mEtSearch.getText().toString().trim();
            if (!mSearchText.equals("") && mSearchText.length() > 1) {
                mTestCats = mSelectDbHelper.getSearchTestCatList(mSearchText);
                mTvCount.setText(mTestCats.size() + " result(s) found.");
                mTestCatAdapter = new TestCatAdapter(getActivity(), R.layout.test_cat_list_item, mTestCats);
                mTestCatList.setAdapter(mTestCatAdapter);
                if (mTestCats.size() < 10) {
                    mTestCatList.getScroller().hideForPDL();
                } else {
                    mTestCatList.getScroller().showForPDL();
                }

            } else {
                String s=mEtSearch.getText().toString().trim();
                mEtSearch.setText(s);
                PdlUtils.createOKAlert(getActivity(),"VALIDATION FAILED!", "Please enter a search term that is at least two(2) letters long.");


            }

        }

    }
}
