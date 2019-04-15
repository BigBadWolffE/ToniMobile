package co.crowde.toni.view.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.view.fragment.Customer;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.fragment.Inventory;
import co.crowde.toni.view.fragment.Report;
import co.crowde.toni.view.popup.InfoShopPopup;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.crowde.toni.R.id.navBottomMenu;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static CircleImageView imgShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Id Views
        imgShop = findViewById(R.id.imgShop);

        Picasso.with(MainActivity.this.getBaseContext())
                .load(API.Host+SavePref.readPicture(MainActivity.this))
                .into(imgShop);

        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoShopPopup.showInfoShop(MainActivity.this);
            }
        });

        //Set Fragment
        Dashboard dashboard = new Dashboard();
        FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboardTransaction.replace(R.id.mainFrameLayout, dashboard);
        dashboardTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_dashboard:
                Dashboard dashboard = new Dashboard();
                FragmentTransaction dashboardTransaction = getSupportFragmentManager()
                        .beginTransaction();
                dashboardTransaction.replace(R.id.mainFrameLayout, dashboard);
                dashboardTransaction.commit();
                break;
            case R.id.menu_inventory:
                Inventory inventory  = new Inventory();
                FragmentTransaction inventoryTransaction = getSupportFragmentManager()
                        .beginTransaction();
                inventoryTransaction.replace(R.id.mainFrameLayout, inventory);
                inventoryTransaction.commit();
                break;
            case R.id.menu_report:
                Report report = new Report();
                FragmentTransaction reportTransaction = getSupportFragmentManager()
                        .beginTransaction();
                reportTransaction.replace(R.id.mainFrameLayout, report);
                reportTransaction.commit();
                break;

            case R.id.menu_customer:
                Customer customer  = new Customer();
                FragmentTransaction customerTransaction = getSupportFragmentManager()
                        .beginTransaction();
                customerTransaction.replace(R.id.mainFrameLayout, customer);
                customerTransaction.commit();
                break;
        }
        return true;
    }
}
