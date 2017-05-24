package com.gw.presentation.view.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gw.domain.exception.IntervalAmountException;
import com.gw.presentation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseDurationFragment extends Fragment {

    private static final String ARG_TITLE = "Fragment title";
    private String mTitle = "";
    private int mSpinnerPos;

    @BindView(R.id.tvChooseDurationTitle)
    TextView tvTitle;
    @BindView(R.id.etDuration)
    EditText etDuration;
    @BindView(R.id.spinnerDuration)
    Spinner spinner;


    public ChooseDurationFragment() {
        // Required empty public constructor
    }

    public static ChooseDurationFragment newInstance(String param1) {
        ChooseDurationFragment fragment = new ChooseDurationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mTitle = getArguments().getString(ARG_TITLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_duration, container, false);
        ButterKnife.bind(this, view);

        tvTitle.setText(mTitle);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            ChooseDurationFragment.this.mSpinnerPos = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    });

        return view;
    }

    public int getPosition(){
        return this.mSpinnerPos;
    }

    public int getDuration(){
        try {
            int duration = Integer.parseInt(etDuration.getText().toString());
            return duration;

        } catch (NumberFormatException ex) {
            return -1;
        }
    }

}
