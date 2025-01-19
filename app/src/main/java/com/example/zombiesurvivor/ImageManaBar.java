package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.mobs.Player;

public class ImageManaBar extends Image{
    protected Player joueur;
    public ImageManaBar(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, int timeCentiBetweenFrame, Player joueur) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, timeCentiBetweenFrame);
        this.joueur = joueur;

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(animations.get(0).get((int) ((double)joueur.getMana()/joueur.getMaxMana()*10)), (float) posX, (float) posY, null);
    }
}
