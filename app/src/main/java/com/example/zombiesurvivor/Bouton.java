package com.example.zombiesurvivor;

import android.content.Context;

public class Bouton extends Movable{
    private boolean isPressed;
    public Bouton(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
    }

    public boolean getIsPressed(){
        return this.isPressed;
    }

    public boolean isPressed(double x, double y){
        return x >= posX && x <= posX + tailleX && y >= posY && y <= posY + tailleY;
    }

    public void setIsPressed(boolean b) {
        this.isPressed = b;
    }
}
