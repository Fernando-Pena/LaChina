package com.china;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.name;

public class LaChina {

    public static void chinificate(View view){
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

    public static void chinificateRingtone(Context context){
        File file = new File(Environment.getExternalStorageDirectory(), "/ringtonFolder/Audio/");
        if (!file.exists()) {
            file.mkdirs();
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ringtonFolder/Audio/";

        File f = new File(path + "/", "china.mp3");

        Uri mUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/china.mp3");
        ContentResolver mCr = context.getContentResolver();
        AssetFileDescriptor soundFile;
        try {
            soundFile = mCr.openAssetFileDescriptor(mUri, "r");
        } catch (FileNotFoundException e) {
            soundFile = null;
        }

        try {
            byte[] readData = new byte[1024];
            FileInputStream fis = soundFile.createInputStream();
            FileOutputStream fos = new FileOutputStream(f);
            int i = fis.read(readData);

            while (i != -1) {
                fos.write(readData, 0, i);
                i = fis.read(readData);
            }

            fos.close();
        } catch (IOException io) {
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "china");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.MediaColumns.SIZE, f.length());
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(f.getAbsolutePath());
        Uri newUri = mCr.insert(uri, values);

        try {
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            Settings.System.putString(mCr, Settings.System.RINGTONE, newUri.toString());
        } catch (Throwable t) {

        }
    }
}
