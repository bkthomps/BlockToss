package ca.uwaterloo.lab4_201_04;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Initializes components which are displayed on the app screen.
 */
public class Lab4_201_04 extends AppCompatActivity {

    final static int WIN_VALUE = 256;
    final static int[] BLOCKS_THAT_CAN_SPAWN = {2, 4};

    private RelativeLayout layout;
    static boolean isGameOfficiallyDone;

    /**
     * Called when app starts so that components can be initialized.
     *
     * @param savedInstanceState passed into the super method which is overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_201_04);

        layout = (RelativeLayout) findViewById(R.id.layout);
        final Grid grid = new Grid(this, layout, 4);

        final AccelerometerSensorHandler accelerometerHandler = new AccelerometerSensorHandler(grid);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Triggers a game win.
     */
    void gameWin() {
        endGame("You Won!");
    }

    /**
     * Triggers a game lose.
     */
    void gameLose() {
        endGame("You Lost!");
    }

    /**
     * Ends the game and notifies the user.
     *
     * @param message notification which appears to the user
     */
    private void endGame(String message) {
        if (!isGameOfficiallyDone) {
            Log.d("Notice", message);
            final TextView endGameMessage = new TextView(getApplicationContext());
            endGameMessage.setText(message);
            layout.addView(endGameMessage);
            final int textSize = 50;
            endGameMessage.setTextSize(textSize);
            final int orange = Color.rgb(255, 128, 0);
            endGameMessage.setTextColor(orange);
            endGameMessage.setX(70);
            endGameMessage.setY(20);
            endGameMessage.bringToFront();
            isGameOfficiallyDone = true;
        }
    }
}
