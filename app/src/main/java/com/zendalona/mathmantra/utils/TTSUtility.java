package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TTSUtility {
    private TextToSpeech tts;
    private boolean isInitialized = false;

    public TTSUtility(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
//                int result = tts.setLanguage(Locale.US);
                int result = tts.setLanguage(Locale.forLanguageTag("en-IN"));
                isInitialized = result != TextToSpeech.LANG_MISSING_DATA
                        && result != TextToSpeech.LANG_NOT_SUPPORTED;
            }
        });
    }

    public void speak(String text) {
        if (isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void setSpeechRate(float rate) {
        tts.setSpeechRate(rate);
    }

    public void stopSpeaking() {
        if (tts != null && tts.isSpeaking()) {
            tts.stop();
        }
    }


    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
