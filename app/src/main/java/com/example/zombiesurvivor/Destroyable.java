package com.example.zombiesurvivor;

import android.content.Context;

public abstract class Destroyable extends Movable{
    private int hp;
    private final int maxHp;

    public Destroyable(double posX,
                       double posY, int tailleX, int tailleY,
                       String cheminImages, boolean isAnimating,
                       double enfoncementTop, double enfoncementBottom,
                       double enfoncementLeft, double enfoncementRight,
                       int timeCentiBetweenFrame, int hp, int maxHp) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
        this.hp = hp;
        this.maxHp = maxHp;
    }
    public void damage(int hp){
        this.hp-=hp;
        if(this.hp<=0){
            destroy();
        }
    }
    public void destroy(){
        Game.getPartie().getCarte().setObjetsCarte(this.getPosXTile(false), this.getPosYTile(false), 1, null);
    }
    public int getHp(){
        return hp;
    }
    public int getMaxHp() {
        return maxHp;
    }
    public void soigner(int soin) {
        this.hp += soin;
        if(hp > maxHp) hp = maxHp;
    }
}
