package uk.ac.warwick.cim.signalApp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.content.Context;


import java.io.File;

/**
 * Check the API levels in the App?
 */


public class BluetoothLE {

    private static final String TAG = "BLUETOOTH";

    private static final int REQUEST_ENABLE_BT = 104;

    private final File fName;

    private Context mContext;

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner bluetoothLeScanner;

    private boolean scanning;

    private Handler handler = new Handler();

    public Tone signalTone;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 2000;

    public BluetoothLE(File fileName, Tone tone) {
        fName = fileName;
        signalTone = tone;
        this.initBluetoothDetails();
    }

    private void initBluetoothDetails() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) mContext).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        this.scanLeDevice();

    }

    private void scanLeDevice() {
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if(bluetoothLeScanner != null) {
            if (!scanning) {
                // Stops scanning after a pre-defined scan period.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanning = false;
                        bluetoothLeScanner.stopScan(leScanCallback);
                    }
                }, SCAN_PERIOD);

                scanning = true;
                bluetoothLeScanner.startScan(leScanCallback);
            } else {
                scanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        }
    }

    // Device scan callback.
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    ScanRecord details = result.getScanRecord();
                    String data;
                    //test for APIs. If less than 26, we lose some fields.
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        data = System.currentTimeMillis()
                            + ", " + result.getDevice()
                            + ", " + result.getRssi()
                            + ", " + result.getPrimaryPhy()
                            + ", " + result.getSecondaryPhy()
                            + ", " + result.getPeriodicAdvertisingInterval()
                            + ", " + details.getDeviceName()
                            + ", " + details.getManufacturerSpecificData().toString()
                            + ", " + details.getTxPowerLevel()
                            + ", " + details.getAdvertiseFlags()
                            + "\n";
                    } else {
                        data = System.currentTimeMillis()
                            + ", " + result.getDevice()
                            + ", " + result.getRssi()
                            + ", " + details.getDeviceName()
                            + ", " + details.getManufacturerSpecificData().toString()
                            + ", " + details.getTxPowerLevel()
                            + ", " + details.getAdvertiseFlags()
                            + "\n";
                }
                    signalTone.playTone();
                    //writeData(data);
                }
            };

    private void writeData (String data) {
        try {
            new FileConnection(fName).writeFile(data);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

}

