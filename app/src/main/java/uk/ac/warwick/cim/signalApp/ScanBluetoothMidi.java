package uk.ac.warwick.cim.signalApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.UUID;



public class ScanBluetoothMidi {

    public ArrayList<BluetoothDevice> bleDevices;

    private BluetoothAdapter mBluetoothAdapter;

    private Handler mHandler;

    private boolean mScanning;

    private static final UUID[] MIDI_UUIDS = new UUID[] {
            UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700")
    };

    protected ScanBluetoothMidi () {
        bleDevices = new ArrayList<BluetoothDevice>();
    }

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 2 seconds.
    private static final long SCAN_PERIOD = 2000;

    public void startScanningLeDevices() {
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);

        mScanning = true;
        mBluetoothAdapter.startLeScan(MIDI_UUIDS, mLeScanCallback);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    bleDevices.add(device);
                }
            };
}
