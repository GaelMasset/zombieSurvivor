package com.example.zombiesurvivor;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Page extends SurfaceView implements SurfaceHolder.Callback {
    private PageLoop gameLoop;

    public Page(Context context) {
        super(context);
    }

    public void update(){

    }
    public void draw(){

    }






    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
