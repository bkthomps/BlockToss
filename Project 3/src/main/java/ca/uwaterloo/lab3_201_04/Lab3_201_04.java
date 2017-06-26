package ca.uwaterloo.lab3_201_04;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Entry point of app.
 * <p>
 * Initializes components which are displayed on the app screen. Initializes accelerometer handler.
 * Used to save to file.
 */
public class Lab3_201_04 extends AppCompatActivity {

    /**
     * Called when app starts to initialize components.
     *
     * @param savedInstanceState passed into the super method which is overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_201_04);

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        final Grid grid = new Grid(this, layout, 4);
        final TextView direction = createTextHandleProperties(layout, "direction", 50);

        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(direction, grid);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Creates a text handle and sets its default properties.
     *
     * @param layout layout manager which determines order of placing components
     * @param label  text which will be on the text component
     * @return text handle which was created
     */
    private TextView createTextHandleProperties(RelativeLayout layout, String label, int textSize) {
        final TextView handle = new TextView(getApplicationContext());
        handle.setText(label);
        layout.addView(handle);
        handle.setTextSize(textSize);
        handle.setTextColor(Color.BLACK);
        return handle;
    }
}
