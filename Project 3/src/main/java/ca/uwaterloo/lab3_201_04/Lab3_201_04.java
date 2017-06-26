package ca.uwaterloo.lab3_201_04;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Entry point of app.
 * <p>
 * Initializes components which are displayed on the app screen. Initializes accelerometer handler.
 * Used to save to file.
 */
public class Lab3_201_04 extends AppCompatActivity {

    private static final String APP_NAME = "Lab3_201_04";

    /**
     * Called when app starts to initialize components.
     *
     * @param savedInstanceState passed into the super method which is overriden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up basics
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_201_04);

        // Create layout
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        // Create handles
        createBufferSpace(layout);
        final TextView direction = createTextHandleProperties(layout, "direction", 50);
        direction.setGravity(Gravity.CENTER_HORIZONTAL);

        // Start acceleration monitoring
        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(direction);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Used to create space between components.
     *
     * @param layout layout manager which determines order of placing components
     */
    private void createBufferSpace(LinearLayout layout) {
        createTextHandleProperties(layout, "", 20);
    }

    /**
     * Creates a text handle and sets its default properties.
     *
     * @param layout layout manager which determines order of placing components
     * @param label  text which will be on the text component
     * @return text handle which was created
     */
    private TextView createTextHandleProperties(LinearLayout layout, String label, int textSize) {
        final TextView handle = new TextView(getApplicationContext());
        handle.setText(label);
        layout.addView(handle);
        handle.setTextSize(textSize);
        handle.setTextColor(Color.BLACK);
        return handle;
    }

    /**
     * Called when a serious error occurs and the program needs to be terminated.
     *
     * @param error    error message
     * @param location location in code in which error comes from
     */
    static void errorPanic(String error, String location) {
        Log.wtf(APP_NAME, "Panic Error in " + location + ": " + error + "!!");
        System.exit(-1);
    }

    /**
     * Called when an error occurs, but is not serious enough to terminate the program.
     *
     * @param error    error message
     * @param location location in code in which error comes from
     */
    static void errorNonFatal(String error, String location) {
        Log.e(APP_NAME, "Non-Fatal Error in " + location + ": " + error + "!!");
    }
}
