package com.gw.presentation.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gw.domain.model.Interval;
import com.gw.domain.model.TransactionItem;
import com.gw.presentation.R;
import com.gw.presentation.presenter.MainPresenter;
import com.gw.presentation.view.MainView;
import com.gw.presentation.view.adapters.SmoothActionBarDrawerToggle;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    public static final String ARG_TRANSACTION_ITEMS_LIST = "TransactionItemList";
    private static final String ARG_ITEM_POSITION = "Item position";

    private static final String LOG_TAG = "MainActivity.TAG";

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Bind main screen elements
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_expence)
    TextView tvExpense;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_index)
    TextView tvIndex;

    //Drawer
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private SmoothActionBarDrawerToggle mDrawerToggle;
    private TextView mDrawerText;
    private TextView mDrawerEmail;
    private FirebaseUser mUser;
    private int mItemSelected = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        this.getActivityComponent().inject(this);

        setupPresenter();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        setupToolbar();

        setupChart();

        setupNavigationDrawer();
        String locale = getResources().getConfiguration().locale.getCountry();
        showToastMessage(locale);

    }

    private void setupPresenter(){
        mainPresenter.setView(this);
        mainPresenter.initialize();
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        try {
      //      getSupportActionBar().setDisplayShowTitleEnabled(tr);
        } catch (NullPointerException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            showError(ex.getMessage());
        }

    }
//
//    private void setupSpinner(){
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.main_spinner_items));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//        spinner.setSelection(2);
//
//        spinner.setOnItemSelectedListener(mOnSpinnerItemSelectedListener);
//
//    }

    private void setupNavigationDrawer(){

        mDrawerToggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_overview);
        View v = navigationView.getHeaderView(0);
        mDrawerEmail = (TextView) v.findViewById(R.id.drawer_email);
        mDrawerText = (TextView) v.findViewById(R.id.drawer_text);
        ImageView drawerImage = (ImageView) v.findViewById(R.id.drawer_image);

        updateDrawer(null, mUser.getDisplayName(), mUser.getEmail());
        Picasso.with(this)
                .load(mUser.getPhotoUrl())
                .resize(60, 60)
                .centerCrop()
                .into(drawerImage);
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
//        Description desc = new Description();
//        desc.setText(getString(R.string.chart_name));
//        desc.setTextSize(10f);
//        chart.setDescription(desc);

        // enable touch gestures
        chart.setTouchEnabled(true);

        //   chart.setDragDecelerationFrictionCoef(10f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        //  chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.animateY(700);
        chart.getLegend().setEnabled(false);


        //    chart.setBackgroundColor(Color.TRANSPARENT);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(10f);
        yAxis.setAxisLineWidth(1f);
        chart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(getResources().getColor(R.color.dark_blue_gray));
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(5, true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });

    }

    private void showDateRangeDialog(){
        CharSequence[] items = getResources().getStringArray(R.array.main_spinner_items);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.choose_period_title));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }});

        builder.setSingleChoiceItems(items, mItemSelected, mOnDialogClickListener);

        builder.show();
    }

    private DialogInterface.OnClickListener mOnDialogClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mItemSelected = which;
                    Interval interval = Interval.fromInteger(which);

                    mainPresenter.changeChartInterval(interval);
                    dialog.cancel();
                }
            };

//
//    private AdapterView.OnItemSelectedListener mOnSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view,
//                                   int position, long id) {
//            Interval interval = Interval.fromInteger((int) id);
//
//            mainPresenter.changeChartInterval(interval);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//        }
//    };

    @OnClick(R.id.btn_decision)
    public void onDecisionClick() {
        try {
            ArrayList<TransactionItem> list = new ArrayList<>(mainPresenter.getTransactionItemsList());
            navigator.navigateToDecisionActivity(this, list);
        }catch (NullPointerException ex) {
            showError("List is empty!");
        }
    }

    @OnClick(R.id.btn_forecast)
    public void onForecastClick() {
        try {
            ArrayList<TransactionItem> list = new ArrayList<>(mainPresenter.getTransactionItemsList());
            navigator.navigateToForecastActivity(this, list);
        }catch (NullPointerException ex) {
            showError("List is empty!");
        }
    }

    @Override
    public void setChartData(LineData data) {
        chart.setData(data);
        chart.animateY(700);
        chart.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.main_date_range:
                showDateRangeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_log_out) {
            mDrawerToggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    mainPresenter.logOut();
                    Intent intent = LoginActivity.getCallingIntent(MainActivity.this);
                    intent.putExtra("SIGNING_OUT", true);
                    startActivity(intent);
                    finish();
                }
            });
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);
*/
        //     outState.putString("sweet2_txt", editText1.getText().toString());
        outState.putInt(ARG_ITEM_POSITION, mItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
       /* restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));
*/
        // Sweet2
        //        editText1.setText(savedInstanceState.getString("sweet2_txt"));

        if(savedInstanceState != null) {
            mItemSelected = savedInstanceState.getInt(ARG_ITEM_POSITION);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setIncome(String income) {
        tvIncome.setText(income + " UAH");
    }

    @Override
    public void setExpense(String expense) {
        tvExpense.setText(expense + " UAH");
    }

    @Override
    public void setBalance(String balance) {
        tvBalance.setText(balance + " UAH");
    }

    @Override
    public void setIndex(String index) {
        tvIndex.setText(index);
    }

    @Override
    public void updateDrawer(Bitmap image, String name, String email) {
        this.mDrawerEmail.setText(email);
        this.mDrawerText.setText(name);
    }

    @Override
    public Context context() {
        return this;
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
    protected void onDestroy() {
        mainPresenter.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mainPresenter.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mainPresenter.resume();
        super.onResume();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

}

