package com.example.bears;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bears.Activity.MainActivity;
import com.example.bears.Activity.SearchResultActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STT implements RecognitionListener {
    Context context;
    TTS tts;
    ImageView iv_voice;

    public STT(Context context) {
        this.context = context;
        tts = new TTS(context);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
//        수정해야함
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main, null, false);
        iv_voice = view.findViewById(R.id.iv_voice);
        iv_voice.setImageResource(R.drawable.voice2);
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
        Intent intent = new Intent(context, SearchResultActivity.class);

        String result = match.substring(1, match.length()-1);
        Toast.makeText(context, "음성인식 결과 : " + result, Toast.LENGTH_SHORT).show();

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
