package ca.uwaterloo.lab2_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Receives and manages accelerometer sensor data readings.
 */
class AccelerometerSensorHandler implements SensorEventListener {

    private static final int SAVE_HISTORY = 100;
    static final int NEWEST_INDEX = SAVE_HISTORY - 1;
    static final int OLDEST_INDEX = 0;

    private final LineGraphView graph;

    private final List<Float[]> latestReadings = new ArrayList<>();

    private float xFiltered;
    private float yFiltered;
    private float zFiltered;

    AccelerometerSensorHandler(TextView direction, LineGraphView graph) {
        for (int i = 0; i < SAVE_HISTORY; i++) {
            latestReadings.add(new Float[]{0F, 0F, 0F});
        }

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
        final int FILTER_LEVEL = 100;
        xFiltered += (eventInfo.values[0] - xFiltered) / FILTER_LEVEL;
        yFiltered += (eventInfo.values[1] - yFiltered) / FILTER_LEVEL;
        zFiltered += (eventInfo.values[2] - zFiltered) / FILTER_LEVEL;

        setLatestReadings(xFiltered, yFiltered, zFiltered);
        graph.addPoint(xFiltered, yFiltered, zFiltered);
    }

    /**
     * All data is pushed over by one index, deleting the data from the oldest index, and moving the new data to the
     * newest index.
     *
     * @param x the current x-coordinate
     * @param y the current y-coordinate
     * @param z the current z-coordinate
     */
    private void setLatestReadings(float x, float y, float z) {
        latestReadings.remove(OLDEST_INDEX);
        final Float[] dataPoint = {x, y, z};
        latestReadings.add(NEWEST_INDEX, dataPoint);
        if (latestReadings.size() != SAVE_HISTORY) {
            Lab2_201_04.errorPanic("latest readings size is incorrect", "AccelerometerSensorHandler.setLatestReadings");
        }
    }

    /**
     * Used to get data based on how long ago it was collected.
     *
     * @param index represents how long ago the data was collected
     * @return the data of all three coordinates for the respective index
     */
    Float[] getLatestReadingsAtIndex(int index) {
        return latestReadings.get(index);
    }
}
