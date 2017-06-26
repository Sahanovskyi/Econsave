package com.gw.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gw.domain.model.decision.BuyAsset;
import com.gw.domain.model.decision.Decision;
import com.gw.domain.model.decision.OpenDeposit;
import com.gw.domain.model.decision.SellAsset;
import com.gw.domain.model.decision.TakeCredit;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.DecisionComponent;
import com.gw.presentation.view.activity.DecisionActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DecisionWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DecisionWizardFragment extends BaseFragment implements AmountDecisionFragment.OnTextChangeListener, com.gw.presentation.view.View {
    private static final int FRAGMENT_CONTAINER = R.id.decision_wizard_fragment_container;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_next)
    Button btnNext;

    private int mStep = 0;
    private ArrayList<Fragment> mFragmentList;

    private OnWizardCompleteListener mOnWizardCompleteListener;
    private Decision mDecision;
    private int mPosition;


    public DecisionWizardFragment() {
        // Required empty public constructor
    }

    public static DecisionWizardFragment newInstance() {
        DecisionWizardFragment fragment = new DecisionWizardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DecisionComponent.class).inject(this);
        this.mFragmentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decision_wizard, container, false);
        ButterKnife.bind(this, view);
        //   this.getActivityComponent().inject(this);

        progressBar.setProgress(0);

        String title = getString(R.string.decision_type);
        ArrayList<String> items = new ArrayList<>(
                Arrays.asList(getResources().getStringArray(R.array.decision_type_items)));

        Fragment fragment = RadioButtonFragment.newInstance(title, items);
        mFragmentList.add(fragment);

        addChildFragment(R.id.decision_wizard_fragment_container, fragment);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mOnWizardCompleteListener = (DecisionActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (mFragmentList != null) {
                mFragmentList.clear();
                mFragmentList = null;
            }
        } catch (Exception ex) {
            ((DecisionActivity) getActivity()).showError(ex.getMessage());
        }

        super.onDestroy();
    }

    private void chooseDecision() {
        mPosition = ((RadioButtonFragment) mFragmentList.get(0)).getPosition();

        if (mPosition != -1) {
            decisionByIndex(mPosition);
        } else {
            showToastMessage(getString(R.string.chosen_error_message));
        }

    }


    private void setAmount() {

        if (((AmountDecisionFragment) mFragmentList.get(mStep)).isCorrect()) {
            mDecision.setAmount(((AmountDecisionFragment) mFragmentList.get(mStep)).getAverage());

            Fragment fourthFragment = RadioButtonFragment.newInstance(getString(R.string.decision_priority),
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.decision_priority_items))));

            mFragmentList.add(fourthFragment);

            replaceChildFragment(FRAGMENT_CONTAINER, fourthFragment);
            this.progressBar.setProgress(++mStep);


        } else {
            showToastMessage(getString(R.string.amount_interval_error_message));
        }
    }

    private void setPriority() {
        int priorPos = ((RadioButtonFragment) mFragmentList.get(mStep)).getPosition();
        if (priorPos != -1) {
            mDecision.setPriority(Decision.Priority.fromInteger(priorPos));
            mOnWizardCompleteListener.onWizardComplete(mDecision);

        } else {
            showToastMessage(getString(R.string.chosen_error_message));
        }

    }

    private void buyAsset() {
        if (mStep == 0) {

            this.progressBar.setMax(4);
            mDecision = new BuyAsset();
            Fragment secondFragment = RadioButtonFragment.newInstance(getString(R.string.decision_assets_type),
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.decision_assets_type_items))));
            replaceChildFragment(FRAGMENT_CONTAINER, secondFragment);
            this.mFragmentList.add(secondFragment);

            this.progressBar.setProgress(++mStep);


        } else if (mStep == 1) {

            int radioBtnPos = ((RadioButtonFragment) mFragmentList.get(mStep)).getPosition();

            if (radioBtnPos != -1) {
                ((BuyAsset) mDecision).setAssetType(BuyAsset.AssetType.fromInteger(radioBtnPos));

                Fragment thirdFragment = AmountDecisionFragment.newInstance(getString(R.string.amount_fragment_title), this);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                mFragmentList.add(thirdFragment);

                this.progressBar.setProgress(++mStep);

            } else {
                showToastMessage(getString(R.string.chosen_error_message));
            }

        } else if (mStep == 2) {
            setAmount();

        } else if (mStep == 3) {
            setPriority();

        }
    }

    private void sellAsset() {
        if (mStep == 0) {

            this.progressBar.setMax(4);

            mDecision = new SellAsset();

            Fragment secondFragment = RadioButtonFragment.newInstance(getString(R.string.decision_assets_type),
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.decision_assets_type_items))));
            this.mFragmentList.add(secondFragment);

            replaceChildFragment(FRAGMENT_CONTAINER, secondFragment);
            this.progressBar.setProgress(++mStep);


        } else if (mStep == 1) {

            int radioBtnPos = ((RadioButtonFragment) mFragmentList.get(mStep)).getPosition();

            if (radioBtnPos != -1) {
                ((SellAsset) mDecision).setAssetType(SellAsset.AssetType.fromInteger(radioBtnPos));

                Fragment thirdFragment = AmountDecisionFragment.newInstance(getString(R.string.amount_fragment_title), this);
                mFragmentList.add(thirdFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                this.progressBar.setProgress(++mStep);

            } else {
                showToastMessage(getString(R.string.chosen_error_message));
            }


        } else if (mStep == 2) {
            setAmount();

        } else if (mStep == 3) {
            setPriority();
        }
    }


    private void takeCredit() {
        if (mStep == 0) {
            this.progressBar.setMax(5);

            mDecision = new TakeCredit();
            Fragment mSecondFragment = AmountDecisionFragment.newInstance(getString(R.string.interest_rate), this);
            this.mFragmentList.add(mSecondFragment);

            replaceChildFragment(FRAGMENT_CONTAINER, mSecondFragment);
            this.progressBar.setProgress(++mStep);

        } else if (mStep == 1) {

            if (((AmountDecisionFragment) mFragmentList.get(mStep)).isCorrect()) {

                ((TakeCredit) mDecision).setInterestRate(((AmountDecisionFragment) mFragmentList.get(mStep)).getAverage());

                Fragment thirdFragment = ChooseDurationFragment.newInstance(getString(R.string.credit_duration_label));
                mFragmentList.add(thirdFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                this.progressBar.setProgress(++mStep);

            } else
                showToastMessage(getString(R.string.amount_interval_error_message));

        } else if(mStep == 2) {
            int duration = ((ChooseDurationFragment) mFragmentList.get(mStep)).getDuration();
            if (duration != -1) {
                int position = ((ChooseDurationFragment) mFragmentList.get(mStep)).getPosition();
                if(position == 1)
                    duration *= 12;

                ((TakeCredit) mDecision).setCreditDuration(duration);

                Fragment thirdFragment = AmountDecisionFragment.newInstance(getString(R.string.amount_fragment_title), this);
                mFragmentList.add(thirdFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                this.progressBar.setProgress(++mStep);
            } else {
                showToastMessage(getString(R.string.aduration_error_message));
            }
        } else if (mStep == 3) {
            setAmount();

        } else if (mStep == 4) {
            setPriority();

        }
    }


    private void openDeposit() {
        if (mStep == 0) {
            this.progressBar.setMax(6);

            mDecision = new OpenDeposit();
            Fragment mSecondFragment = AmountDecisionFragment.newInstance(getString(R.string.interest_rate), this);
            this.mFragmentList.add(mSecondFragment);

            replaceChildFragment(FRAGMENT_CONTAINER, mSecondFragment);
            this.progressBar.setProgress(++mStep);

        } else if (mStep == 1) {

            if (((AmountDecisionFragment) mFragmentList.get(mStep)).isCorrect()) {

                ((OpenDeposit) mDecision).setInterestRate(((AmountDecisionFragment) mFragmentList.get(mStep)).getAverage());

                Fragment fourthFragment = RadioButtonFragment.newInstance(getString(R.string.deposit_type),
                        new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.deposit_types))));

                mFragmentList.add(fourthFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, fourthFragment);
                this.progressBar.setProgress(++mStep);

            } else
                showToastMessage(getString(R.string.amount_interval_error_message));

        }else if(mStep == 2){

            int priorPos = ((RadioButtonFragment) mFragmentList.get(mStep)).getPosition();
            if (priorPos != -1) {

                ((OpenDeposit) mDecision).setDepositType(OpenDeposit.DepositType.fromInteger(((RadioButtonFragment) mFragmentList.get(mStep)).getPosition()));

                Fragment thirdFragment = ChooseDurationFragment.newInstance(getString(R.string.deposit_duration_label));
                mFragmentList.add(thirdFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                this.progressBar.setProgress(++mStep);
            } else {
                showToastMessage(getString(R.string.chosen_error_message));
            }

        } else if (mStep == 3) {

            int duration = ((ChooseDurationFragment) mFragmentList.get(mStep)).getDuration();
            if (duration != -1) {
                int position = ((ChooseDurationFragment) mFragmentList.get(mStep)).getPosition();
                if(position == 1)
                    duration *= 12;

                ((OpenDeposit) mDecision).setDepositDuration(duration);

                Fragment thirdFragment = AmountDecisionFragment.newInstance(getString(R.string.amount_fragment_title), this);
                mFragmentList.add(thirdFragment);

                replaceChildFragment(FRAGMENT_CONTAINER, thirdFragment);
                this.progressBar.setProgress(++mStep);
            } else {
                showToastMessage(getString(R.string.aduration_error_message));
            }

        } else if (mStep == 4) {
            setAmount();

        } else if (mStep == 5) {
            setPriority();
        }
    }

    @Override
    public void onDonePressed() {
        onNextClick();
    }


    private void decisionByIndex(int index) {
        switch (index) {
            case 0:
                buyAsset();
                break;
            case 1:
                sellAsset();
                break;
            case 2:
                takeCredit();
                break;
            case 3:
                openDeposit();
                break;
        }
    }


    @OnClick(R.id.btn_next)
    public void onNextClick() {

        if (mStep == 0) {
            chooseDecision();
        } else {
            decisionByIndex(mPosition);
        }
    }

    @OnClick(R.id.btn_previous)
    public void onPreviousClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            try {
                this.mFragmentList.get(mStep).onDestroy();
            } catch (Exception ex) {
                ((DecisionActivity) getActivity()).showError(ex.getMessage());
            }
            try {
                this.mFragmentList.remove(mStep);
            } catch (Exception ex) {
                ((DecisionActivity) getActivity()).showError(ex.getMessage());
            }
            progressBar.setProgress(--mStep);

        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    public interface OnWizardCompleteListener {
        void onWizardComplete(Decision decision);
    }

}
