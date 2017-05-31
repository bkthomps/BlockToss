package ca.uwaterloo.lab2_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Receives and manages accelerometer sensor data readings.
 */
class AccelerometerSensorHandler implements SensorEventListener {

    private static final int DIMENSIONS = 3;
    private static final int SAVE_HISTORY = 100;
    static final int NEWEST_INDEX = SAVE_HISTORY - 1;
    static final int OLDEST_INDEX = 0;

    private final LineGraphView graph;

    private float[][] latestReadings = new float[DIMENSIONS][SAVE_HISTORY];

    AccelerometerSensorHandler(TextView direction, LineGraphView graph) {
        final String directionText = "\nUndefined";
        direction.setText(directionText);
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
        final float xCurrent = eventInfo.values[0];
        final float yCurrent = eventInfo.values[1];
        final float zCurrent = eventInfo.values[2];
        setLatestReading(xCurrent, yCurrent, zCurrent);
        graph.addPoint(eventInfo.values);
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
