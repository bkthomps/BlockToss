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

    private final TextView direction;
    private final List<Float[]> latestReadings = new ArrayList<>();

    private float xFiltered;
    private float yFiltered;
    private float zFiltered;

    private boolean isXDominant;
    private boolean isYDominant;
    private boolean isXLastDominant;
    private boolean isYLastDominant;

    AccelerometerSensorHandler(TextView direction, LineGraphView graph) {
        for (int i = 0; i < SAVE_HISTORY; i++) {
            latestReadings.add(new Float[]{0F, 0F, 0F});
        }

        final String directionText = "\nNONE";
        direction.setText(directionText);
        this.direction = direction;
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
        graph.addPoint(xFiltered, yFiltered, zFiltered);
    }

    private float removeUnderThreshHold(float val, float threshHold) {
        return (Math.abs(val) < threshHold) ? 0 : val;
    }

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
                final String UP = "\nUP";
                direction.setText(UP);
            } else if (yData.get(firstIndex) < 0 && yData.get(lastIndex) > 0) {
                final String DOWN = "\nDOWN";
                direction.setText(DOWN);
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
                final String RIGHT = "\nRIGHT";
                direction.setText(RIGHT);
            } else if (xData.get(firstIndex) < 0 && xData.get(lastIndex) > 0) {
                final String LEFT = "\nLEFT";
                direction.setText(LEFT);
            }
        }
    }

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
