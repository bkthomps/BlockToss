package lab1_201_04.uwaterloo.ca.lab1_201_04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.Locale;

class RotationVectorSensorHandler implements SensorEventListener {

    private final TextView text;

    private float xHighest;
    private float yHighest;
    private float zHighest;

    RotationVectorSensorHandler(TextView text) {
        this.text = text;
    }

    public void onAccuracyChanged(Sensor s, int i) {
        // Nothing
    }

    public void onSensorChanged(SensorEvent eventInfo) {
        final float xCurrent = eventInfo.values[0];
        final float yCurrent = eventInfo.values[1];
        final float zCurrent = eventInfo.values[2];
        setHistoricalHigh(xCurrent, yCurrent, zCurrent);
        final String currentReading = "\n   Rotation Vector Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xCurrent, yCurrent, zCurrent);
        final String highestReading = "\n\n   Highest Rotation Vector Reading:\n        "
                + String.format(Locale.US, "(%.2f, %.2f, %.2f)", xHighest, yHighest, zHighest) + "\n";
        final String reading = currentReading + highestReading;
        text.setText(reading);
    }

    void resetHistoricalHigh() {
        xHighest = 0;
        yHighest = 0;
        zHighest = 0;
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
}
