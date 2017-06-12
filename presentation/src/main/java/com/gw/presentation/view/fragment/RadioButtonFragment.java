package com.gw.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gw.presentation. R;
import com.gw.presentation. view.adapters.ChooseAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RadioButtonFragment extends BaseFragment implements ChooseAdapter.IProcessFilter {

    @BindView(R.id.tvDecisionFragment) TextView tv;
    @BindView(R.id.frament_list) ListView fragmentList;

    private static final String ARG_ITEMS = "FRAGMENT_ITEMS_LIST";
    private static final String ARG_TITLE = "FRAGMENT_TITLE";
    private final static String ARG_POSITION = "position";
    private int mCurrentPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_radio_button, container, false);
        ButterKnife.bind(this, v);
        fragmentList.setDivider(null);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> items = args.getStringArrayList(ARG_ITEMS);
            String title = args.getString(ARG_TITLE);
            tv.setText(title);

            ChooseAdapter chooseAdapter = new ChooseAdapter(getActivity(), R.layout.radion_button_item, items, this);
            fragmentList.setAdapter(chooseAdapter);
        }
        return v;
    }

    public static RadioButtonFragment newInstance(String title, ArrayList<String> items) {
        RadioButtonFragment fragment = new RadioButtonFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(RadioButtonFragment.ARG_ITEMS, items);
        args.putString(RadioButtonFragment.ARG_TITLE, title);

        fragment.setArguments(args);
        return fragment;
    }

    public RadioButtonFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_POSITION, mCurrentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPosition(int position) {
        mCurrentPosition = position;
    }

    @Override
    public int getPosition() {
        return mCurrentPosition;
    }
}
