package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class LightSensorHandler implements SensorEventListener {

    private final TextView text;

    private float current;

    private float highest;

    LightSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        current = eventInfo.values[0];
        setHistoricalHigh(current);
        setDisplayText();
    }

    void resetHistoricalHigh() {
        highest = current;
        setDisplayText();
    }

    private void setDisplayText() {
        final String currentReading = "\n   Light Reading:\n        "
                + String.format(Locale.US, "(%.2f)", current);
        final String highestReading = "\n\n   Highest Light Reading:\n        "
                + String.format(Locale.US, "(%.2f)", highest);
        final String reading = currentReading + highestReading;
        text.setText(reading);
    }

    private void setHistoricalHigh(float light) {
        if (light > highest) {
            highest = light;
        }
    }
}
