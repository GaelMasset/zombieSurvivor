package com.example.zombiesurvivor;

import android.content.Context;

import com.example.zombiesurvivor.mobs.Action;

public class WeaponBaton extends Item {
    private final int degats;
    private final int attackrange;

    public WeaponBaton(double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, int timeCentiToUse, int degats, int attackrange) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame,postUseAnimTime, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiToUse, false, 1);
        this.degats = degats;
        this.attackrange = attackrange;
    }

    @Override
    public boolean use() {
        super.use();
        return hit();
    }

    @Override
    protected String[] getAnimations() {
        return new String[]{
                "_idle", "_walking", "_hittingsword"
        };
    }

    //CHANGE OBSTACLES PAR ENEMIES ?
    private boolean hit() {
        Movable hitZone;
        if (Game.getPartie().getJoueur().getLastDirection() == Action.WALKING_RIGHT) {
             hitZone = new Zone(
                     Game.getPartie().getJoueur().posX+Game.getPartie().getJoueur().tailleX,
                     Game.getPartie().getJoueur().posY,
                     this.attackrange,
                     Game.getPartie().getJoueur().tailleY);
        } else {
            hitZone = new Zone(
                    Game.getPartie().getJoueur().posX-attackrange,
                    Game.getPartie().getJoueur().posY,
                    (int) Game.getPartie().getJoueur().posX,
                    Game.getPartie().getJoueur().tailleY);
        }
        Movable target = Movable.isOneTouching(hitZone, Game.getPartie().getMobs());
        if (target != null) {
            if (target instanceof Destroyable) {
                ((Destroyable) target).damage(this.degats);
            }
        }
        return true;
    }
}
