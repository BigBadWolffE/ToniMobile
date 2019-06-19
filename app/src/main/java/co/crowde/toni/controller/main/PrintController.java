package co.crowde.toni.controller.main;

import android.app.Activity;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.DetailTransaksiModel;
import co.crowde.toni.model.TransaksiModel;
import co.crowde.toni.model.UserDetailModel;
import co.crowde.toni.model.response.object.AddNewTransactionModel;
import co.crowde.toni.model.response.object.CreditPayModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.view.fragment.cart.CartPayment;
import co.crowde.toni.view.fragment.modul.Dashboard;

import static co.crowde.toni.helper.DecimalFormatRupiah.formatNumber;
import static co.crowde.toni.utils.PrinterNetwork.mBluetoothSocket;
import static co.crowde.toni.utils.PrinterNetwork.os;
import static co.crowde.toni.utils.PrinterNetwork.printCustom;
import static co.crowde.toni.utils.PrinterNetwork.printNewLine;
import static co.crowde.toni.utils.PrinterNetwork.printPhoto;
import static co.crowde.toni.utils.PrinterNetwork.printText;

public class PrintController {


    public static void printCash(Activity activity, String data){
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]","");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]","");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]","");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]","");

        String tanggalTransaksi = model.getCreatedAt();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(),0,1);
            printCustom(models.getStreet(),0,1);
            printCustom(villages+", "+districts,0,1);
            printCustom(regencys,0,1);
            printCustom("",0,0);

            printNewLine();
            printText("Kode Struk: "+model.getTransactionId()+"\n");
            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
//                                    printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : "+model.getCustomerName()+"\n");
            printText("================================\n");

            for(CartModel cartModel : Dashboard.cartModels){
                String product = cartModel.getProductName();
                String nama;
                if(product.contains("_")){
                    nama = StringUtils.substringBeforeLast(product, "_")
                            +"("+StringUtils.substringAfterLast(product, "_")+")";
                } else {
                    nama = product;
                }

                DecimalFormatRupiah.changeFormat(activity);
                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getAmount());
                printText(nama+"\n");
                printText(String.format("%-17s %14s",qty+" x "+price,total)+"\n");
            }

            printText("================================\n");
            printText(String.format("%-18s %13s","" +
                            "Total Item : "+Dashboard.totalItem,
                    formatNumber.format(Integer
                            .parseInt(model.getAmount())))+"\n");

            printNewLine();
            printText(String.format("%-10s %21s","Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid())))+"\n");
            printText(String.format("%-10s %21s","Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change())))+"\n");

            printNewLine();
            printNewLine();
            printCustom("Terima Kasih telah membeli\nproduk pertanian di\nToko "
                    +models.getShopName(),0,1);

            printNewLine();
            printText("--------------------------------\n");
            printCustom("Layanan Konsumen Toko",0,1);
            printCustom(models.getPhoneNumber(),0,1);
            printCustom("",0,0);

            printNewLine();
            printNewLine();

            os.flush();

        } catch (IOException e) {
        }

    }

    public static void printCredit(Activity activity, String data, CustomerModel credit){
        AddNewTransactionModel model = new Gson().fromJson(data, AddNewTransactionModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]","");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]","");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]","");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]","");

        String tanggalTransaksi = model.getCreatedAt();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(),0,1);
            printCustom(models.getStreet(),0,1);
            printCustom(villages+", "+districts,0,1);
            printCustom(regencys,0,1);
            printCustom("",0,0);

            printNewLine();
            printText("Kode Struk: "+model.getTransactionId()+"\n");
            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
//                                    printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : "+model.getCustomerName()+"\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for(CartModel cartModel : Dashboard.cartModels){
                String product = cartModel.getProductName();
                String nama;
                if(product.contains("_")){
                    nama = StringUtils.substringBeforeLast(product, "_")
                            +"("+StringUtils.substringAfterLast(product, "_")+")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getAmount());
                printText(nama+"\n");
                printText(String.format("%-17s %14s",qty+" x "+price,total)+"\n");
            }

            printText("Hutang Sebelumnya\n");
            printText(String.format("%-17s %14s", "1 x "
                            + formatNumber.format(credit.getSaldo()),
                    formatNumber.format(credit.getSaldo())) + "\n");

            printText("================================\n");
            final int totalCredit = Dashboard.totalAmount
                    + credit.getSaldo();
            String ubahTotal = String.valueOf(totalCredit);
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + (Dashboard.totalItem + 1),
                    formatNumber.format(Integer.parseInt(ubahTotal))) + "\n");

            printNewLine();
            printText(String.format("%-15s %16s", "Total Hutang",
                    formatNumber.format(Integer.parseInt(ubahTotal))) + "\n");

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
        }catch (IOException e) {
        }
    }

    public static void printCashCredit(Activity activity, String dataTransaction){
        AddNewTransactionModel model = new Gson().fromJson(dataTransaction, AddNewTransactionModel.class);

//        CreditPayModel creditPay = new Gson().fromJson(credit, CreditPayModel.class);

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        CustomerModel customer = new Gson().fromJson(SavePref.readCustomer(activity), CustomerModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]","");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]","");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]","");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]","");

        String tanggalTransaksi = model.getCreatedAt();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(tanggalTransaksi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
        String ubahTanggalTransaksi = formatter.format(date);
        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(),0,1);
            printCustom(models.getStreet(),0,1);
            printCustom(villages+", "+districts,0,1);
            printCustom(regencys,0,1);
            printCustom("",0,0);

            printNewLine();
            printText("Kode Struk: "+model.getTransactionId()+"\n");
            printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
