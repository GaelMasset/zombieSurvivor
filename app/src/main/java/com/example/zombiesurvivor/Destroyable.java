package com.example.zombiesurvivor;

import android.content.Context;

public abstract class Destroyable extends Movable{
    private int hp;
    private Game partie;
    public Destroyable(Context context, double posX,
                       double posY, int tailleX, int tailleY,
                       String cheminImages, boolean isAnimating,
                       double enfoncementTop, double enfoncementBottom,
                       double enfoncementLeft, double enfoncementRight,
                       int timeCentiBetweenFrame, int hp) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
        this.hp = hp;
    }
    public void damage(int hp){
        if(this instanceof UndestroyableObstacle){
            return;
        }
        this.hp-=hp;
        if(this.hp<=0){
            destroy();
        }
    }
    public void destroy(){
        partie.getCarte().getObstacles().remove(this);
    }
    public int getHp(){
        return hp;
    }
    public void setPartie(Game partie){
        this.partie = partie;
    }
}
