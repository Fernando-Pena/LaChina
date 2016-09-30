package com.china;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LaChina {

    public static final int CODE_WRITE_SETTINGS_PERMISSION = 100;

    public static void chinificate(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view.getContext());
            }
        });
    }

    public static void play(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        MediaPlayer.create(context, R.raw.china).start();
    }

    public static void chinificateRingtone(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(), "/ringtonFolder/Audio/");
        if (!file.exists()) {
            file.mkdirs();
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ringtonFolder/Audio/";

        File f = new File(path + "/", "china.mp3");

        Uri mUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/china");
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
            io.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "China");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.MediaColumns.SIZE, f.length());
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(f.getAbsolutePath());
        mCr.delete(uri, MediaStore.MediaColumns.DATA + "=\"" + f.getAbsolutePath() + "\"", null);
        Uri newUri = mCr.insert(uri, values);

        try {
            if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(context)) {
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
                Settings.System.putString(mCr, Settings.System.RINGTONE, newUri.toString());
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    if (context instanceof Activity) {
                        ((Activity) context).startActivityForResult(intent, CODE_WRITE_SETTINGS_PERMISSION);
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
