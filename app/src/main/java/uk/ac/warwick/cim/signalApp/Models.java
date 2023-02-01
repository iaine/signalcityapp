package uk.ac.warwick.cim.signalApp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

import androidx.lifecycle.ReportFragment;

public class Models {

    /**
     * Function to sonify the Covid model
     *
     */
    public void covidBeaconModel (Tone signalTone) {
        signalTone.playTone("A2", false);
    }

    /**
     * Function to create the loop model to provide a heuristic model
     * of in or out of the loop.
     * @param distance
     */
    public void inLoopModel (Tone signalTone, boolean distance, Integer serviceOffered) {
        signalTone.playTone("C4", distance);
        signalTone.playTone("G4", distance);

        if (serviceOffered > 0) {
            signalTone.playTone("C5", distance);
        }
    }

    /**
     *  Function to check if the device has been seen before.
     *
     * @todo do we want to change to collection to support counting items?
     * @param repetition
     * @param result
     */
    public void checkRepetition (Tone signalTone, Repetition repetition, ScanResult result) {
        Integer idx = repetition.checkRepetition(result.getDevice());
        if (idx >= 1) {
            signalTone.playTone("A3", false);
        }
    }

}
