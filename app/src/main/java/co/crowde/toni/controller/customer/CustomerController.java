package co.crowde.toni.controller.customer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.response.object.CreditPayModel;
import co.crowde.toni.utils.print.PopUpPaperSize;
import co.crowde.toni.utils.print.PrinterNetwork;
import co.crowde.toni.view.dialog.message.customer.CreditPayDialog;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;

import static co.crowde.toni.utils.print.PrinterNetwork.resetConnection;

public class CustomerController {

    public static void printCreditPay(Activity activity, int credit, CreditPayModel model){
        resetConnection();
        if(SavePref.readDeviceAddress(activity)!=null){
            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
            try {
                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                PrinterNetwork.mBluetoothSocket.connect();
                if (PrinterNetwork.mBluetoothSocket.isConnected()){
                    if(SavePref.readPaperSize(activity).equals("small")){
                        PrintController.printCustomerCreditPay(activity, model, credit);
                    }else if (SavePref.readPaperSize(activity).equals("large")){
                        PrintController.printCustomerCreditPayL(activity,model,credit);
                    }else if (SavePref.readPaperSize(activity)== null){
                        Toast.makeText(activity,"Ukuran Kertas Belum Dipilih",Toast.LENGTH_LONG).show();
                        PopUpPaperSize.showDialog(activity);
                    }

                }
            } catch (IOException e) {
                PrinterConnectivityDialog.showDialog(activity);
                CreditPayDialog.progressDialog.dismiss();
                Log.e("Bluetooth","Can't Connect");
            }

        } else {
            CreditPayDialog.progressDialog.dismiss();
            PrinterNetwork.pairingBluetooth(activity);
        }
    }
}
