package com.example.zombiesurvivor;

import android.content.Context;

public class Bow extends Weapon{

    protected String[] animations = new String[]{
            "_idle", "_walking", "_shootingbow"
    };

    @Override
    protected String[] getAnimations() {
        return new String[]{
                "_idle", "_walking", "_shootingbow"
        };
    }

    public Bow(double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, double precision, int firerate, Bullet munitionType, double heatPerFire, int timeCentiToUse) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame,postUseAnimTime, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, precision, firerate, munitionType, heatPerFire, timeCentiToUse);
    }
}
