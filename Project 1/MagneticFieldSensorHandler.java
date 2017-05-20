package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class MagneticFieldSensorHandler implements SensorEventListener {

    private final TextView text;

    MagneticFieldSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        final float xMagneticField = eventInfo.values[0];
        final float yMagneticField = eventInfo.values[1];
        final float zMagneticField = eventInfo.values[2];
        final String magneticFieldSensorReading = Lab1_201_04.SPACING + "Magnetic field sensor reading: "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xMagneticField, yMagneticField, zMagneticField);
        text.setText(magneticFieldSensorReading);
    }
}
