package com.gw.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gw.domain.model.TransactionItem;
import com.gw.presentation.R;
import com.gw.presentation.view.activity.DecisionActivity;
import com.gw.presentation.view.activity.ForecastActivity;
import com.gw.presentation.view.activity.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
        //empty
    }

    /**
     * Goes to the decision making screen.
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToDecisionActivity(Context context, ArrayList<TransactionItem> list) {
        if (context != null) {
            if (list != null) {
                Intent intentToLaunch = DecisionActivity.getCallingIntent(context);
                Bundle args = new Bundle();
                args.putSerializable(MainActivity.ARG_TRANSACTION_ITEMS_LIST, list);
                intentToLaunch.putExtras(args);
                context.startActivity(intentToLaunch);
            } else {
                Toast.makeText(context, context.getString(R.string.null_transaction_items_list), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Goes to the forecast screen.
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToForecastActivity(Context context, ArrayList<TransactionItem> list) {
        if (context != null) {
            if (list != null) {
                Intent intentToLaunch = ForecastActivity.getCallingIntent(context);
                Bundle args = new Bundle();
                args.putSerializable(MainActivity.ARG_TRANSACTION_ITEMS_LIST, list);
                intentToLaunch.putExtras(args);
                context.startActivity(intentToLaunch);
            } else {
                Toast.makeText(context, context.getString(R.string.null_transaction_items_list), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Goes to the main screen.
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToMainActivity(Context context) {
        if (context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }






}
