package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Zone extends Movable{
    public Paint paint = new Paint();

    public Zone(double posX, double posY, int tailleX, int tailleY) {
        super(posX, posY, tailleX, tailleY, "vide", false, 100, 0, 0, 0, 0);
    }

    public void setPaintColor(int color){
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect((float) posX, (float) posY, (float) posX+tailleX, (float) posY+tailleY, paint);
    }
}
