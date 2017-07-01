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
        Log.d("You Won!!", "You Won!!");
    }

    void gameLose() {
        Log.d("You Lost!!", "You Lost!!");
    }
}
