package uk.ac.warwick.cim.signalApp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;

/**
 * Class to handle tone creation
 *  todo Really might want to make this configurable
 */

public class Tone {
    private final double[] mSound;

    private short[] mBuffer;

    private final int toneduration = 44100;

    HashMap<String, short[]> tones = new HashMap<>();


    protected Tone() {
        this.mSound = new double[4410];
        this.mBuffer = new short[toneduration];
        // AudioTrack definition
        //this.createTone(440, toneduration);
        tones.put("A2", this.createTone(116, toneduration));
        tones.put("A3", this.createTone(220, toneduration));
        tones.put("C4", this.createTone(261.63 , toneduration));
        tones.put("G4", this.createTone(392 , toneduration));
        tones.put("C5", this.createTone(523.25 , toneduration));

    }

    /**
     * Method to create the tone and buffer on initialisation
     * @param frequency frequency of the note in Hertz, not MIDI
     * @param duration  the duration of the tone
     */
    private short[] createTone(double frequency, int duration) {
        mBuffer = new short[duration];
        // Sine wave
        for (int i = 0; i < this.mSound.length; i++) {
            this.mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            this.mBuffer[i] = (short) (this.mSound[i]*Short.MAX_VALUE);
        }
        return mBuffer;
    }

    public short[] getTone (String note) {
        return tones.get(note);
    }

    /**
     * Method to play the tone when a signal is found.
     * For now, limited to Geiger counter style. Really ought to make it
     * more aesthetic.
     */
    public void playTone(String freq, boolean distance) {
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack =  new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        mAudioTrack.play();

        short[] lBuffer = tones.get(freq);
        mAudioTrack.write(lBuffer, 0, this.mSound.length);

        if (distance && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //@todo change to the RSSI/TX
            mAudioTrack.setVolume(AudioTrack.getMaxVolume());
        }
        mAudioTrack.stop();
        mAudioTrack.release();
    }


}
