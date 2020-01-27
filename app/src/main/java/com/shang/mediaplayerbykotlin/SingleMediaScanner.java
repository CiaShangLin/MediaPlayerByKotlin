package com.shang.mediaplayerbykotlin;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File[] mFile=new File[3];

    public SingleMediaScanner(Context context) {
        mFile[0]=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        mFile[1]=new File(Environment.getDataDirectory().getAbsolutePath());
        mFile[2]=new File(Environment.getRootDirectory().getAbsolutePath());
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        //https://www.cnblogs.com/plokmju/p/android_mediastore.html
        for (int i = 0; i < mFile.length;i++){
            mMs.scanFile(mFile[i].getAbsolutePath(), "audio/*");
        }

    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }

}
