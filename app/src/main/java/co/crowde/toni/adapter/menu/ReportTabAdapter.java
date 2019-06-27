package co.crowde.toni.adapter.menu;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import co.crowde.toni.view.fragment.transaction.DashboardReportFragment;
import co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment;
import co.crowde.toni.view.fragment.transaction.TransactionReport;


public class ReportTabAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public ReportTabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                DashboardReportFragment dashboard = new DashboardReportFragment();

                return dashboard;
            case 1:
                ListTransactionReportFragment list = new ListTransactionReportFragment();
                return list;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Laporan";
            case 1:
                return "Transaksi";
        }
        return null;
    }
}