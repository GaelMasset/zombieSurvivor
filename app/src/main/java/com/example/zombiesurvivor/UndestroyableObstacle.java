package com.example.zombiesurvivor;

import android.content.Context;

public class UndestroyableObstacle extends Obstacle{
    public UndestroyableObstacle(Context context, double posX, double posY, int tailleX, int tailleY, double enfoncementTop, double enfoncementBottom,
                    double enfoncementLeft, double enfoncementRight,String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int maxHp, int hp) {
        super(context, posX, posY, tailleX, tailleY,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, cheminImages, isAnimating, timeCentiBetweenFrame, maxHp, hp);
    }

}
