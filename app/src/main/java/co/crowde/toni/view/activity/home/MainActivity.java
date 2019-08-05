package co.crowde.toni.view.activity.home;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import co.crowde.toni.R;
import co.crowde.toni.network.API;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.utils.print.PrinterNetwork;
import co.crowde.toni.view.activity.app.HelpAppsActivity;
import co.crowde.toni.view.activity.user.InfoShopActivity;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;
import co.crowde.toni.view.dialog.message.shop.CloseShopDialog;
import co.crowde.toni.view.dialog.popup.auth.ShopDetailPopup;
import co.crowde.toni.view.fragment.modul.CustomerFragment;
import co.crowde.toni.view.fragment.modul.DashboardFragment;
import co.crowde.toni.view.fragment.modul.InventoryFragment;
import co.crowde.toni.view.fragment.modul.ReportFragment;
import co.crowde.toni.zHackaton.dialog.InputProjectKeywordDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.crowde.toni.utils.print.PrinterNetwork.mBluetoothAdapter;
import static co.crowde.toni.utils.print.PrinterNetwork.mBluetoothDevice;
import static co.crowde.toni.utils.print.PrinterNetwork.mBluetoothSocket;
import static co.crowde.toni.utils.print.PrinterNetwork.printNewLine;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static CircleImageView imgShop;
    ImageView imgLogo, imgBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageView imgLogo = findViewById(R.id.imgLogo);

        imgBarcode = findViewById(R.id.img_project);
        imgBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputProjectKeywordDialog.showCredit(MainActivity.this);
            }
        });

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
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboardTransaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out);
        dashboardTransaction.replace(R.id.mainFrameLayout, dashboardFragment);
        dashboardTransaction.commit();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            CloseAppsDialog.showDialog(MainActivity.this);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_info) {
            Intent infoShop = new Intent(MainActivity.this, InfoShopActivity.class);
            startActivity(infoShop);

        } else if (id == R.id.nav_print) {
//            Toast.makeText(this, "Printer", Toast.LENGTH_SHORT).show();
            PrinterNetwork.pairingBluetooth(this);

//        } else if (id == R.id.nav_offline) {
//            Toast.makeText(this, "Mode Offline", Toast.LENGTH_SHORT).show();
//
//        } else if (id == R.id.nav_sync) {
//            Toast.makeText(this, "Sinkronisasi", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_help) {
            Intent help = new Intent(MainActivity.this, HelpAppsActivity.class);
            startActivity(help);

        } else if (id == R.id.nav_logout) {
//            Toast.makeText(this, "Tutup Toko", Toast.LENGTH_SHORT).show();
            CloseShopDialog.showDialog(MainActivity.this);
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
                        DashboardFragment dashboardFragment = new DashboardFragment();
                        FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        dashboardTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        dashboardTransaction.replace(R.id.mainFrameLayout, dashboardFragment);
                        dashboardTransaction.commit();
                        break;
                    case R.id.menu_inventory:
                        menuItem.setChecked(true);
                        InventoryFragment inventoryFragment = new InventoryFragment();
                        FragmentTransaction inventoryTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        inventoryTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        inventoryTransaction.replace(R.id.mainFrameLayout, inventoryFragment);
                        inventoryTransaction.commit();
                        break;
                    case R.id.menu_report:
                        menuItem.setChecked(true);
                        ReportFragment reportFragment = new ReportFragment();
                        FragmentTransaction reportTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        reportTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        reportTransaction.replace(R.id.mainFrameLayout, reportFragment);
                        reportTransaction.commit();
                        break;

                    case R.id.menu_customer:
                        menuItem.setChecked(true);
                        CustomerFragment customerFragment = new CustomerFragment();
                        FragmentTransaction customerTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        customerTransaction.setCustomAnimations(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        customerTransaction.replace(R.id.mainFrameLayout, customerFragment);
                        customerTransaction.commit();
                        break;
                }

                return false;
            }
        });
    }

}
