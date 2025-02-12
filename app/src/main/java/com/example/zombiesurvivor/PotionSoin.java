package com.example.zombiesurvivor;

import android.content.Context;

public class PotionSoin extends Item{
    int soin;

    public PotionSoin(double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, int timeCentiToUse, int soin) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame,postUseAnimTime ,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiToUse, false, 1);
        this.soin = soin;
    }

    @Override
    protected String[] getAnimations() {
        return new String[]{
                "_idle", "_walking", "_drinking"
        };
    }

    @Override
    public boolean use(){
        super.use();
        Game.getPartie().getJoueur().soigner(soin);
        return false;
    }
}
