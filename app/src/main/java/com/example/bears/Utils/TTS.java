package com.example.bears.Utils;

import android.app.Activity;
import android.content.Context;
import android.media.TimedText;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.bears.Activity.MainActivity;

import java.util.Locale;

public class TTS extends Activity implements TextToSpeech.OnInitListener {
    Context context;
    public TextToSpeech tts;

    public TTS(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        tts = new TextToSpeech(MainActivity.get)
//
//        // TODO : other code
//
//    }

    // 앱 종료시 등
    @Override
    protected void onDestroy() {

        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            // 한국어 설정
            int result = tts.setLanguage(Locale.KOREAN);

            // tts.setPitch(5); // set pitch level
            // tts.setSpeechRate(2); // set speech speed rate

            // 한국어가 안된다면,
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "지원하지 않는 언어입니다.");
            } else {
//                speech(getString(R.string.app_name) + "에 오신것을 환영합니다");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    // TODO : TTS
    public void speech(String text) {
        // tts가 사용중이면, 말하지않는다.
        if (!tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}
