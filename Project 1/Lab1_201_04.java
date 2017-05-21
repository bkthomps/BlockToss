package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

public class Lab1_201_04 extends AppCompatActivity {
    LineGraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up basics
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_201_04);

        // Create layout
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        // Create handles
        graph = new LineGraphView(getApplicationContext(),100, Arrays.asList("x","y","z"));
        layout.addView(graph);
        graph.setVisibility(View.VISIBLE);
        final Button resetHistoricalHigh = createButtonHandleProperties(layout, "Reset Historical High Readings");
        final TextView bufferSpace = createTextHandleProperties(layout, "");
        final Button saveAccelerometerData = createButtonHandleProperties(layout, "Save Accelerometer Data");
        final TextView lightHandle = createTextHandleProperties(layout, "lightLabel");
        final TextView accelerometerHandle = createTextHandleProperties(layout, "accelerometerLabel");
        final TextView magneticFieldHandle = createTextHandleProperties(layout, "magneticFieldLabel");
        final TextView rotationVectorHandle = createTextHandleProperties(layout, "rotationVectorLabel");

        // Create handlers
        final LightSensorHandler lightHandler = new LightSensorHandler(lightHandle);
        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(accelerometerHandle,graph);
        final MagneticFieldSensorHandler magneticFieldHandler = new MagneticFieldSensorHandler(magneticFieldHandle);
        final RotationVectorSensorHandler rotationVectorHandler = new RotationVectorSensorHandler(rotationVectorHandle);

        // Get instance of sensor manager
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get default sensors
        final Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Attach sensors to sensor manager
        sensorManager.registerListener(lightHandler, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magneticFieldHandler, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(rotationVectorHandler, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Initialize button
        resetHistoricalHigh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lightHandler.resetHistoricalHigh();
                accelerometerHandler.resetHistoricalHigh();
                magneticFieldHandler.resetHistoricalHigh();
                rotationVectorHandler.resetHistoricalHigh();
            }
        });

        // Initialize button
        saveAccelerometerData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveReadings(accelerometerHandler);
            }
        });
    }

    private TextView createTextHandleProperties(LinearLayout layout, String label) {
        final int TEXT_SIZE = 20;
        final TextView handle = new TextView(getApplicationContext());
        handle.setText(label);
        layout.addView(handle);
        handle.setTextSize(TEXT_SIZE);
        handle.setTextColor(Color.BLACK);
        return handle;
    }

    private Button createButtonHandleProperties(LinearLayout layout, String label) {
        final int TEXT_SIZE = 20;
        final Button handle = new Button(getApplicationContext());
        handle.setText(label);
        layout.addView(handle);
        handle.setTextSize(TEXT_SIZE);
        handle.setTextColor(Color.BLACK);
        handle.setBackgroundColor(Color.rgb(66, 152, 244));
        return handle;
    }

    /**
     * Will save to .csv file in CSV format (comma-separated list). Ie: "x-val,y-val,z-val". Each of the SAVE_HISTORY
     * amount of readings is saved to the file with oldest readings at the top and newest at the bottom. The file name
     * is in format: "accelerometerReadings_nanoTime_" + currentNanoTime + ".csv".
     */
    void saveReadings(AccelerometerSensorHandler accelerometerHandler) {
        final long currentNanoTime = System.nanoTime();
        final String fileName = "accelerometerReadings_nanoTime_" + currentNanoTime + ".csv";
        createFile(fileName, accelerometerHandler);
    }

    private void createFile(String fileName, AccelerometerSensorHandler accelerometerHandler) {
        PrintWriter writer = null;
        try {
            final File fileHandle = new File(getExternalFilesDir("Lab1_201_04"), fileName);
            writer = new PrintWriter(fileHandle);
            for (int i = AccelerometerSensorHandler.OLDEST_INDEX; i <= AccelerometerSensorHandler.NEWEST_INDEX; i++) {
                final float[] latestReadings = accelerometerHandler.getLatestReadingsAtIndex(i);
                final String line = latestReadings[0] + "," + latestReadings[1] + "," + latestReadings[2];
                writer.println(line);
            }
        } catch (IOException e) {
            throw new Error("File could not be created!!");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
