package uk.ac.warwick.cim.signalApp;

import android.media.midi.MidiDeviceService;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiReceiver;

public class MidiSynthDeviceService extends MidiDeviceService {
    private static final String TAG = "MidiSynthDeviceService";
    private MySynthEngine mSynthEngine = new MySynthEngine();
    private boolean synthStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mSynthEngine.stop();
        super.onDestroy();
    }

    @Override
    // Declare the receivers associated with your input ports.
    public MidiReceiver[] onGetInputPortReceivers() {
        return new MidiReceiver[] { mSynthEngine };
    }

    /**
     * This will get called when clients connect or disconnect.
     * You can use it to turn on your synth only when needed.
     */
    @Override
    public void onDeviceStatusChanged(MidiDeviceStatus status) {
        if (status.isInputPortOpen(0) && !synthStarted) {
            mSynthEngine.start();
            synthStarted = true;
        } else if (!status.isInputPortOpen(0) && synthStarted){
            mSynthEngine.stop();
            synthStarted = false;
        }
    }
}

