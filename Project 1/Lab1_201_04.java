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

public class Lab1_201_04 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up basics
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_201_04);

        // Create layout
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        // Create handles
        final Button resetHistoricalHigh = createButtonHandleProperties(layout, "Reset Historical High Readings");
        final TextView lightHandle = createTextHandleProperties(layout, "lightLabel");
        final TextView accelerometerHandle = createTextHandleProperties(layout, "accelerometerLabel");
        final TextView magneticFieldHandle = createTextHandleProperties(layout, "magneticFieldLabel");
        final TextView rotationVectorHandle = createTextHandleProperties(layout, "rotationVectorLabel");

        // Create handlers
        final LightSensorHandler lightHandler = new LightSensorHandler(lightHandle);
        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(accelerometerHandle);
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

        // Button Handler
        resetHistoricalHigh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lightHandler.resetHistoricalHigh();
                accelerometerHandler.resetHistoricalHigh();
                magneticFieldHandler.resetHistoricalHigh();
                rotationVectorHandler.resetHistoricalHigh();
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
}
