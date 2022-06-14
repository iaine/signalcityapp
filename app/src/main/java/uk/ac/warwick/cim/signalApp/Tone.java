package uk.ac.warwick.cim.signalApp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

/**
 * Class to handle tone creation
 * @todo: Really might want to make this configurable
 */

public class Tone {
    private double[] mSound;

    private short[] mBuffer;

    private final int toneduration = 44100;


    public Tone() {
        this.mSound = new double[4410];
        this.mBuffer = new short[toneduration];
        // AudioTrack definition
        this.createTone(440, toneduration);

    }

    /**
     * Method to create the tone and buffer on initialisation
     * @param frequency
     * @param duration
     */
    private void createTone(double frequency, int duration) {
        // Sine wave
        for (int i = 0; i < this.mSound.length; i++) {
            this.mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            this.mBuffer[i] = (short) (this.mSound[i]*Short.MAX_VALUE);
        }
    }

    /**
     * Method to play the tone when a signal is found.
     * For now, limited to Geiger counter style. Really ought to make it
     * more aesthetic.
     */
    public void playTone() {
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack =  new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);


        mAudioTrack.play();

        mAudioTrack.write(this.mBuffer, 0, this.mSound.length);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAudioTrack.setVolume(AudioTrack.getMaxVolume());
        }
        mAudioTrack.stop();
        mAudioTrack.release();
    }

    /**
     * Method to play the tone when a signal is found.
     * Using a
     */
    public void playTone(double distance) {
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack =  new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);


        mAudioTrack.play();

        mAudioTrack.write(this.mBuffer, 0, this.mSound.length);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("BLUEZ", "Str distance" + distance);
            Log.i("BLUEZ", "max " + AudioTrack.getMaxVolume());
            mAudioTrack.setVolume((float) distance);
        }

        mAudioTrack.stop();
        mAudioTrack.release();
    }

    public double setVolume(int rssi, int txPower) {

        double currentDistance = this.getDistance(rssi,txPower);

        //Audio Volume is capped at 1. Farther away is quieter
        double volume = 1.0 - (currentDistance/10.0);

        //set a floor so that we can hear something for a device.
        if (volume < 0.1) volume = 0.1;

        return volume;
    }

    /**
     * Function to get distance of the device from the app
     * to the RSSI and txPower parts.
     *
     * It does assume a straight line and no walls or signal drop outs.
     *
     * @param rssi
     * @param txPower
     * @return
     */
    public double getDistance(int rssi, int txPower) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */

        return Math.pow(10d, ((double) txPower - (rssi)) / (10 * 2));
    }

    public double getDistance(int rssi) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */

        if (rssi > 0) {
            return 1 * ((double) rssi / 100.0);
        }

        return 1 * (-((double) rssi) / 100.0);
    }
}
