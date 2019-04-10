package co.crowde.toni.view.main;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import co.crowde.toni.R;
import co.crowde.toni.view.fragment.Customer;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.fragment.Inventory;
import co.crowde.toni.view.fragment.Report;

import static co.crowde.toni.R.id.navBottomMenu;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new Dashboard());
        BottomNavigationView bottomNavigationView = findViewById(R.id.navBottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.menu_dashboard:
                fragment = new Dashboard();
                break;
            case R.id.menu_inventory:
                fragment = new Inventory();
                break;
            case R.id.menu_report:
                fragment = new Report();
                break;
            case R.id.menu_customer:
                fragment = new Customer();
                break;
        }
        return loadFragment(fragment);
    }
}
