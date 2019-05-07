package co.crowde.toni.view.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.view.fragment.Customer;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.fragment.Inventory;
import co.crowde.toni.view.fragment.Report;
import co.crowde.toni.view.popup.InfoShopPopup;
import co.crowde.toni.view.popup.ShopDetailPopup;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static CircleImageView imgShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ShopModel model = new Gson()
                .fromJson(SavePref.readUserDetail(MainActivity.this), ShopModel.class);

        TextView tvShopName = headerView.findViewById(R.id.tvNavShopName);
        TextView tvShopOwner = headerView.findViewById(R.id.tvNavShopOwner);
        tvShopName.setText(model.getShopName());
        tvShopOwner.setText(model.getOwnerName());

        CircleImageView imgNav = headerView.findViewById(R.id.navImg);
        Picasso.with(MainActivity.this.getBaseContext())
                .load(API.Host+SavePref.readPicture(MainActivity.this))
                .into(imgNav);

        bottomNav();

        //Set Fragment
        Dashboard dashboard = new Dashboard();
        FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboardTransaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out);
        dashboardTransaction.replace(R.id.mainFrameLayout, dashboard);
        dashboardTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_info) {
            ShopDetailPopup.showDetailShop(MainActivity.this);

        } else if (id == R.id.nav_print) {
            Toast.makeText(this, "Printer", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_offline) {
            Toast.makeText(this, "Mode Offline", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sync) {
            Toast.makeText(this, "Sinkronisasi", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Tentang Aplikasi", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Tutup Toko", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void bottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navBottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_dashboard:
                        menuItem.setChecked(true);
                        Dashboard dashboard = new Dashboard();
                        FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        dashboardTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        dashboardTransaction.replace(R.id.mainFrameLayout, dashboard);
                        dashboardTransaction.commit();
                        break;
                    case R.id.menu_inventory:
                        menuItem.setChecked(true);
                        Inventory inventory  = new Inventory();
                        FragmentTransaction inventoryTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        inventoryTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        inventoryTransaction.replace(R.id.mainFrameLayout, inventory);
                        inventoryTransaction.commit();
                        break;
                    case R.id.menu_report:
                        menuItem.setChecked(true);
                        Report report = new Report();
                        FragmentTransaction reportTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        reportTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        reportTransaction.replace(R.id.mainFrameLayout, report);
                        reportTransaction.commit();
                        break;

                    case R.id.menu_customer:
                        menuItem.setChecked(true);
                        Customer customer  = new Customer();
                        FragmentTransaction customerTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        customerTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        customerTransaction.replace(R.id.mainFrameLayout, customer);
                        customerTransaction.commit();
                        break;
                }

                return false;
            }
        });
    }
}
