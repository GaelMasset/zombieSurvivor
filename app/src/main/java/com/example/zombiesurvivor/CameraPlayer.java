package com.example.zombiesurvivor;

import static com.example.zombiesurvivor.Base.MainActivity.canvasHeight;
import static com.example.zombiesurvivor.Base.MainActivity.canvasWidth;

public class CameraPlayer extends Camera{
    public CameraPlayer() {
        super();
    }

    @Override
    public void update() {
        {
            posX = Game.getPartie().getJoueur().posX - (canvasWidth / 2.0);

            posY = -Game.getPartie().getJoueur().posY + (canvasHeight/2.0);
        }
    }
}
