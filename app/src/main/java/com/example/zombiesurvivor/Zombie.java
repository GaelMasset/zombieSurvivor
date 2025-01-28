package com.example.zombiesurvivor;

import android.content.Context;

import com.example.zombiesurvivor.mobs.Mob;

public class Zombie extends Mob {
    public Zombie(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages,double enfoncementTop, double enfoncementBottom,
                  double enfoncementLeft, double enfoncementRight ,boolean isAnimating, int timeCentiBetweenFrame,int maxHp, int hp, double speed) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame,maxHp, hp, speed);
    }

    @Override
    public String[] getAnimations() {
        return new String[0];
    }

    @Override
    public boolean move() {
        return false;
    }
}
