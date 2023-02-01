package uk.ac.warwick.cim.signalApp;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class Repetition {
    ArrayList repeat;
    protected Repetition () {
        this.repeat = new ArrayList();
    }

    private void add (String device) {
        if (this.repeat.size() == 100) {
            this.repeat.remove(0);
        }
        this.repeat.add(device);
    }

    private Integer lastIndex (String device) {
        Integer idx = this.repeat.lastIndexOf(device);
        return idx;
    }

    public Integer checkRepetition (BluetoothDevice device) {
        String dev = device.getAddress().toString();
        Integer idx = this.lastIndex(dev);
        this.add(dev);
        return idx;
    }
}
