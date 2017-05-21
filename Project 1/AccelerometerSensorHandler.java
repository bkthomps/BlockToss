package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.icu.lang.UCharacter;
import android.widget.TextView;

import java.util.Locale;

import ca.uwaterloo.sensortoy.LineGraphView;

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

    AccelerometerSensorHandler(TextView text,LineGraphView graph) {
        this.text = text;
        this.graph = graph;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        xCurrent = eventInfo.values[0];
        yCurrent = eventInfo.values[1];
        zCurrent = eventInfo.values[2];
        setLatestReading(xCurrent, yCurrent, zCurrent);
        setHistoricalHigh(xCurrent, yCurrent, zCurrent);
        setDisplayText();
        setGraphView(eventInfo.values);

    }

    void resetHistoricalHigh() {
        xHighest = xCurrent;
        yHighest = yCurrent;
        zHighest = zCurrent;
        setDisplayText();
    }

    private void setDisplayText() {
        final String currentReading = "\n   Accelerometer Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xCurrent, yCurrent, zCurrent);
        final String historicalHighReading = "\n\n   Highest Accelerometer Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xHighest, yHighest, zHighest);
        final String reading = currentReading + historicalHighReading;
        text.setText(reading);
    }

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

    private void setGraphView(float[] values){
        graph.addPoint(values);
    }

    float[] getLatestReadingsAtIndex(int index) {
        return new float[]{latestReadings[0][index], latestReadings[1][index], latestReadings[2][index]};
    }
}
