package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;

import java.util.ArrayList;

public abstract class Mob extends Destroyable {
    protected Action action = Action.IDLE;
    protected Action lastDirection = Action.WALKING_RIGHT;
    protected ArrayList<ArrayList<Bitmap>> animations = new ArrayList<ArrayList<Bitmap>>();

    protected double speed;

    public Mob(Context context, double posX, double posY, int tailleX,
               int tailleY, String cheminImages,
               double enfoncementTop, double enfoncementBottom,
               double enfoncementLeft, double enfoncementRight,
               boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp,
               double speed) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiBetweenFrame, hp, maxHp);
        this.speed = speed;

        int slot = 0;
        for(String animation: getAnimations()) {
            animations.add(new ArrayList<Bitmap>());
            int i = 1;
            boolean moreImg = true;
            while (moreImg) {
                int resId = context.getResources().getIdentifier(cheminImages + animation +i, "drawable", context.getPackageName());
                if (resId != 0) {
                    System.out.println("Trouvé pour le " + this.getClass().toString() + cheminImages + animation+ i);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    animations.get(slot).add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                    i++;
                } else {
                    moreImg = false;
                }
            }
            slot++;
        }
    }

    public abstract String[] getAnimations();

    @Override
    public void destroy() {

    }

    public double rangeXFrom(Movable m) {
        if (isAtTheRightOf(m)) {
            return this.getPosX() - (m.getPosX() + m.getTailleX());
        } else {
            return m.getPosX() - (this.getPosX() + this.tailleX);
        }
    }

    public double rangeYFrom(Movable m) {
        if (isUnderOf(m)) {
            return this.getPosY() + this.tailleY - m.getPosY();
        } else {
            return m.getPosY() + m.getTailleY() - this.getPosY();
        }
    }

    public boolean isUnderOf(Movable m) {
        return this.getPosY() >= m.getPosY() + m.getTailleY();
    }

    public boolean isAtTheRightOf(Movable m) {
        return this.getPosX() >= m.getPosX() + m.getTailleX();
    }


    public abstract boolean move();
}
