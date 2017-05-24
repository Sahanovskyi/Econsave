package com.gw.presentation.view;

import android.content.Context;

/**
 * Created by vadym on 05.05.17.
 */

public interface View {
    /**
     * Get a {@link Context}.
     */
    Context context();

    void showToastMessage(String message);

    void showProgressDialog();
    void hideProgressDialog();
    /**
     * Show an error message
     *
     * @param message A string representing an error.
     *
     */
    void showError(String message);

}
