package com.example.bears.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bears.Activity.MainActivity;
import com.example.bears.R;

import java.util.ArrayList;

public class STT implements RecognitionListener {
    Context context;
    TTS tts;
    ImageView iv_voice;
    LottieAnimationView anim_stt;

    public STT(Context context) {
        this.context = context;
        tts = new TTS(context);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        iv_voice = ((MainActivity) context).findViewById(R.id.iv_voice);
        anim_stt = ((MainActivity) context).findViewById(R.id.anim_stt);
        anim_stt.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.drawable.voice2);
        anim_stt.playAnimation();
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        iv_voice.setImageResource(R.drawable.voice);
        anim_stt.pauseAnimation();
        anim_stt.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(int error) {
        String message;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "오디오 에러";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "클라이언트 에러";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "퍼미션 없음";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "네트워크 에러";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "네트웍 타임아웃";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "찾을 수 없음";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RECOGNIZER가 바쁘다";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "서버가 이상함";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "말하는 시간초과";
                break;
            default:
                message = "알 수 없는 오류임";
                break;
        }
        Log.d("음성인식 에러메시지", message);
    }

    @Override
    public void onResults(Bundle results) { // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어 줌
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String match = matches.toString();
        Log.d("ArrayList", match);
        Intent intent = new Intent(context, MainActivity.class);

        String result = match.substring(1, match.length()-1);

        if (!matches.isEmpty()) {
            intent.putExtra("busnumber", result);
            tts.speech(result+"번으로 검색합니다.");
            context.startActivity(intent);
        } else
            tts.speech("검색어를 찾을 수 없습니다");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
