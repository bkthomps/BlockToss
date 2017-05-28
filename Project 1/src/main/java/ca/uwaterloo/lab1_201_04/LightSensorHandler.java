package ca.uwaterloo.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

/**
 * Receives and manages light sensor data readings.
 */
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

    /**
     * Sets current data to data from sensor readings.
     *
     * @param eventInfo first index is used for light sensor data
     */
    public void onSensorChanged(SensorEvent eventInfo) {
        current = eventInfo.values[0];
        setHistoricalHigh(current);
        setDisplayText();
    }

    /**
     * Sets the historical high data to the current data.
     */
    void resetHistoricalHigh() {
        highest = current;
        setDisplayText();
    }

    /**
     * Used to refresh the text on text components so that they represent actual data.
     */
    private void setDisplayText() {
        final String currentReading = "\n   Light Reading:\n        "
                + String.format(Locale.US, "(%.2f)", current);
        final String highestReading = "\n\n   Highest Light Reading:\n        "
                + String.format(Locale.US, "(%.2f)", highest);
        final String reading = currentReading + highestReading;
        text.setText(reading);
    }

    /**
     * If the current light reading is higher than the historical high reading, the historical high reading is set to
     * the current reading.
     *
     * @param light current light data reading
     */
    private void setHistoricalHigh(float light) {
        if (light > highest) {
            highest = light;
        }
    }
}
