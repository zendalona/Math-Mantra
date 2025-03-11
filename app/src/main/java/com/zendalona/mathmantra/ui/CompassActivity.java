import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private float[] gravity, geomagnetic;
    private TextView directionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        directionText = findViewById(R.id.directionText);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values;

        if (gravity != null && geomagnetic != null) {
            float[] rotationMatrix = new float[9];
            float[] orientation = new float[3];

            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(rotationMatrix, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]); // Rotation around Z-axis

                String direction = "Unknown";
                if (azimuth >= -22.5 && azimuth < 22.5) {
                    direction = "North";
                } else if (azimuth >= 22.5 && azimuth < 67.5) {
                    direction = "Northeast";
                } else if (azimuth >= 67.5 && azimuth < 112.5) {
                    direction = "East";
                } else if (azimuth >= 112.5 && azimuth < 157.5) {
                    direction = "Southeast";
                } else if (azimuth >= 157.5 || azimuth < -157.5) {
                    direction = "South";
                } else if (azimuth >= -157.5 && azimuth < -112.5) {
                    direction = "Southwest";
                } else if (azimuth >= -112.5 && azimuth < -67.5) {
                    direction = "West";
                } else if (azimuth >= -67.5 && azimuth < -22.5) {
                    direction = "Northwest";
                }

                directionText.setText("Direction: " + direction);
                giveFeedback(); // Vibration feedback
            }
        }
    }

    // Vibration Feedback Method
    private void giveFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }
}
