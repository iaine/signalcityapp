package uk.ac.warwick.cim.signalApp;

import android.bluetooth.le.ScanResult;

public class Models {

    public void presenceModel (Tone signalTone) {
        signalTone.playTone("C4", false);
    }

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
     * @param distance - boolean to apply the distance
     */
    public void inLoopModel (Tone signalTone, boolean distance) {
        signalTone.playTone("C5", distance);
    }

    /**
     * Function to create the loop model to provide a heuristic model
     * of out of the loop.
     * @param distance - boolean to apply the distance
     */
    public void outLoopModel (Tone signalTone, boolean distance) {
        signalTone.playTone("G4", distance);
    }
    /**
     *  Function to check if the device has been seen before.
     *
     * @todo do we want to change to collection to support counting items?
     * @param repetition - repetition list
     * @param result - scan result
     */
    public void checkRepetition (Tone signalTone, Repetition repetition, ScanResult result) {
        String device = result.getDevice().toString();
        Integer idx = repetition.checkRepetition(device);
        if (idx >= 1) {
            signalTone.playTone("A3", false);
        }
    }

}
