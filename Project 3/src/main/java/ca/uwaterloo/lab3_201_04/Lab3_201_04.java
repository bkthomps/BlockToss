package ca.uwaterloo.lab3_201_04;

import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        final int gameBoardDimension = gameBoardDimension();

        // Create layout
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        layout.getLayoutParams().width = gameBoardDimension;
        layout.getLayoutParams().height = gameBoardDimension;

        layout.setBackgroundResource(R.drawable.gameboard);

        final int blocksPerScreen = 4;
        final int pixelsOfBlock = 130;
        final int sizeOfBlock = gameBoardDimension / blocksPerScreen;

        final float ratio = (float) sizeOfBlock / (blocksPerScreen * pixelsOfBlock);

        final int BASE_PIXEL = -80;

        final ImageView block = createImageViewProperties(layout);
        block.setImageResource(R.drawable.gameblock);
        block.setScaleX(ratio);
        block.setScaleY(ratio);
        block.setX(BASE_PIXEL);
        block.setY(BASE_PIXEL);

        // Create handles
        final TextView direction = createTextHandleProperties(layout, "direction", 50);

        // Start acceleration monitoring
        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(direction);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private int gameBoardDimension() {
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int BANNER_OFFSET = 300;  // TODO: scale with phone banner size
        final int width = size.x;
        final int height = size.y - BANNER_OFFSET;
        return (width < height) ? width : height;
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

    private ImageView createImageViewProperties(RelativeLayout layout) {
        final ImageView handle = new ImageView(getApplicationContext());
        layout.addView(handle);
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
