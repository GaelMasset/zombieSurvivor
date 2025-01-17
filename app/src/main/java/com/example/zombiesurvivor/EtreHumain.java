package com.example.zombiesurvivor;

import android.content.Context;

public abstract class EtreHumain extends Destroyable{
    protected double speed;

    public EtreHumain(Context context, double posX, double posY, int tailleX,
                      int tailleY, String cheminImages,
                      double enfoncementTop, double enfoncementBottom,
                      double enfoncementLeft, double enfoncementRight,
                      boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp,
                      double speed, Game partie) {
        super(context, posX, posY, tailleX, tailleY, cheminImages,isAnimating, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiBetweenFrame,hp, maxHp);
        this.speed = speed;
    }

    @Override
    public void destroy() {

    }
    public abstract boolean move();
}
