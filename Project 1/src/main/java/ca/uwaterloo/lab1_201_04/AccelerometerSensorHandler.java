package ca.uwaterloo.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Receives and manages accelerometer sensor data readings.
 */
class AccelerometerSensorHandler implements SensorEventListener {

    private static final int DIMENSIONS = 3;
    private static final int SAVE_HISTORY = 100;
    static final int NEWEST_INDEX = SAVE_HISTORY - 1;
    static final int OLDEST_INDEX = 0;

    private final TextView text;
    private final LineGraphView graph;

    private float[][] latestReadings = new float[DIMENSIONS][SAVE_HISTORY];

    private float xCurrent;
    private float yCurrent;
    private float zCurrent;

    private float xHighest;
    private float yHighest;
    private float zHighest;

    AccelerometerSensorHandler(TextView text, LineGraphView graph) {
        this.text = text;
        this.graph = graph;
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
        setLatestReading(xCurrent, yCurrent, zCurrent);
        setHistoricalHigh(xCurrent, yCurrent, zCurrent);
        setDisplayText();
        graph.addPoint(eventInfo.values);
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
        final String currentReading = "\n   Accelerometer Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xCurrent, yCurrent, zCurrent);
        final String historicalHighReading = "\n\n   Highest Accelerometer Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xHighest, yHighest, zHighest);
        final String reading = currentReading + historicalHighReading;
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

    /**
     * All data is pushed over by one index, deleting the data from the oldest index, and moving the new data to the
     * newest index.
     *
     * @param x the current x-coordinate
     * @param y the current y-coordinate
     * @param z the current z-coordinate
     */
    private void setLatestReading(float x, float y, float z) {
        for (int i = OLDEST_INDEX; i < NEWEST_INDEX; i++) {
            latestReadings[0][i] = latestReadings[0][i + 1];
            latestReadings[1][i] = latestReadings[1][i + 1];
            latestReadings[2][i] = latestReadings[2][i + 1];
        }
        latestReadings[0][NEWEST_INDEX] = x;
        latestReadings[1][NEWEST_INDEX] = y;
        latestReadings[2][NEWEST_INDEX] = z;
    }

    /**
     * Used to get data based on how long ago it was collected.
     *
     * @param index represents how long ago the data was collected
     * @return the data of all three coordinates for the respective index
     */
    float[] getLatestReadingsAtIndex(int index) {
        return new float[]{latestReadings[0][index], latestReadings[1][index], latestReadings[2][index]};
    }
}
