package uk.ac.warwick.cim.signalApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * todo: think about removing the location part for sonification?
 *
 * If we remove, then we need to work out how to use service to run every
 * few seconds (5?)
 */

public class MainActivity extends AppCompatActivity {

    private File signalFile;

    private File wifiFile;

    private static boolean handlerflag=false;

    private static final String TAG = "MAINACC";

    private final Handler handler = new Handler();

    private final Tone tone = new Tone();

    private BroadcastReceiver receiver;

    private MidiManager midiManager;

    private final int index = 0;

    public ArrayList<MidiDevice> midiDevice;

    public SoundState sound;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "Location Permissions error");

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
           Log.i("MIDI", "midi not supported ");
           //@todo: needs to be a toast message
            Toast.makeText(this, "MidiManager is null!", Toast.LENGTH_LONG)
                    .show();
        }

        //set an initial layer
        sound = new SoundState();
        sound.setState(SoundState.SoundStates.TONES);

        midiManager  = (MidiManager)this.getSystemService(Context.MIDI_SERVICE);
        midiDevice = new ArrayList<>();


        // Set up for Android T.
        /*Collection<MidiDeviceInfo> universalDeviceInfos = midiManager.getDevicesForTransport(
                MidiManager.TRANSPORT_UNIVERSAL_MIDI_PACKETS);*/


        /*MidiOutputPort outputPort = device.openOutputPort(index);
        outputPort.connect(new MySynthEngine());*/

        handlerflag = true;
        // in case we do find any new organisations
        String currentName = "signal_" + System.currentTimeMillis() + ".txt";
        signalFile = this.createDataFile(currentName);

        wifiFile = this.createDataFile("wifi_" + System.currentTimeMillis() + ".txt");
        Log.i(TAG, "Created file");

        this.setUpWifiScan(tone);
        this.setUpScan(tone);
        this.setUpBluetoothScan();
    }

    public void changeSound (View view) {
        // refactor to .equals() not ==
        if (sound.getState().equals(SoundState.SoundStates.TONES)) {
            sound.setState(SoundState.SoundStates.MIDI);
            Toast.makeText(this, "Using Midi", Toast.LENGTH_LONG);
        } else {
            sound.setState(SoundState.SoundStates.TONES);
            Toast.makeText(this, "Using Tones", Toast.LENGTH_LONG);
        }
    }

    public void setUpMidi (View view) {
        MidiDeviceInfo[] infos = midiManager.getDevices();

        for (MidiDeviceInfo info: infos) {
            int numInputs = info.getInputPortCount();
            int numOutputs = info.getOutputPortCount();
            Bundle properties = info.getProperties();
            String manufacturer = properties
                    .getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);



            midiManager.openDevice(info, device -> {
                if (device == null) {
                    Log.e(TAG, "could not open device " + info);
                } else {
                    midiDevice.add(device);
                    //MidiInputPort inputPort = device.openInputPort(index);
                    //sendMidiNote(inputPort);
                }

            }, new Handler(Looper.getMainLooper()));
        }
    }

    public void setUpBluetoothConnection (View view) {
        //set up the scan connection
        ScanBluetoothMidi scanBluetoothMidi = new ScanBluetoothMidi();
        if (scanBluetoothMidi.bleDevices.size() < 1) {
            Toast.makeText(this, "No Bluetooth Devices discovered" , Toast.LENGTH_LONG)
                    .show();
        }

        for (BluetoothDevice bleDevice: scanBluetoothMidi.bleDevices) {
            midiManager.openBluetoothDevice(bleDevice, device -> {
                if (device == null) {
                    Log.e(TAG, "could not open device ");
                } else {
                    MidiInputPort inputPort = device.openInputPort(index);
                    Log.i("MIDI", "port found " + inputPort);
                }

            }, new Handler(Looper.getMainLooper()));
        }

    }

    /**
     * Set up the Bluetooth scanning
     */
    private void setUpBluetoothScan () {
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    System.out.println("Found device " + deviceName + " with addy " + deviceHardwareAddress);
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    /**
     * Runnable to set up
     * @param tone - set up tone
     */
    private void setUpWifiScan (Tone tone) {
        Runnable runnable = new Runnable() {
            //put the handler on a timer - every few seconds & make configurable?
            public void run() {
                Log.i(TAG, "In the runner");
                /*
                 * Open the companies file here and pass through.
                 * Use to sonify and to get any new data.
                 */
                new WifiDetails(wifiFile, sound);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private void setUpScan (Tone tone)  {
        Runnable runnable = new Runnable() {
            //put the handler on a timer - every few seconds & make configurable?
            public void run() {
                Log.i(TAG, "In the runner");
                /*
                 * Open the companies file here and pass through.
                 * Use to sonify and to get any new data.
                 */
                new BluetoothLE(signalFile, tone, sound);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private void checkPermissions(String accessFineLocation, String s) {
        //Get permissions to find location
        if (ContextCompat.checkSelfPermission(MainActivity.this, accessFineLocation)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSIONS", s);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    accessFineLocation)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{accessFineLocation}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{accessFineLocation}, 1);
            }
        }
    }

   @Override
    protected void onResume() {
        super.onResume();
        handlerflag = false;
        handler.removeCallbacks(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handlerflag = true;
        this.setUpScan(tone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (signalFile.length() == 0) {
            signalFile.delete();
        }
        unregisterReceiver(receiver);
    }

    private File createDataFile(String fileName) {
        File fName = new File(this.getExternalFilesDir(null), fileName);

        if (!fName.exists()) {
            try {
                final boolean newFile = fName.createNewFile();
                if (!newFile) Log.i("FILE", fileName + " not created");
            } catch (IOException e) {
                Log.i("FILE",e.toString());
            }
        }

        return fName;
    }
}
