package uk.ac.warwick.cim.signalApp;

import android.media.midi.MidiReceiver;

import java.io.IOException;

public class MySynthEngine extends MidiReceiver {
    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {

    }
    public void start () {}
    public void stop () {}
}

