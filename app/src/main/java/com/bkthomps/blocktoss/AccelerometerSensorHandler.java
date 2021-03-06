package com.bkthomps.blocktoss;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Receives and manages accelerometer sensor data readings.
 */
class AccelerometerSensorHandler implements SensorEventListener {

    private final Grid grid;

    private static final int SAVE_HISTORY = 100;
    private static final int NEWEST_INDEX = SAVE_HISTORY - 1;
    private static final int OLDEST_INDEX = 0;

    private final List<Float[]> latestReadings = new ArrayList<>();

    private float xFiltered;
    private float yFiltered;
    private float zFiltered;

    private boolean isXDominant;
    private boolean isYDominant;
    private boolean isXLastDominant;
    private boolean isYLastDominant;

    AccelerometerSensorHandler(Grid grid) {
        this.grid = grid;
        for (int i = 0; i < SAVE_HISTORY; i++) {
            latestReadings.add(new Float[]{0F, 0F, 0F});
        }
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
        final float DATA_THRESH_HOLD = 0.5F;
        final float xCurrent = removeUnderThreshHold(eventInfo.values[0], DATA_THRESH_HOLD);
        final float yCurrent = removeUnderThreshHold(eventInfo.values[1], DATA_THRESH_HOLD);
        final float zCurrent = removeUnderThreshHold(eventInfo.values[2], DATA_THRESH_HOLD);

        final int FILTER_LEVEL = 30;
        final float KEEP_OLD_FILTER = 0.6F;
        xFiltered = xFiltered * KEEP_OLD_FILTER + (xCurrent - xFiltered) / FILTER_LEVEL;
        yFiltered = yFiltered * KEEP_OLD_FILTER + (yCurrent - yFiltered) / FILTER_LEVEL;
        zFiltered = zFiltered * KEEP_OLD_FILTER + (zCurrent - zFiltered) / FILTER_LEVEL;

        final float FILTER_THRESH_HOLD = 0.15F;
        xFiltered = removeUnderThreshHold(xFiltered, FILTER_THRESH_HOLD);
        yFiltered = removeUnderThreshHold(yFiltered, FILTER_THRESH_HOLD);
        zFiltered = removeUnderThreshHold(zFiltered, FILTER_THRESH_HOLD);

        determineDominantCoordinate();

        setLatestReadings(xFiltered, yFiltered, zFiltered);
    }

    /**
     * If value is under specified thresh hold, it shall become 0.
     *
     * @param val        value
     * @param threshHold thresh hold to compare value to
     * @return value if more than thresh hold, else 0
     */
    private float removeUnderThreshHold(float val, float threshHold) {
        return (Math.abs(val) < threshHold) ? 0 : val;
    }

    /**
     * Determines whether the dominant coordinate is x or y, that is horizontal or vertical.
     */
    private void determineDominantCoordinate() {
        zFiltered = 0;
        if (xFiltered == 0 && yFiltered == 0) {
            analyzeGraph();
            isXDominant = false;
            isYDominant = false;
            isXLastDominant = false;
            isYLastDominant = false;
        } else if (isXDominant || (!isYDominant && Math.abs(xFiltered) > Math.abs(yFiltered))) {
            isXDominant = true;
            isXLastDominant = true;
            isYLastDominant = false;
            yFiltered = 0;
        } else {
            isYDominant = true;
            isYLastDominant = true;
            isXLastDominant = false;
            xFiltered = 0;
        }
    }

    /**
     * Determines if the motion is up, down, right, or left.
     */
    private void analyzeGraph() {
        if (isYLastDominant) {
            final Float[] yGraph = new Float[latestReadings.size()];
            int i = 0;
            for (Float[] data : latestReadings) {
                yGraph[i] = data[1];
                i++;
            }
            final List<Float> yData = getGraph(yGraph);
            final int size = yData.size();
            if (size < 5) {
                return;
            }
            final int firstIndex = (int) (0.1 * size);
            final int lastIndex = (int) (0.9 * size);
            if (yData.get(firstIndex) > 0 && yData.get(lastIndex) < 0) {
                grid.moveUp();
            } else if (yData.get(firstIndex) < 0 && yData.get(lastIndex) > 0) {
                grid.moveDown();
            }
        } else if (isXLastDominant) {
            final Float[] xGraph = new Float[latestReadings.size()];
            int i = 0;
            for (Float[] data : latestReadings) {
                xGraph[i] = data[0];
                i++;
            }
            final List<Float> xData = getGraph(xGraph);
            final int size = xData.size();
            if (size < 5) {
                return;
            }
            final int firstIndex = (int) (0.1 * size);
            final int lastIndex = (int) (0.9 * size);
            if (xData.get(firstIndex) > 0 && xData.get(lastIndex) < 0) {
                grid.moveRight();
            } else if (xData.get(firstIndex) < 0 && xData.get(lastIndex) > 0) {
                grid.moveLeft();
            }
        }
    }

    /**
     * Takes in a graph and determines the pattern associated with hand motion.
     *
     * @param data entire graph fed in
     * @return graph associated with hand motion
     */
    private List<Float> getGraph(Float[] data) {
        final int lastIndex = data.length - 1;
        int firstIndex = -1;
        for (int i = lastIndex; firstIndex == -1; i--) {
            if (i >= 2) {
                if (data[i] == 0 && data[i - 1] == 0 && data[i - 2] == 0) {
                    firstIndex = i;
                }
            } else {
                firstIndex = 0;
            }
        }
        final List<Float> graph = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            graph.add(data[i]);
        }
        return graph;
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
            Log.wtf("Error!!", "Error: latest reading data is incorrect!!");
        }
    }
}
