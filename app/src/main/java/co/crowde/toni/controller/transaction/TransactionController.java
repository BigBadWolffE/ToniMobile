package co.crowde.toni.controller.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.utils.print.PrinterNetwork;
import co.crowde.toni.view.activity.home.MainActivity;
import co.crowde.toni.view.activity.print.WaitingPrintTransactionActivity;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;
import co.crowde.toni.view.dialog.message.transaction.RePrintTransactionDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.utils.print.PrinterNetwork.resetConnection;

public class TransactionController {

    public static void printBill(Activity activity, String data, ProgressDialog progressDialog, String payment_type, String saldo, String credit){

        resetConnection();
        if(SavePref.readDeviceAddress(activity)!=null){
            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
            try {
                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                PrinterNetwork.mBluetoothSocket.connect();
                if (PrinterNetwork.mBluetoothSocket.isConnected()){
//                    TransactionRequest.postNewTransaction(activity);

                    switch (payment_type) {
                        case "Cash":
                            PrintController.printCash(activity, data);
                            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CASH_CREDIT_PRINT);

                            break;
                        case "Credit":
                            PrintController.printCredit(activity, data, Integer.parseInt(credit));
                            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CASH_PRINT);

                            break;
                        case "CashCredit":
                            PrintController.printCashCredit(activity, data, Integer.parseInt(saldo), Integer.parseInt(credit));
                            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CREDIT_PRINT);
                            break;
                    }

                    DashboardFragment.dbCart.deleteAllItem();
                    DashboardFragment.ifCartEmpty(activity);
                    Intent home = new Intent(activity, MainActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(home);
                    activity.finish();


                }
            } catch (IOException e) {
                PrinterConnectivityDialog.showDialog(activity);
//                ConfirmTransactionDialog.progressDialog.dismiss();
                progressDialog.dismiss();
                Log.e("Bluetooth","Can't Connect");
            }

        } else {
//            ConfirmTransactionDialog.progressDialog.dismiss();
            progressDialog.dismiss();
            PrinterNetwork.pairingBluetooth(activity);
        }
    }

    public static void printDetailTransaction(Activity activity, TransactionModel model, List<TransactionProductModel> productModels){

        resetConnection();
        if(SavePref.readDeviceAddress(activity)!=null){
            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
            try {
                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                PrinterNetwork.mBluetoothSocket.connect();
                if (PrinterNetwork.mBluetoothSocket.isConnected()){

                    PrintController.printDetailTransaction(activity, model, productModels);

                    Intent print = new Intent(activity, WaitingPrintTransactionActivity.class);
                    activity.startActivity(print);
                    activity.finish();
                }
            } catch (IOException e) {
                PrinterConnectivityDialog.showDialog(activity);
                RePrintTransactionDialog.progressDialog.dismiss();
                Log.e("Bluetooth","Can't Connect");
            }

        } else {
            PrinterNetwork.pairingBluetooth(activity);
            RePrintTransactionDialog.progressDialog.dismiss();
        }

    }

    public static void removeHistory(Activity activity){
        DashboardFragment.dbCart.deleteAllItem();
        DashboardFragment.cartModels.clear();
        DashboardFragment.ifCartEmpty(activity);

        DashboardFragment.productModels.clear();
        DashboardFragment.requestFilter(activity);
        ProductRequest.getProductList(activity);
    }


}
