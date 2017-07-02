package ca.uwaterloo.lab4_201_04;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Initializes components which are displayed on the app screen.
 */
public class Lab4_201_04 extends AppCompatActivity {

    final static int WIN_VALUE = 64;
    final static int BLOCK_APPEAR_RATE_IN_MILLI_SECONDS = 2500;
    final static int[] BLOCKS_THAT_CAN_SPAWN = {1, 2};

    private boolean isGameOfficiallyDone;

    /**
     * Called when app starts to initialize components.
     *
     * @param savedInstanceState passed into the super method which is overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_201_04);

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        final Grid grid = new Grid(this, layout, 4);

        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(grid);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    void gameWin() {
        if (!isGameOfficiallyDone) {
            Log.wtf("Notice", "You Won!!");
            isGameOfficiallyDone = true;
        }
    }

    void gameLose() {
        if (!isGameOfficiallyDone) {
            Log.wtf("Notice", "You Lost!!");
            isGameOfficiallyDone = true;
        }
    }
}
