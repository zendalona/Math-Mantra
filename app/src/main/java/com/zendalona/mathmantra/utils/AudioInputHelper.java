package com.zendalona.mathmantra.utils;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
public class AudioInputHelper {
    private static final int SAMPLE_RATE = 44100;  // Standard audio sample rate
    private static final int THRESHOLD = 20000;    // Noise threshold for clapping detection
    private AudioRecord audioRecord;
    private boolean isListening = false;
    private OnClapDetectedListener listener;

    public interface OnClapDetectedListener {
        void onClapDetected();
    }

    public AudioInputHelper(OnClapDetectedListener listener) {
        this.listener = listener;
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    public void startListening() {
        isListening = true;
        new Thread(() -> {
            short[] buffer = new short[1024];
            audioRecord.startRecording();
            while (isListening) {
                int read = audioRecord.read(buffer, 0, buffer.length);
                for (int i = 0; i < read; i++) {
                    if (Math.abs(buffer[i]) > THRESHOLD) {
                        if (listener != null) {
                            listener.onClapDetected();  // Trigger clap detection event
                        }
                        break;
                    }
                }
            }
            audioRecord.stop();
        }).start();
    }

    public void stopListening() {
        isListening = false;
        audioRecord.release();
    }
}
