package co.crowde.toni.constant;

import com.squareup.okhttp.MediaType;

public class Const {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = "TONI";

    //Intent Put Extras Code
    public final static String KEY_USERNAME_FROM_LOGIN = "LoginGetUsername";
    public final static String KEY_PASSWORD_FROM_LOGIN = "LoginGetPassword";

    public final static int KEY_SUCCESS_CREDIT_PAY = 123;
    public final static int KEY_SUCCESS_PAYMENT = 123;


    //Analytics
    //Analytics - Category
    public final static String CATEGORY_AUTHENTIFICATION = "Authentication";
    public final static String CATEGORY_PRODUCT_MANAGE = "Product Management";
    public final static String CATEGORY_CUSTOMER = "Customer";
    public final static String CATEGORY_PRODUCT = "Product";
    public final static String CATEGORY_TRANSACTION = "Transaction";
    public final static String CATEGORY_REPORT = "Report";

    //Analytics - Modul
    public final static String MODUL_LOGIN = "Login";
    public final static String MODUL_PASS = "Password";
    public final static String MODUL_REGISTER = "Register";
    public final static String MODUL_PRODUCT = "Product";
    public final static String MODUL_CART = "Cart";
    public final static String MODUL_CUSTOMER = "Customer";
    public final static String MODUL_DISCOUNT = "Discount";
    public final static String MODUL_TRANSACTION = "Transaction";
    public final static String MODUL_REPORT= "Report";

