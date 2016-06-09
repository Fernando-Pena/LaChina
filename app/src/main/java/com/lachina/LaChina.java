package com.lachina;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;

public class LaChina {

    public static void chinificar(View view, final boolean multichina){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view.getContext());
            }
        });
    }

    public static void play(Context context){
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        MediaPlayer.create(context, R.raw.china).start();
    }
}
