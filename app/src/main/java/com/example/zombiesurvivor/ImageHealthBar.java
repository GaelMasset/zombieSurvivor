package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.mobs.Player;

public class ImageHealthBar extends Image{
    protected Player joueur;
    public ImageHealthBar(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, int timeCentiBetweenFrame) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, timeCentiBetweenFrame);
        this.joueur = Game.getPartie().getJoueur();

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(animations.get(0).get((int) ((double)joueur.getHp()/joueur.getMaxHp()*10)), (float) posX, (float) posY, null);
    }
}
