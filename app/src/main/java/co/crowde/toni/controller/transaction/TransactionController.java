package co.crowde.toni.controller.transaction;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.utils.print.PrinterNetwork;
import co.crowde.toni.view.activity.print.WaitingCreditPayActivity;
import co.crowde.toni.view.activity.print.WaitingPrintActivity;
import co.crowde.toni.view.activity.print.WaitingPrintTransactionActivity;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPrintTransactionDialog;
import co.crowde.toni.view.dialog.message.transaction.ConfirmTransactionDialog;
import co.crowde.toni.view.dialog.message.transaction.RePrintTransactionDialog;
import co.crowde.toni.view.fragment.cart.CartPaymentFragment;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.utils.print.PrinterNetwork.resetConnection;

public class TransactionController {

    public static void printBill(Activity activity, String data){

        final CustomerModel credit = new Gson().fromJson(SavePref.readCustomer(activity), CustomerModel.class);

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

                    if(CartPaymentFragment.cash){
                        PrintController.printCash(activity, data);
                    } else if (CartPaymentFragment.credit) {
                        PrintController.printCredit(activity, data, credit);
                    } else if (CartPaymentFragment.cashCredit) {
                        PrintController.printCashCredit(activity, data);
                        CustomerRequest.payCredit(activity);
                    }

                    Intent print = new Intent(activity, WaitingPrintActivity.class);
                    activity.startActivity(print);
                    activity.finish();


                }
            } catch (IOException e) {
                PrinterConnectivityDialog.showDialog(activity);
//                ConfirmTransactionDialog.progressDialog.dismiss();
                ConfirmPrintTransactionDialog.progressDialog.dismiss();
                Log.e("Bluetooth","Can't Connect");
            }

        } else {
//            ConfirmTransactionDialog.progressDialog.dismiss();
            ConfirmPrintTransactionDialog.progressDialog.dismiss();
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

        SavePref.saveCustomer(activity, null);
        SavePref.saveCustomerId(activity, null);
    }


}
