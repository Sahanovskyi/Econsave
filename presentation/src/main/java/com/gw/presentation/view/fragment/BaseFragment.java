package com.gw.presentation.view.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Toast;

import com.gw.presentation.R;
import com.gw.presentation.internal.di.HasComponent;
import com.gw.presentation.view.activity.BaseActivity;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends android.app.Fragment {

    protected void replaceChildFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_left);
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void addChildFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_left);
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }


    public void showProgressDialog() {
        ((BaseActivity)getActivity()).showProgressDialog();
    }

    public void hideProgressDialog() {
        ((BaseActivity)getActivity()).hideProgressDialog();
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showError(String message) {
        this.showToastMessage(message);
    }

    public void onBackPressed(){}

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
