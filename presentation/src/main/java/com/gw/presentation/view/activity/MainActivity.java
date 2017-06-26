package com.gw.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.HasComponent;
import com.gw.presentation.internal.di.component.DaggerMainComponent;
import com.gw.presentation.internal.di.component.MainComponent;
import com.gw.presentation.view.adapters.SmoothActionBarDrawerToggle;
import com.gw.presentation.view.fragment.BaseFragment;
import com.gw.presentation.view.fragment.OverviewFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HasComponent<MainComponent>, com.gw.presentation.view.View {

    public static final String ARG_TRANSACTION_ITEMS_LIST = "TransactionItemList";
    private static final String ARG_ITEM_POSITION = "Item position";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Drawer
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private SmoothActionBarDrawerToggle mDrawerToggle;
    private TextView mDrawerText;
    private TextView mDrawerEmail;
    private FirebaseUser mUser;

    private BaseFragment mFragment;

    private MainComponent mMainComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        initializeInjector();

        mFragment = OverviewFragment.newInstance();
        addFragment(R.id.main_fragment_container, mFragment);
        setupNavigationDrawer();
    }

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_accounts) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_log_out) {
            mDrawerToggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
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

    public void updateDrawer(Bitmap image, String name, String email) {
        this.mDrawerEmail.setText(email);
        this.mDrawerText.setText(name);
    }

    private void initializeInjector() {
        this.mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public MainComponent getComponent() {
        return this.mMainComponent;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

}