    //Analytics - Label
    //Register
    public final static String LABEL_REGISTER = "Try to Register";
    public final static String LABEL_REGISTER_SUCCESS = "Register Success";
    public final static String LABEL_REGISTER_FAILED = "Register Failed";
    //Login
    public final static String LABEL_LOGIN = "Try to Login";
    public final static String LABEL_LOGIN_SUCCESS = "Login Success";
    public final static String LABEL_LOGIN_FAILED_ACCOUNT = "Login Failed - Wrong Username / Password";
    public final static String LABEL_LOGIN_ADMIN_FAILED = "GENERATE TOKEN FAILED";
    public final static String LABEL_LOGIN_ADMIN_SUCCESS= "GENERATE TOKEN SUCCESS";
    public final static String LABEL_LOGIN_FAILED_NETWORK = "Login Failed - Network Trouble";
    //Password
    public final static String LABEL_SHOW_PASS = "Show Password";
    public final static String LABEL_FORGOT_PASS = "Try to Forgot Password";
    public final static String LABEL_SET_NEW_PASS_SUCCESS = "Set New Password Success";
    //OTP
    public final static String LABEL_REGISTER_SEND_OTP = "Register - Try to send OTP Code";
    public final static String LABEL_REGISTER_SEND_OTP_SUCCESS = "Register - Send OTP Code Success";
    public final static String LABEL_REGISTER_SEND_OTP_FAILED = "Register - Send OTP Code Failed";
    public final static String LABEL_RESET_PASS_SEND_OTP = "Reset Password - Try to send OTP Code";
    public final static String LABEL_RESET_PASS_SEND_OTP_SUCCESS = "Reset Password - Send OTP Code Success";
    public final static String LABEL_RESET_PASS_SEND_OTP_FAILED = "Reset Password - Send OTP Code Failed";
    //Product
    public final static String LABEL_PRODUCT_SEARCH_DASHBOARD = "Product - Search Product Dashboard";
    public final static String LABEL_PRODUCT_SEARCH_INVENTORY = "Product - Search Product Inventory";
    public final static String LABEL_PRODUCT_FILTER_DASHBOARD = "Product - Filter Product Dashboard";
    public final static String LABEL_PRODUCT_FILTER_CATEGORY_INVENTORY = "Product - Filter Category Product Inventory";
    public final static String LABEL_PRODUCT_FILTER_STATUS_INVENTORY = "Product - Filter Status Product Inventory";
    //Product - Stock
    public final static String LABEL_PRODUCT_ADD_STOCK_PLUS_MIN = "Product - Add stock from Plus Minus";
    public final static String LABEL_PRODUCT_ADD_STOCK_COLUMN = "Product - Add stock from Column";
    public final static String LABEL_PRODUCT_UPDATE = "Product - Update Product Detail";
    // Product - Discount
    public final static String LABEL_DISCOUNT_PRODUCT_DETAIL_ADD = "Product - Add Product Discount from Product Detail";
    public final static String LABEL_DISCOUNT_PRODUCT_DETAIL_UPDATE = "Product - Update Product Discount from Product Detail";
    public final static String LABEL_DISCOUNT_PRODUCT_DETAIL_REMOVE = "Product - Remove Product Discount from Product Detail";
    public final static String LABEL_DISCOUNT_CART_ADD = "Product - Add Product Discount from Cart List";
    public final static String LABEL_DISCOUNT_CART_UPDATE = "Product - Update Product Discount from Cart List";
    public final static String LABEL_DISCOUNT_CART_REMOVE = "Product - Remove Product Discount from Cart List";
    //Transaction
    public final static String LABEL_TRANSACTION_CASH_SUCCESS = "Transaction - Payment Cash Success";
    public final static String LABEL_TRANSACTION_CASH_FAILED = "Transaction - Payment Cash Failed";
    public final static String LABEL_TRANSACTION_CASH_PRINT = "Transaction - Print Payment Cash";
    public final static String LABEL_TRANSACTION_CREDIT_SUCCESS = "Transaction - Payment Credit Success";
    public final static String LABEL_TRANSACTION_CREDIT_FAILED = "Transaction - Payment Credit Failed";
    public final static String LABEL_TRANSACTION_CREDIT_PRINT = "Transaction - Print Payment Credit";
    public final static String LABEL_TRANSACTION_CASH_CREDIT_SUCCESS = "Transaction - Payment Cash+Credit Success";
    public final static String LABEL_TRANSACTION_CASH_CREDIT_FAILED = "Transaction - Payment Cash+Credit Failed";
    public final static String LABEL_TRANSACTION_CASH_CREDIT_PRINT = "Transaction - Print Payment Cash+Credit";
    public final static String LABEL_TRANSACTION_NOT_PRINTED = "Transaction - Payment Struct not Print";
    //Cart - Product
//    public final static String LABEL_CART_ADD_PRODUCT_POPUP = "Cart - Add Product via Popup Product";
    public final static String LABEL_CART_ADD_PRODUCT_DETAIL = "Cart - Add Product via Product Detail";
    public final static String LABEL_CART_ADD_PRODUCT_DASHBOARD = "Cart - Add Product via Dashboard";
    public final static String LABEL_CART_REMOVE_PRODUCT = "Cart - Remove Product";
    public final static String LABEL_CART_REMOVE_ALL_PRODUCT = "Cart - Remove All Product";
    //Cart - Quantity
    public final static String LABEL_CART_CHANGE_QTY_PLUS_MIN = "Cart - Change Quantity from Plus Minus";
    public final static String LABEL_CART_CHANGE_QTY_DROPDOWN = "Cart - Change Quantity from Dropdown";
    public final static String LABEL_CART_CHANGE_QTY_DASHBOARD = "Cart - Change Quantity from Dashboard Plus Minus";
    //Cart - Customer
    public final static String LABEL_CART_CHOOSE_CUSTOMER = "Cart - Choose Customer";
    public final static String LABEL_CART_CHANGE_CUSTOMER = "Cart - Change Customer";
    public final static String LABEL_CART_ADD_NEW_CUSTOMER = "Cart - Add New Customer";
    //Report
    public final static String LABEL_REPORT_TODAY = "Report Today";
    public final static String LABEL_REPORT_THREE_DAY = "Report Three Days";
    public final static String LABEL_REPORT_WEEK = "Report this week";
    public final static String LABEL_REPORT_MONTH = "Report this month";
    public final static String LABEL_REPORT_DATE_RANGE = "Report date range";
    //Transaction List
    public final static String LABEL_TRANSACTION_TODAY = "Transaction Today";
    public final static String LABEL_TRANSACTION_THREE_DAY = "Transaction Three Days";
    public final static String LABEL_TRANSACTION_WEEK = "Transaction this week";
    public final static String LABEL_TRANSACTION_MONTH = "Transaction this month";
    public final static String LABEL_TRANSACTION_DATE_RANGE = "Transaction date range";
    //Customer List
    public final static String LABEL_CUSTOMER_LIST = "Get Customer List";
    public final static String LABEL_CUSTOMER_SEARCH = "Search Customer";
    public final static String LABEL_CUSTOMER_ADD_NEW = "Add New Customer";
    public final static String LABEL_CUSTOMER_CANCEL_ADD = "Cancel to add new Customer";
    public final static String LABEL_CUSTOMER_DETAIL = "Get Detail Customer";
    public final static String LABEL_CUSTOMER_CREDIT_PAY = "Customer - Credit Pay";
    public final static String LABEL_CUSTOMER_PRINT = "Customer - Print Customer credit";

}
