package com.example.zombiesurvivor.Base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.zombiesurvivor.fenetres.Fenetre;
import com.example.zombiesurvivor.fenetres.FenetreAccueil;
import com.example.zombiesurvivor.fenetres.FenetreTest;
import com.example.zombiesurvivor.R;

public class Page extends SurfaceView implements SurfaceHolder.Callback {
    private PageLoop pageLoop;
    private Fenetre fenetre;

    public Page(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        pageLoop = new PageLoop(this, surfaceHolder);
        fenetre = new FenetreAccueil(this);
    }

    public void update(){
        fenetre.update();
    }
    public void draw(Canvas canvas){
        super.draw(canvas);

        fenetre.draw(canvas);

        /*drawFPS(canvas);
        drawUPS(canvas);*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return fenetre.onClick(event);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        pageLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    public Fenetre getFenetre(){
        return this.fenetre;
    }
    public void setFenetre(Fenetre f){
        this.fenetre = f;
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(pageLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(40);
        canvas.drawText("UPS : " + averageUPS, 100, 40, paint);
    }
    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(pageLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(40);
        canvas.drawText("FPS : " + averageFPS, 100, 80, paint);
    }
}
