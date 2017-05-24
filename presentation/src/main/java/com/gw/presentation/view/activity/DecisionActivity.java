package com.gw.presentation.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gw.domain.model.TransactionItem;
import com.gw.domain.model.decision.BuyAsset;
import com.gw.domain.model.decision.Decision;
import com.gw.domain.model.decision.SellAsset;
import com.gw.domain.model.decision.TakeCredit;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.HasComponent;
import com.gw.presentation.internal.di.component.DaggerDecisionComponent;
import com.gw.presentation.internal.di.component.DecisionComponent;
import com.gw.presentation.navigation.Navigator;
import com.gw.presentation.view.fragment.BaseFragment;
import com.gw.presentation.view.fragment.DecisionListFragment;
import com.gw.presentation.view.fragment.DecisionReviewFragment;
import com.gw.presentation.view.fragment.DecisionWizardFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.gw.presentation.view.activity.MainActivity.ARG_TRANSACTION_ITEMS_LIST;

public class DecisionActivity extends BaseActivity implements com.gw.presentation.view.View, HasComponent<DecisionComponent>, DecisionWizardFragment.OnWizardCompleteListener {

    @Inject
    Navigator navigator;

    private BaseFragment mFragment;

    private DecisionComponent mDecisionComponent;


    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private Menu mMenu;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, DecisionActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeInjector();

        mFragment = DecisionWizardFragment.newInstance();
        addFragment(R.id.decision_fragment_container, mFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.decision_menu, menu);


        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        mSearchView.setSubmitButtonEnabled(false);

        mSearchMenuItem.setVisible(false);
        mSearchView.setVisibility(View.GONE);

        return true;
    }

    private void showOverflowMenu(boolean showMenu) {
        if (mMenu == null)
            return;
        mMenu.setGroupVisible(R.id.decision_menu_group, showMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                super.finish();
                return true;
            case R.id.decision_action_wizard:
                mFragment = DecisionWizardFragment.newInstance();
                replaceFragment(R.id.decision_fragment_container, mFragment, false);
                mSearchView.setVisibility(View.GONE);
                mSearchMenuItem.setVisible(false);
                return true;
            case R.id.decision_action_list:
                mFragment = DecisionListFragment.newInstance();

                replaceFragment(R.id.decision_fragment_container, mFragment, false);
                mSearchView.setVisibility(View.VISIBLE);
                mSearchMenuItem.setVisible(true);

                return true;
            default:
                mSearchView.setVisibility(View.GONE);
                mSearchMenuItem.setVisible(false);

                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null &&
                mFragment instanceof DecisionWizardFragment &&
                mFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            mFragment.onBackPressed();
        } else super.onBackPressed();
    }

    private void initializeInjector() {
        this.mDecisionComponent = DaggerDecisionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public DecisionComponent getComponent() {
        return this.mDecisionComponent;
    }

    @Override
    public void onWizardComplete(Decision decision) {
        showOverflowMenu(false);

        mFragment = DecisionReviewFragment.newInstance(
                (ArrayList<TransactionItem>) getIntent().getSerializableExtra(ARG_TRANSACTION_ITEMS_LIST), decision);

        replaceFragment(R.id.decision_fragment_container, mFragment, false);
    }
}
