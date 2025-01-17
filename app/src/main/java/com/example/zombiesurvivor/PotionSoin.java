package com.example.zombiesurvivor;

import android.content.Context;

public class PotionSoin extends Item{
    int soin;

    public PotionSoin(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, int timeCentiToUse, int soin) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiToUse, false, 1);
        this.soin = soin;
    }

    @Override
    protected String[] getAnimations() {
        return new String[]{
                "_idle", "_walking", "_drinking"
        };
    }

    @Override
    public boolean use(Game partie){
        super.use(partie);
        partie.getJoueur().soigner(soin);
        return false;
    }
}
