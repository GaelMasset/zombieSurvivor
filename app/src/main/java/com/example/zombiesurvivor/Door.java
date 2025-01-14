package com.example.zombiesurvivor;

import android.content.Context;

public class Door extends Movable{
    private final int priceToOpen;
    private boolean isOpen = false;
    private Game partie;

    public Door(Context context, double posX, double posY,
                int tailleX, int tailleY,double enfoncementTop, double enfoncementBottom,
                double enfoncementLeft, double enfoncementRight, String cheminImages
            , boolean isAnimating, int timeCentiBetweenFrame, int priceToOpen, Game partie) {
        super(context, posX, posY, tailleX, tailleY,
                cheminImages, isAnimating, timeCentiBetweenFrame,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
        this.priceToOpen = priceToOpen;
        this.partie = partie;
    }


}
