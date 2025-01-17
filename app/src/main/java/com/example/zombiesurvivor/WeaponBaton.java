package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Path;

import com.example.zombiesurvivor.Player.Action;

public class WeaponBaton extends Item {
    private final int degats;
    private final int attackrange;

    public WeaponBaton(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, int timeCentiToUse, int degats, int attackrange) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame,postUseAnimTime, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiToUse, false, 1);
        this.degats = degats;
        this.attackrange = attackrange;
    }

    @Override
    public boolean use(Game game) {
        super.use(game);
        return hit(game);
    }

    @Override
    protected String[] getAnimations() {
        return new String[]{
                "_idle", "_walking", "_hittingsword"
        };
    }

    //CHANGE OBSTACLES PAR ENEMIES ?
    private boolean hit(Game game) {
        Movable hitZone = this.clone();
        if (game.getJoueur().getLastDirection() == Action.WALKING_RIGHT) {
            hitZone.setTailleX(hitZone.getTailleX() + attackrange);
        } else {
            hitZone.setTailleY(hitZone.getTailleY() - attackrange);
        }
        Movable target = Movable.isOneTouching(hitZone, game.getCarte().getObstacles());
        if (target != null) {
            if (target instanceof Destroyable) {
                ((Destroyable) target).damage(this.degats);
            }
        }
        return true;
    }
}
