package ca.uwaterloo.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

/**
 * Receives and manages rotation vector sensor data readings.
 */
class RotationVectorSensorHandler implements SensorEventListener {

    private final TextView text;

    private float xCurrent;
    private float yCurrent;
    private float zCurrent;

    private float xHighest;
    private float yHighest;
    private float zHighest;

    RotationVectorSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    /**
     * Sets current data to data from sensor readings.
     *
     * @param eventInfo array with acceleration information, x-coordinate is index 0, y-coordinate is index 1, and
     *                  z-coordinate is index 2
     */
    public void onSensorChanged(SensorEvent eventInfo) {
        xCurrent = eventInfo.values[0];
        yCurrent = eventInfo.values[1];
        zCurrent = eventInfo.values[2];
        setHistoricalHigh(xCurrent, yCurrent, zCurrent);
        setDisplayText();
    }

    /**
     * Sets the historical high data to the current data.
     */
    void resetHistoricalHigh() {
        xHighest = xCurrent;
        yHighest = yCurrent;
        zHighest = zCurrent;
        setDisplayText();
    }

    /**
     * Used to refresh the text on text components so that they represent actual data.
     */
    private void setDisplayText() {
        final String currentReading = "\n   Rotation Vector Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xCurrent, yCurrent, zCurrent);
        final String highestReading = "\n\n   Highest Rotation Vector Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xHighest, yHighest, zHighest) + "\n";
        final String reading = currentReading + highestReading;
        text.setText(reading);
    }

    /**
     * If the magnitude of a historic high coordinate is less than the magnitude of a current coordinate, that
     * historical high coordinate gets set the the current coordinate.
     *
     * @param x the current x-coordinate
     * @param y the current y-coordinate
     * @param z the current z-coordinate
     */
    private void setHistoricalHigh(float x, float y, float z) {
        if (Math.abs(x) > Math.abs(xHighest)) {
            xHighest = x;
        }
        if (Math.abs(y) > Math.abs(yHighest)) {
            yHighest = y;
        }
        if (Math.abs(z) > Math.abs(zHighest)) {
            zHighest = z;
        }
    }
}
