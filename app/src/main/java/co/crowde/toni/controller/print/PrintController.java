package co.crowde.toni.controller.print;

import android.app.Activity;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.UserDetailModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.model.response.object.AddNewTransactionModel;
import co.crowde.toni.model.response.object.CreditPayModel;
import co.crowde.toni.view.activity.customer.CustomerHutangActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.helper.DecimalFormatRupiah.formatNumber;
import static co.crowde.toni.utils.print.PrinterNetwork.mBluetoothSocket;
import static co.crowde.toni.utils.print.PrinterNetwork.os;
import static co.crowde.toni.utils.print.PrinterNetwork.printCustom;
import static co.crowde.toni.utils.print.PrinterNetwork.printNewLine;
import static co.crowde.toni.utils.print.PrinterNetwork.printPhoto;
import static co.crowde.toni.utils.print.PrinterNetwork.printText;

public class PrintController {

    public static Locale lokal = new Locale("id");

    private static String payment;
    public static int sub_total, discount, total_amount;

    //print cash
    public static void printCash(Activity activity, String data) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %13s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            printNewLine();
            printText(String.format("%-10s %21s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-10s %21s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();

        } catch (IOException ignored) {
        }

    }
    public static void printCashM(Activity activity, String data) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-21s %18s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %13s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            printNewLine();
            printText(String.format("%-10s %21s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-10s %21s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom("Ini Ukuran Medium",0,1
            );
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();

        } catch (IOException ignored) {
        }

    }
    public static void printCashL(Activity activity, String data) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0,1 );
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================================\n");

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-18s %28s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-18s %28s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================================\n");
            printText(String.format("%-18s %28s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %28s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %28s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            printNewLine();
            printText(String.format("%-18s %28s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-18s %28s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("------------------------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);


            printNewLine();
            printNewLine();

            os.flush();

        } catch (IOException ignored) {
        }

    }



    //print credit
    public static void printCredit(Activity activity, String data, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %13s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            final int totalCredit = DashboardFragment.totalAmount
                    + credit;

            printNewLine();
            printText(String.format("%-18s %13s", "Hutang Sebelumnya",
                    formatNumber.format(credit)) + "\n");
            printText(String.format("%-18s %13s", "Total Hutang",
                    formatNumber.format(totalCredit)) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }
    public static void printCreditM(Activity activity, String data, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %13s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            final int totalCredit = DashboardFragment.totalAmount
                    + credit;

            printNewLine();
            printText(String.format("%-18s %13s", "Hutang Sebelumnya",
                    formatNumber.format(credit)) + "\n");
            printText(String.format("%-18s %13s", "Total Hutang",
                    formatNumber.format(totalCredit)) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }
    public static void printCreditL(Activity activity, String data, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        total_amount = Integer.parseInt(model.getAmount().replaceAll("[,.]",""));

        String tanggalTransaksi = model.getCreatedAt();

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-18s %28s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-18s %28s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================================\n");
            printText(String.format("%-18s %28s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (total_amount<sub_total) {
                printText(String.format("%-18s %28s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
                printText(String.format("%-18s %28s", "" +
                                "Total Belanja",
                        formatNumber.format(Integer
                                .parseInt(model.getAmount()))) + "\n");
            }

            final int totalCredit = DashboardFragment.totalAmount
                    + credit;

            printNewLine();
            printText(String.format("%-18s %28s", "Hutang Sebelumnya",
                    formatNumber.format(credit)) + "\n");
            printText(String.format("%-18s %28s", "Total Hutang",
                    formatNumber.format(totalCredit)) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }

    //print Cash Credit
    public static void printCashCredit(Activity activity, String dataTransaction, int saldo, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(dataTransaction, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        String tanggalTransaksi = model.getCreatedAt();

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (discount > 0) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
            }
            printText(String.format("%-18s %13s", "" +
                            "Bayar Hutang",
                    formatNumber.format(credit)) + "\n");

            int amount = Integer.parseInt(model.getAmount()) + credit;
            printText(String.format("%-18s %13s", "" +
                            "Total Belanja",
                    formatNumber.format(amount)) + "\n");

            printNewLine();
            printText(String.format("%-10s %21s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-10s %21s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
//            printText(String.format("%-18s %13s","Hutang Sebelumnya",
//                    formatNumber.format(customer.getSaldo()))+"\n");
            printText(String.format("%-15s %16s", "Sisa Hutang", (
                    formatNumber.format(saldo - credit))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCashCreditM(Activity activity, String dataTransaction, int saldo, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(dataTransaction, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        String tanggalTransaksi = model.getCreatedAt();

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================\n");
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (discount > 0) {
                printText(String.format("%-18s %13s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
            }
            printText(String.format("%-18s %13s", "" +
                            "Bayar Hutang",
                    formatNumber.format(credit)) + "\n");

            int amount = Integer.parseInt(model.getAmount()) + credit;
            printText(String.format("%-18s %13s", "" +
                            "Total Belanja",
                    formatNumber.format(amount)) + "\n");

            printNewLine();
            printText(String.format("%-10s %21s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-10s %21s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
//            printText(String.format("%-18s %13s","Hutang Sebelumnya",
//                    formatNumber.format(customer.getSaldo()))+"\n");
            printText(String.format("%-15s %16s", "Sisa Hutang", (
                    formatNumber.format(saldo - credit))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCashCreditL(Activity activity, String dataTransaction, int saldo, int credit) {
        AddNewTransactionModel model = new Gson().fromJson(dataTransaction, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        String tanggalTransaksi = model.getCreatedAt();

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk: " + model.getTransactionId() + "\n");
            printText("Tanggal   : " + ubahTanggalTransaksi + "\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for (CartModel cartModel : DashboardFragment.cartModels) {
                String product = cartModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getSellingPrice() * cartModel.getQuantity());
                printText(nama + "\n");
                printText(String.format("%-18s %28s", qty + " x " + price, total) + "\n");
                if (cartModel.getAmount() < cartModel.getQuantity()*cartModel.getSellingPrice()) {
                    printText(String.format("%-18s %28s", "", "(-" + formatNumber.format(cartModel.getDiscount())) + ")\n");
                    discount = discount + cartModel.getDiscount();
                }

                sub_total = sub_total + (cartModel.getSellingPrice() * cartModel.getQuantity());
            }

            printText("================================================\n");
            printText(String.format("%-18s %28s", "" +
                            "Total Item : " + DashboardFragment.totalItem,
                    formatNumber.format(sub_total)) + "\n");
            if (discount > 0) {
                printText(String.format("%-18s %28s", "" +
                                "Total Diskon ",
                        "-" + formatNumber.format(discount)) + "\n");
            }
            printText(String.format("%-18s %28s", "" +
                            "Bayar Hutang",
                    formatNumber.format(credit)) + "\n");

            int amount = Integer.parseInt(model.getAmount()) + credit;
            printText(String.format("%-18s %28s", "" +
                            "Total Belanja",
                    formatNumber.format(amount)) + "\n");

            printNewLine();
            printText(String.format("%-18s %28s", "Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid()))) + "\n");
            printText(String.format("%-18s %28s", "Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change()))) + "\n");

            printNewLine();
//            printText(String.format("%-18s %13s","Hutang Sebelumnya",
//                    formatNumber.format(customer.getSaldo()))+"\n");
            printText(String.format("%-18s %28s", "Sisa Hutang", (
                    formatNumber.format(saldo - credit))) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("------------------------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }

    //print Customer Credit
    public static void printCustomerCredit(Activity activity, CustomerModel customerModel, String formattedDate) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText(customerModel.getCustomerName() + "\n");
            DecimalFormat money = new DecimalFormat("#,###,###");
            final int amount = customerModel.getCredit() - customerModel.getCreditPaid();
            printText("Tanggal : " + formattedDate + "\n");
            printText("Sisa Hutang Anda : Rp." + money.format(amount) + "\n");
            printText("================================\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCustomerCreditM(Activity activity, CustomerModel customerModel, String formattedDate) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText(customerModel.getCustomerName() + "\n");
            DecimalFormat money = new DecimalFormat("#,###,###");
            final int amount = customerModel.getCredit() - customerModel.getCreditPaid();
            printText("Tanggal : " + formattedDate + "\n");
            printText("Sisa Hutang Anda : Rp." + money.format(amount) + "\n");
            printText("================================\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCustomerCreditL(Activity activity, CustomerModel customerModel, String formattedDate) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText(customerModel.getCustomerName() + "\n");
            DecimalFormat money = new DecimalFormat("#,###,###");
            final int amount = customerModel.getCredit() - customerModel.getCreditPaid();
            printText("Tanggal : " + formattedDate + "\n");
            printText("Sisa Hutang Anda : Rp." + money.format(amount) + "\n");
            printText("================================================\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("------------------------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }

    //print Detail Transaction
    public static void printDetailTransaction(Activity activity, TransactionModel model, List<TransactionProductModel> models) {

        UserDetailModel userModels = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = userModels.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = userModels.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = userModels.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = userModels.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(model.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

        try {
            os = mBluetoothSocket.getOutputStream();

            printPhoto(R.drawable.toni_black, activity);
            printNewLine();

            printCustom(userModels.getShopName().toUpperCase(), 0, 1);
            printCustom(userModels.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk : " + model.getTransactionId() + "\n");
            printText("Pelanggan  : " + model.getCustomerName() + "\n");
            printText("Tanggal    : " + ubahTanggalTransaksi + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            for (TransactionProductModel transactionProductModel : models) {
                String product = transactionProductModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                DecimalFormatRupiah.changeFormat(activity);
                int qty = transactionProductModel.getQuantity();
                String price = formatNumber.format(transactionProductModel.getSellingPrice());
                String total = formatNumber.format(transactionProductModel.getAmount());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (transactionProductModel.getDiscount() > 0) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(transactionProductModel.getDiscount())) + ")\n");
                    discount = discount + transactionProductModel.getDiscount();
                }

                sub_total = sub_total + (transactionProductModel.getSellingPrice() * transactionProductModel.getQuantity());
            }
            printText("================================\n");
            printText(String.format("%-15s %16s", "Total",
                    formatNumber.format(model.getAmount())) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + userModels.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(userModels.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }
    public static void printDetailTransactionM(Activity activity, TransactionModel model, List<TransactionProductModel> models) {

        UserDetailModel userModels = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = userModels.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = userModels.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = userModels.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = userModels.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(model.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

        try {
            os = mBluetoothSocket.getOutputStream();

            printPhoto(R.drawable.toni_black, activity);
            printNewLine();

            printCustom(userModels.getShopName().toUpperCase(), 0, 1);
            printCustom(userModels.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk : " + model.getTransactionId() + "\n");
            printText("Pelanggan  : " + model.getCustomerName() + "\n");
            printText("Tanggal    : " + ubahTanggalTransaksi + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================\n");

            for (TransactionProductModel transactionProductModel : models) {
                String product = transactionProductModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                DecimalFormatRupiah.changeFormat(activity);
                int qty = transactionProductModel.getQuantity();
                String price = formatNumber.format(transactionProductModel.getSellingPrice());
                String total = formatNumber.format(transactionProductModel.getAmount());
                printText(nama + "\n");
                printText(String.format("%-17s %14s", qty + " x " + price, total) + "\n");
                if (transactionProductModel.getDiscount() > 0) {
                    printText(String.format("%-16s %14s", "", "(-" + formatNumber.format(transactionProductModel.getDiscount())) + ")\n");
                    discount = discount + transactionProductModel.getDiscount();
                }

                sub_total = sub_total + (transactionProductModel.getSellingPrice() * transactionProductModel.getQuantity());
            }
            printText("================================\n");
            printText(String.format("%-15s %16s", "Total",
                    formatNumber.format(model.getAmount())) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + userModels.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(userModels.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }
    public static void printDetailTransactionL(Activity activity, TransactionModel model, List<TransactionProductModel> models) {

        UserDetailModel userModels = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = userModels.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = userModels.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = userModels.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = userModels.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", lokal);
        Date date = null;
        try {
            date = dateFormat.parse(model.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", lokal);
        String ubahTanggalTransaksi = formatter.format(date);

        if (model.getPaymentType().equals("Cash")) {
            payment = "Tunai";
        } else {
            payment = "Hutang";
        }

        try {
            os = mBluetoothSocket.getOutputStream();

            printPhoto(R.drawable.toni_black, activity);
            printNewLine();

            printCustom(userModels.getShopName().toUpperCase(), 0, 1);
            printCustom(userModels.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printText("Kode Struk : " + model.getTransactionId() + "\n");
            printText("Pelanggan  : " + model.getCustomerName() + "\n");
            printText("Tanggal    : " + ubahTanggalTransaksi + "\n");
            printText("Pembayaran : " + payment + "\n");
            printText("================================================\n");

            for (TransactionProductModel transactionProductModel : models) {
                String product = transactionProductModel.getProductName();
                String nama;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_")
                            + "(" + StringUtils.substringAfterLast(product, "_") + ")";
                } else {
                    nama = product;
                }

                DecimalFormatRupiah.changeFormat(activity);
                int qty = transactionProductModel.getQuantity();
                String price = formatNumber.format(transactionProductModel.getSellingPrice());
                String total = formatNumber.format(transactionProductModel.getAmount());
                printText(nama + "\n");
                printText(String.format("%-18s %28s", qty + " x " + price, total) + "\n");
                if (transactionProductModel.getDiscount() > 0) {
                    printText(String.format("%-18s %28s", "", "(-" + formatNumber.format(transactionProductModel.getDiscount())) + ")\n");
                    discount = discount + transactionProductModel.getDiscount();
                }

                sub_total = sub_total + (transactionProductModel.getSellingPrice() * transactionProductModel.getQuantity());
            }
            printText("================================================\n");
            printText(String.format("%-18s %28s", "Total",
                    formatNumber.format(model.getAmount())) + "\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + userModels.getShopName(), 0, 1);

            printNewLine();
            printText("------------------------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(userModels.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }
    }

    //print Customer CreditPay
    public static void printCustomerCreditPay(Activity activity, CreditPayModel model, int credit) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = null;
//        try {
//            date = dateFormat.parse(model.getCreatedAt());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY"); //If you need time just put specific format for time like 'HH:mm:ss'
//        String ubahTanggalTransaksi = formatter.format(date);

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText("Kode Struk: " + model.getCreditPaidId() + "\n");
//            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("================================\n");
            printText(String.format("%-15s %16s", "Total Hutang", (
                    formatNumber.format(CustomerHutangActivity.customerModel.getSaldo()))) + "\n");
            printText(String.format("%-15s %16s", "Bayar Hutang", (
                    formatNumber.format(credit))) + "\n");

            printText("================================\n");
            printText(String.format("%-15s %16s", "Sisa Hutang", (
                    formatNumber.format(model.getSaldo()))) + "\n");
            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCustomerCreditPayM(Activity activity, CreditPayModel model, int credit) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = null;
//        try {
//            date = dateFormat.parse(model.getCreatedAt());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY"); //If you need time just put specific format for time like 'HH:mm:ss'
//        String ubahTanggalTransaksi = formatter.format(date);

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText("Kode Struk: " + model.getCreditPaidId() + "\n");
//            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("================================\n");
            printText(String.format("%-15s %16s", "Total Hutang", (
                    formatNumber.format(CustomerHutangActivity.customerModel.getSaldo()))) + "\n");
            printText(String.format("%-15s %16s", "Bayar Hutang", (
                    formatNumber.format(credit))) + "\n");

            printText("================================\n");
            printText(String.format("%-15s %16s", "Sisa Hutang", (
                    formatNumber.format(model.getSaldo()))) + "\n");
            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
    public static void printCustomerCreditPayL(Activity activity, CreditPayModel model, int credit) {

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]", "");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]", "");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]", "");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]", "");

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = null;
//        try {
//            date = dateFormat.parse(model.getCreatedAt());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY"); //If you need time just put specific format for time like 'HH:mm:ss'
//        String ubahTanggalTransaksi = formatter.format(date);

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(), 0, 1);
            printCustom(models.getStreet(), 0, 1);
            printCustom(villages + ", " + districts, 0, 1);
            printCustom(regencys, 0, 1);
            printCustom("", 0, 0);

            printNewLine();

            printText("Kode Struk: " + model.getCreditPaidId() + "\n");
//            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : " + model.getCustomerName() + "\n");
            printText("================================================\n");
            printText(String.format("%-18s %28s", "Total Hutang", (
                    formatNumber.format(CustomerHutangActivity.customerModel.getSaldo()))) + "\n");
            printText(String.format("%-18s %28s", "Bayar Hutang", (
                    formatNumber.format(credit))) + "\n");

            printText("================================================\n");
            printText(String.format("%-18s %28s", "Sisa Hutang", (
                    formatNumber.format(model.getSaldo()))) + "\n");
            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    + models.getShopName(), 0, 1);

            printNewLine();
            printText("------------------------------------------------\n");
            printCustom("Layanan Konsumen Toko", 0, 1);
            printCustom(models.getPhoneNumber(), 0, 1);
            printCustom("", 0, 0);

            printNewLine();
            printNewLine();

            os.flush();
        } catch (IOException ignored) {
        }

    }
}
