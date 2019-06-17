package co.crowde.toni.controller.transaction;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import co.crowde.toni.helper.SavePref;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.utils.PrinterNetwork;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;
import co.crowde.toni.view.dialog.message.transaction.ConfirmTransactionDialog;
import co.crowde.toni.view.fragment.modul.Dashboard;

import static co.crowde.toni.utils.PrinterNetwork.resetConnection;

public class TransactionController {

    public static void printBill(Activity activity){
        resetConnection();
        if(SavePref.readDeviceAddress(activity)!=null){
            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
            try {
                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                PrinterNetwork.mBluetoothSocket.connect();
                if (PrinterNetwork.mBluetoothSocket.isConnected()){

                    TransactionRequest.postNewTransaction(activity);

                }
            } catch (IOException e) {
//                Toast.makeText(activity, "Tidak dapat terhubung dengan Bluetooth Printer.", Toast.LENGTH_SHORT).show();
                PrinterConnectivityDialog.showDialog(activity);
                ConfirmTransactionDialog.progressDialog.dismiss();
                Log.e("Bluetooth","Can't Connect");
            }

        } else {
            ConfirmTransactionDialog.progressDialog.dismiss();
            PrinterNetwork.pairingBluetooth(activity);
        }
    }

    public static void removeHistory(Activity activity){
        Dashboard.dbCart.deleteAllItem();
        Dashboard.cartModels.clear();
        Dashboard.ifCartEmpty(activity);

        Dashboard.productModels.clear();
        Dashboard.requestFilter(activity);
        ProductRequest.getProductList(activity);

        SavePref.saveCustomer(activity, null);
        SavePref.saveCustomerId(activity, null);
    }


}
