package uk.ac.warwick.cim.signalApp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.content.Context;


import java.io.File;
import java.util.List;

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

    private Repetition repetition;

    private Models model;

    private ModelState mState;

    public BluetoothLE(File fileName, Tone tone, ModelState modelState) {
        fName = fileName;
        signalTone = tone;
        mState = modelState;
        this.initBluetoothDetails();
    }

    private void initBluetoothDetails() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) mContext).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        repetition = new Repetition();

        model = new Models();

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
                    Log.i("phoned", details.toString());
                    String data = System.currentTimeMillis()
                            + ", " + result.getDevice()
                            + ", " + result.getRssi()
                            + ", " + details.getDeviceName()
                            + ", " + details.getManufacturerSpecificData().toString()
                            + ", " + details.getTxPowerLevel()
                            + ", " + details.getAdvertiseFlags();

                    //test for APIs. If less than 26, we lose some fields.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        data += ", " + result.getPeriodicAdvertisingInterval()
                            + ", " + result.getPrimaryPhy()
                            + ", " + result.getSecondaryPhy();

                    }
                    int serviceOffered = 0;

                    if (details.getServiceUuids() != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            serviceOffered = details.getServiceSolicitationUuids().size();
                        }

                    }
                    data += ", " + serviceOffered;

                    data = data + "\n";
                    //test for the distance call being set.
                    boolean distance = mState.distance;

                    if (mState.covid) {
                        //test for covid id here
                        if (serviceID(details).toLowerCase().contains("fdf6")) {
                            model.covidBeaconModel(signalTone);
                        }
                    }
                    if (mState.inLoop) {
                        model.inLoopModel(signalTone, distance, serviceOffered);
                    }

                    if (mState.outLoop) {
                        model.inLoopModel(signalTone, distance, serviceOffered);
                    }

                    if (mState.repetition) {
                        model.checkRepetition(signalTone, repetition, result);
                    }

                    //writeData(data);
                }
            };

    private  String serviceID (ScanRecord r) {
        String serviceUid = "No service";
        List<ParcelUuid> uids =  r.getServiceUuids();

        if (uids != null) {

            //List<ParcelUuid> uids = serviceID(details);
            StringBuilder ids = new StringBuilder();
            for (ParcelUuid pUid: uids) {
                serviceUid = pUid.getUuid().toString();

                ids.append(pUid.getUuid().toString() + ",");
                Log.i(TAG, "UUID " + serviceUid + " file " + pUid.describeContents());
            }
            serviceUid = ids.toString().replaceAll(",$", "");;
        }
    Log.i("phoned", serviceUid);
        return serviceUid;
    }

    private void writeData (String data) {
        try {
            new FileConnection(fName).writeFile(data);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

}

