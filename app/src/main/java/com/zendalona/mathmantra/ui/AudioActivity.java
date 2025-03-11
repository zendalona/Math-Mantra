import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AudioActivity extends AppCompatActivity {
    private MediaPlayer northSound, eastSound, westSound, southSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        northSound = MediaPlayer.create(this, R.raw.north_sound);
        eastSound = MediaPlayer.create(this, R.raw.east_sound);
        southSound = MediaPlayer.create(this, R.raw.south_sound);
        westSound = MediaPlayer.create(this, R.raw.west_sound);
    }

    private void playDirectionSound(String direction) {
        stopAllSounds(); // Stop any currently playing sound

        switch (direction) {
            case "North":
                northSound.start();
                break;
            case "East":
                eastSound.start();
                break;
            case "West":
                westSound.start();
                break;
            case "South":
                southSound.start();
                break;
        }

        giveFeedback(); // Vibration feedback
    }

    // Vibration Feedback Method
    private void giveFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }


    private void stopAllSounds() {
        if (northSound.isPlaying()) northSound.pause();
        if (eastSound.isPlaying()) eastSound.pause();
        if (westSound.isPlaying()) westSound.pause();
        if (southSound.isPlaying()) southSound.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        northSound.release();
        eastSound.release();
        westSound.release();
        southSound.release();
    }
}
