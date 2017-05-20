package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class RotationVectorSensorHandler implements SensorEventListener {

    private final TextView text;

    RotationVectorSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        final float xRotationVector = eventInfo.values[0];
        final float yRotationVector = eventInfo.values[1];
        final float zRotationVector = eventInfo.values[2];
        final String rotationVectorSensorReading = Lab1_201_04.SPACING + "Rotation vector sensor reading: "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xRotationVector, yRotationVector, zRotationVector);
        text.setText(rotationVectorSensorReading);
    }
}
