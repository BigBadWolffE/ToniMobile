package co.crowde.toni.network;

public class API{
    public static final String TAG = "TONI POS";
    public static final String Slash = "/";
    public static final String Count = "?count=9999";

//    public static final String Host = "https://apitoni-staging.crowde.co/";
    public static final String Host2 = "https://user-service-staging.crowde.co/";

    public static final String Host = "https://toni-api.crowde.co/";
    public static final String API = Host+"api/";
    public static final String API2 = Host2+"api/v1/";

    public static final String User = API+"user";
    public static final String Product = API+"product";
    public static final String Product_Discount = API+"product/add-discount";
    public static final String Catalog = API+"catalog";
    public static final String Shop = API+"shop";
    public static final String Register = API+"register";
    public static final String Otp = API+"register/otp/verify";
    public static final String OtpResend = API+"register/otp/resend";

    public static final String Customer = API+"customer";
    public static final String NewShops = API+"shop";
    public static final String Category = API+"category";
    public static final String Supplier = API+"supplier";
    public static final String Warehouse = API+"warehouse";
    public static final String Transaction = API+"transaction";
    public static final String Report = API+"report";
    public static final String Credit = API+"credit";

    public static final String Login = User+Slash+"login";

    public static final String GetShopDetail = Shop+Slash;
    public static final String OpenShop = Shop+Slash+"openShop";
    public static final String ClosedShop = Shop+Slash+"closeShop";
    public static final String CreditPaid = Credit+Slash+"paid";

    public static final String AddNewProduct = Product+Slash+"createMultiple";


    //Another One
    public static final String TRANSAKSI_URL = API+"transaction/dateRange/";
    public static final String TRANSAKSI_URL2 = API+"transaction/";
    public static final String DETAILTRANSAKSI_URL = API+"transaction/detail/";
    public static final String CUSTOMERLIST_URL = API+"customer/";
    public static final String BAYARHUTANG_URL = API+"credit/paid";
    public static final String LAPORAN_URL = API+"report/mobile/";

    //Get Location & Registration
    public static final String PROVINCE = "http://dev.farizdotid.com/api/daerahindonesia/provinsi";
    public static final String PROVINCES = API2+"provinces";
    public static final String CITIES = API2+"city";
    public static final String DISTRICTS = API2+"sub-district";
    public static final String VILLAGES = API2+"urban-village";



}
