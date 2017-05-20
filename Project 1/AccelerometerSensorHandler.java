package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class AccelerometerSensorHandler implements SensorEventListener {

    private final TextView text;

    AccelerometerSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        final float xAcceleration = eventInfo.values[0];
        final float yAcceleration = eventInfo.values[1];
        final float zAcceleration = eventInfo.values[2];
        final String accelerometerSensorReading = Lab1_201_04.SPACING + "Accelerometer sensor reading: "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xAcceleration, yAcceleration, zAcceleration);
        text.setText(accelerometerSensorReading);
    }
}
