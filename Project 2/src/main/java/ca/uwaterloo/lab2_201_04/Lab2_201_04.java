package ca.uwaterloo.lab2_201_04;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Entry point of app.
 * <p>
 * Initializes components which are displayed on the app screen. Initializes accelerometer handler.
 * Used to save to file.
 */
public class Lab2_201_04 extends AppCompatActivity {

    private static final String APP_NAME = "Lab2_201_04";

    /**
     * Called when app starts to initialize components.
     *
     * @param savedInstanceState passed into the super method which is overriden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up basics
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2_201_04);

        // Create layout
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        // Create graph
        createBufferSpace(layout);
        final int samplesToKeep = 100;
        final List<String> listOfLabels = Arrays.asList("x", "y", "z");
        final LineGraphView graph = new LineGraphView(getApplicationContext(), samplesToKeep, listOfLabels);
        layout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        // Create handles
        createBufferSpace(layout);
        final Button saveAccelerometerData = createButtonHandleProperties(layout, "Save Accelerometer Data");
        final TextView direction = createTextHandleProperties(layout, "direction", 50);
        direction.setGravity(Gravity.CENTER_HORIZONTAL);

        // Start acceleration monitoring
        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(direction, graph);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        saveAccelerometerData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveReadings(accelerometerHandler);
            }
        });
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
     * Creates a button handle and sets its default properties.
     *
     * @param layout layout manager which determines order of placing components
     * @param label  text which will be on the button
     * @return button handle which was created
     */
    private Button createButtonHandleProperties(LinearLayout layout, String label) {
        final int TEXT_SIZE = 20;
        final Button handle = new Button(getApplicationContext());
        handle.setText(label);
        layout.addView(handle);
        handle.setTextSize(TEXT_SIZE);
        handle.setTextColor(Color.BLACK);
        final int lightBlue = Color.rgb(66, 152, 244);
        handle.setBackgroundColor(lightBlue);
        return handle;
    }

    /**
     * Saves accelerometer readings.
     *
     * @param accelerometerHandler handler of accelerometer sensor
     */
    void saveReadings(AccelerometerSensorHandler accelerometerHandler) {
        final String fileName = "data_" + getDate() + ".csv";
        createFile(fileName, accelerometerHandler);
    }

    /**
     * Gets current date as a string.
     *
     * @return current date as a string
     */
    private String getDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int monthIndex = calendar.get(Calendar.MONTH);
        final String month = monthIndexToAbbreviation(monthIndex);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);
        final int milliSecond = calendar.get(Calendar.MILLISECOND);
        return year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + second + "_" + milliSecond;
    }

    /**
     * Converts month index to abbreviation.
     *
     * @param index index of month from 0 to 11
     * @return abbreviation of month from "jan" to "dec"
     */
    private String monthIndexToAbbreviation(int index) {
        final String[] months =
                new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        return months[index];
    }

    /**
     * Creates a file in order to store accelerometer data.
     *
     * @param fileName             name of file which is to be created
     * @param accelerometerHandler handler of accelerometer sensor
     */
    private void createFile(String fileName, AccelerometerSensorHandler accelerometerHandler) {
        PrintWriter writer = null;
        try {
            final File fileHandle = new File(getExternalFilesDir(APP_NAME), fileName);
            writer = new PrintWriter(fileHandle);
            for (int i = AccelerometerSensorHandler.OLDEST_INDEX; i <= AccelerometerSensorHandler.NEWEST_INDEX; i++) {
                final Float[] reading = accelerometerHandler.getLatestReadingsAtIndex(i);
                final String line = reading[0] + "," + reading[1] + "," + reading[2];
                writer.println(line);
            }
        } catch (IOException e) {
            errorNonFatal("file could not be created", "Lab2_201_04.createFile");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
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
