package com.example.zombiesurvivor;

import android.content.Context;

public class Obstacle extends Destroyable{
    public Obstacle(Context context, double posX, double posY, int tailleX, int tailleY, double enfoncementTop, double enfoncementBottom,
                    double enfoncementLeft, double enfoncementRight,String cheminImages, boolean isAnimating, int timeCentiBetweenFrame, int hp) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiBetweenFrame, hp);
    }

}
