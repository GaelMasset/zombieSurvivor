package com.example.zombiesurvivor;

import android.content.Context;

public class Floor extends Movable{


    public Floor(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
    }
}
