package com.example.zombiesurvivor;

import android.content.Context;

public class Zombie extends EtreHumain{
    public Zombie(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages,double enfoncementTop, double enfoncementBottom,
                  double enfoncementLeft, double enfoncementRight ,boolean isAnimating, int timeCentiBetweenFrame,int maxHp, int hp, double speed, Game partie) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame,maxHp, hp, speed, partie);
    }

    @Override
    public boolean move() {
        return false;
    }
}
