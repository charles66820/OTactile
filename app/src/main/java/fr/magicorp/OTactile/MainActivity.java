package fr.magicorp.OTactile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment productsFragment;
    private Fragment profileFragment;

    private static final int PRODUCTFRAGMENT = 1;
    private static final int PROFILEFRAGMENT = 2;

    private SharedPreferences pref;
    private boolean isDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isDark = pref.getBoolean("night_mode", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // select on start
        navigationView.setCheckedItem(R.id.nav_products);
        showFragment(PRODUCTFRAGMENT);
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
    protected void onResume() {
        super.onResume();
        if (isDark != pref.getBoolean("night_mode", false)){
            super.recreate();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            showFragment(PRODUCTFRAGMENT);
        } else if (id == R.id.nav_profile) {
            showFragment(PROFILEFRAGMENT);
        } else if (id == R.id.nav_barcode) {
            Intent intent = new Intent(this, BarcodeActivity.class); // instantiate Intent with an new activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class); // instantiate Intent with an new activity
            startActivity(intent);
        } else if (id == R.id.nav_stats) {
            Intent intent = new Intent(this, statsActivity.class); // instantiate Intent with an new activity
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragmentName) {
        Fragment fragment = null;
        switch (fragmentName) {
            case PRODUCTFRAGMENT:
                productsFragment = (productsFragment == null)? new ProductsFragment() : productsFragment;
                fragment = productsFragment;
            break;
            case PROFILEFRAGMENT:
                profileFragment = (profileFragment == null)? new ProfileFragment() : profileFragment;
                fragment = profileFragment;
                break;
        }

        if (fragment != null && !fragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }
}
