package uk.ac.warwick.cim.signalApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

/**
 * @todo: think about removing the location part for sonification?
 *
 * If we remove, then we need to work out how to use service to run every
 * few seconds (5?)
 */

public class MainActivity extends AppCompatActivity {

    private File signalFile;

    private static boolean handlerflag=false;

    private static final String TAG = "MAINACC";

    private Handler handler = new Handler();

    private Tone tone = new Tone();

    private ModelState modelState = new ModelState();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "Location Permissions error");

        handlerflag = true;
        // in case we do find any new organisations
        String currentName = "signal_" + System.currentTimeMillis() + ".txt";
        signalFile = this.createDataFile(currentName);
        Log.i(TAG, "Created file");

        this.setUpScan(tone);
    }

    private void setUpScan (Tone tone)  {
        Runnable runnable = new Runnable() {
            //put the handler on a timer - every few seconds & make configurable?
            public void run() {
                Log.i(TAG, "In the runner");
                /**
                 * Open the companies file here and pass through.
                 * Use to sonify and to get any new data.
                 */
                new BluetoothLE(signalFile, tone);
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

    public void repetitionModel () {
        boolean state = (!modelState.repetition) ? true : false;
        modelState.changeState("repetition", state);
    }

    public void distantModel () {
        boolean state = (!modelState.distance) ? true : false;
        modelState.changeState("distance", state);
    }

    public void covidModel () {
        boolean state = (!modelState.covid) ? true : false;
        modelState.changeState("covid", state);
    }

    public void inLoopModel () {
        boolean state = (!modelState.inLoop) ? true : false;
        modelState.changeState("inloop", state);
    }
    public void outLoopModel () {
        boolean state = (!modelState.outLoop) ? true : false;
        modelState.changeState("outloop", state);
    }

}
