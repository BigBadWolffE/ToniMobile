package co.crowde.toni.controller.network;

import android.app.Activity;

import co.crowde.toni.helper.SavePref;

public class API{
    public static final String TAG = "TONI POS";
    public static final String Slash = "/";
    public static final String Count = "?count=9999";
    public static final String Host = "http://178.128.51.137:10007/";
    public static final String API = Host+"api/";

    public static final String User = API+"user";
    public static final String Product = API+"product";
    public static final String Catalog = API+"catalog";
    public static final String Shop = API+"shop";
    public static final String Customer = API+"customer";
    public static final String Category = API+"category";
    public static final String Supplier = API+"supplier";
    public static final String Warehouse = API+"warehouse";
    public static final String Transaction = API+"transaction";
    public static final String Report = API+"report";

    public static final String Login = User+Slash+"login";

    public static final String GetShopDetail = Shop+Slash;
    public static final String OpenShop = Shop+Slash+"openShop";
    public static final String ClosedShop = Shop+Slash+"closeShop";


    //Another One
    public static final String TRANSAKSI_URL = API+"transaction/dateRange/";
    public static final String DETAILTRANSAKSI_URL = API+"transaction/detail/";
    public static final String CUSTOMERLIST_URL = API+"customer/";
    public static final String BAYARHUTANG_URL = API+"credit/paid";
    public static final String LAPORAN_URL = API+"report/mobile/";

}