//                                    printText("Tanggal   : "+ubahTanggalTransaksi+"\n");
            printText("Pelanggan : "+model.getCustomerName()+"\n");
            printText("================================\n");

            DecimalFormatRupiah.changeFormat(activity);

            for(CartModel cartModel : Dashboard.cartModels){
                String product = cartModel.getProductName();
                String nama;
                if(product.contains("_")){
                    nama = StringUtils.substringBeforeLast(product, "_")
                            +"("+StringUtils.substringAfterLast(product, "_")+")";
                } else {
                    nama = product;
                }

                int qty = cartModel.getQuantity();
                String price = formatNumber.format(cartModel.getSellingPrice());
                String total = formatNumber.format(cartModel.getAmount());
                printText(nama+"\n");
                printText(String.format("%-17s %14s",qty+" x "+price,total)+"\n");
            }

            printText("================================\n");
            printText(String.format("%-18s %13s","Bayar Hutang",
                    formatNumber.format(CartPayment.creditPay))+"\n");

            printText("================================\n");
            final int totalHutangSekarang = CartPayment.totalBill;
            String ubahTotal = String.valueOf(totalHutangSekarang);
            printText(String.format("%-18s %13s", "" +
                            "Total Item : " + (Dashboard.totalItem + 1),
                    formatNumber.format(Integer.parseInt(ubahTotal))) + "\n");

            printNewLine();
            printText(String.format("%-10s %21s","Tunai",
                    formatNumber.format(Integer
                            .parseInt(model.getPaid())))+"\n");
            printText(String.format("%-10s %21s","Kembali",
                    formatNumber.format(Integer
                            .parseInt(model.get_change())))+"\n");

            printNewLine();
            printText(String.format("%-18s %13s","Hutang Sebelumnya",
                    formatNumber.format(customer.getSaldo()))+"\n");
            printText(String.format("%-15s %16s","Total Hutang",(
                    formatNumber.format(customer.getSaldo()-CartPayment.creditPay)))+"\n");

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
        }catch (IOException e) {
        }

    }

    public static void printCustomerCredit(Activity activity, CustomerModel customerModel){

        UserDetailModel models = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = models.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]","");
        String district = models.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]","");
        String regency = models.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]","");
        String province = models.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]","");

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(models.getShopName().toUpperCase(),0,1);
            printCustom(models.getStreet(),0,1);
            printCustom(villages+", "+districts,0,1);
            printCustom(regencys,0,1);
            printCustom("",0,0);

            printNewLine();

            printText(customerModel.getCustomerName()+"\n");
            DecimalFormat money = new DecimalFormat("#,###,###");
            final int amount = customerModel.getCredit() - customerModel.getCreditPaid();
            printText("Sisa Hutang Anda : Rp."+money.format(amount)+"\n");
            printText("--------------------------------\n");

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
        }catch (IOException e) {
        }

    }

    public static void printDetailTransaction(Activity activity, TransaksiModel model, List<DetailTransaksiModel> models , String formattedDate, String jenisBayar){
        UserDetailModel userModels = new Gson().fromJson(SavePref.readUserDetail(activity), UserDetailModel.class);

        String village = userModels.getVillage();
        String villages = village.replaceAll("[^A-Za-z ]","");
        String district = userModels.getDistrict();
        String districts = district.replaceAll("[^A-Za-z ]","");
        String regency = userModels.getRegency();
        String regencys = regency.replaceAll("[^A-Za-z ]","");
        String province = userModels.getProvince();
        String provinces = province.replaceAll("[^A-Za-z ]","");

        try {
            os = mBluetoothSocket.getOutputStream();

            printNewLine();
            printPhoto(R.drawable.toni_black, activity);

            printCustom(userModels.getShopName().toUpperCase(),0,1);
            printCustom(userModels.getStreet(),0,1);
            printCustom(villages+", "+districts,0,1);
            printCustom(regencys,0,1);
            printCustom("",0,0);

            printNewLine();
            printText("Nomor Transaksi : "+ model.getTransactionId()+"\n");
            printText("Pelanggan : "+ model.getCostumerName()+"\n");
            printText("--------------------------------\n");
            printText("Tanggal : "+ formattedDate+"\n");
            printText("--------------------------------\n");
            printText("Pembayaran: "+ jenisBayar +"\n");
            printText("--------------------------------\n");

            for(DetailTransaksiModel modelss : models  ){
                int sellingprice = Integer.parseInt(modelss.getSellingPrice());
                printText(modelss.getProductName()+"\n");
                printText(String.format("%2s %1s %1s ",modelss.getQuantity(), "X","Rp."
                        +DecimalFormatRupiah.formatNumber.format(sellingprice)+"\n"));

            }
            printText("-------------------------------\n");
            int amount = Integer.parseInt(model.getAmount());
            printText("Total  : "+"Rp."+DecimalFormatRupiah.formatNumber.format(amount));
            printNewLine();
            printText("--------------------------------\n");

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
        } catch (IOException e) {
        }
    }
}
