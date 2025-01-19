package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Zone extends Movable{

    public Zone(Context context, double posX, double posY, int tailleX, int tailleY) {
        super(context, posX, posY, tailleX, tailleY, "vide", false, 100, 0, 0, 0, 00);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect((float) posX, (float) posY, (float) posX+tailleX, (float) posY+tailleY, new Paint());
    }
}
