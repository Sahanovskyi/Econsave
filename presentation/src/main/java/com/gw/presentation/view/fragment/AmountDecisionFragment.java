package com.gw.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.gw.presentation.R;
import com.gw.domain.exception.IntervalAmountException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AmountDecisionFragment extends BaseFragment {
    public interface OnTextChangeListener {
        void onDonePressed();
    }

    @BindView(R.id.tvAmountTitle) TextView tvTitle;
    @BindView(R.id.etFrom) EditText etFrom;
    @BindView(R.id.etTo) EditText etTo;

    private static final String ET_FROM_TEXT = "from";
    private static final String ET_TO_TEXT = "to";
    private static final String ARG_TITLE = "title";
    private static final String TAG = "AmountFragmentException";
    private OnTextChangeListener listener;

    public AmountDecisionFragment() {
    }

    public static AmountDecisionFragment newInstance(String title, OnTextChangeListener listener) {
        AmountDecisionFragment fragment = new AmountDecisionFragment();
        fragment.listener = listener;
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amount_decision, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            tvTitle.setText(savedInstanceState.getString(ARG_TITLE));
            etFrom.setText(savedInstanceState.getString(ET_FROM_TEXT));
            etTo.setText(savedInstanceState.getString(ET_TO_TEXT));
        }

        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString(ARG_TITLE);
            tvTitle.setText(title);
        }
        etTo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    listener.onDonePressed();
                }
                return false;
            }
        });

        return view;
    }



    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public boolean isCorrect() {
        try {
            double from = Double.parseDouble(etFrom.getText().toString());
            double to = Double.parseDouble(etTo.getText().toString());

            if (from <= to)
                return true;
            else
                throw new IntervalAmountException("Incorrect amount interval");

        } catch (NumberFormatException ex) {
            exception = ex;
            Log.e(TAG, ex.getMessage());
        }
        catch (IntervalAmountException ex){
            exception = ex;
        }
        return false;

    }


    public double getAverage(){

        if(isCorrect()){
            double from = Double.parseDouble(etFrom.getText().toString());
            double to = Double.parseDouble(etTo.getText().toString());
            return (from + to) / 2;
        }
        return -1;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_TITLE, tvTitle.getText().toString());
        outState.putString(ET_FROM_TEXT, etFrom.getText().toString());
        outState.putString(ET_TO_TEXT, etTo.getText().toString());
        super.onSaveInstanceState(outState);

    }


}
