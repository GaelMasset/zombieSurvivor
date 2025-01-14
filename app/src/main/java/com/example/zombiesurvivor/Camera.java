package com.example.zombiesurvivor;

public abstract class Camera {
    protected double posX = 0;
    protected double posY = 0;
    protected Game partie;
    public Camera(Game partie){
        this.partie = partie;
    }
    public float getPosX(){
        return (float) posX;
    }
    public float getPosY(){
        return (float) posY;
    }

    public abstract void update();


}
