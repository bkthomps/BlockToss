package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class LightSensorHandler implements SensorEventListener {

    private final TextView text;

    LightSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        final float lightReading = eventInfo.values[0];
        final String lightSensorReading = Lab1_201_04.SPACING + "Light sensor reading: "
                + String.format(Locale.US, "(%.2f)", lightReading);
        text.setText(lightSensorReading);
    }
}
