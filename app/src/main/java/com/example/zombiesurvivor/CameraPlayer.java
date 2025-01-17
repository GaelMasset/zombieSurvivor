package com.example.zombiesurvivor;

import static com.example.zombiesurvivor.Base.MainActivity.canvasHeight;
import static com.example.zombiesurvivor.Base.MainActivity.canvasWidth;

public class CameraPlayer extends Camera{
    public CameraPlayer(Game partie) {
        super(partie);
    }

    @Override
    public void update() {
        {
            posX = partie.getJoueur().posX - (canvasWidth / 2.0);

            posY = -partie.getJoueur().posY + (canvasHeight/2.0);
        }
    }
}
