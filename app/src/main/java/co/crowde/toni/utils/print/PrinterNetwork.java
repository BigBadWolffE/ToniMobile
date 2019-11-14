package co.crowde.toni.utils.print;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;

public class PrinterNetwork {

    //Bluetooth
    public static BluetoothAdapter mBluetoothAdapter= null;
    public static UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream os = null;
    public static String bluetoothAddress;

    //popup
    Dialog dialogPaperSize;

    public static void pairingBluetooth(final Activity activity) {
        ArrayList bluetoothList = new ArrayList();
        final ArrayList<String> bluetoothDeviceList = new ArrayList();
        resetConnection();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (Object device : pairedDevices) {
                mBluetoothDevice = (BluetoothDevice) device;
                bluetoothList.add(mBluetoothDevice.getName() + "\n" + mBluetoothDevice.getAddress());
                bluetoothDeviceList.add(mBluetoothDevice.getAddress());
            }
        }

        // show list
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Pilih Device");

        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.select_dialog_singlechoice,
                bluetoothList.toArray(new String[bluetoothList.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                final String deviceAddress = bluetoothDeviceList.get(position);

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(deviceAddress);

                try {
                    mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
                    mBluetoothSocket.connect();

                    Toast.makeText(activity, "Berhasil terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();


                    SavePref.saveDeviceAddress(activity, deviceAddress);
                    PopUpPaperSize.showDialog(activity);


                } catch (IOException e) {
                    try {
                        PrinterConnectivityDialog.showDialog(activity);
                        dialog.dismiss();
                        mBluetoothSocket.close();
                        Log.e("Bluetooth","Can't Connect");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        PrinterConnectivityDialog.showDialog(activity);
                        Log.e("Bluetooth","Socket can't closed");
                    }
                }

            }
        });
        alertDialog.show();

    }

    public static BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                return (BluetoothSocket) m.invoke(device, applicationUUID);
            } catch (Exception e) {
                Log.e(Const.TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(applicationUUID);
    }
    public static void resetConnection() {
        if (os != null) {
            try {os.close();} catch (Exception e) {}
            os = null;
        }
        if (mBluetoothSocket != null) {
            try {mBluetoothSocket.close();} catch (Exception e) {}
            mBluetoothSocket = null;
        }
    }

    public static void printPhoto(int img, Activity activity) {
        try {

            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(),
                    img);
            if(bmp!=null){
                byte[] command = UtilsImage.decodeBitmap(bmp);

                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
    public static void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] format = { 27, 50, 0 };
        byte[] cc = new byte[]{0x1D,0x21,0x00};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1D,0x21,0x01};  // 0- normal size text
        byte[] bb = new byte[]{0x1D,0x21,0x03};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1D,0x21,0x04}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1D,0x21,0x05}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    os.write(cc);
                    break;
                case 1:
                    os.write(cc1);
                    break;
                case 2:
                    os.write(bb);
                    break;
                case 3:
                    os.write(bb2);
                    break;
                case 4:
                    os.write(bb3);
                    break;
                case 5:
                    os.write(format);
            }

            switch (align){
                case 0:
                    //left align
                    os.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    os.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    os.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            os.write(msg.getBytes());
            os.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void printNewLine() {
        try {
            os.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void printText(String msg) {
        try {
            // Print normal text
            os.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void printText(byte[] msg) {
        try {
            // Print normal text
            os.write(PrinterCommands.ESC_ALIGN_CENTER);
            os.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
