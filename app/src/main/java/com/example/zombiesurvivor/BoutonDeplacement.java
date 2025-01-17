package com.example.zombiesurvivor;

import android.graphics.Canvas;

public abstract class BoutonDeplacement {
    protected boolean isPressed;
    public double actuatorX;
    protected double actuatorY;

    public abstract void draw(Canvas canvas);
    public abstract void update();
    public abstract void setIsPressed(boolean b);
    public abstract boolean getIsPressed();
    public double getActuatorX() {
        return this.actuatorX;
    }
    public double getActuatorY() {
        return this.actuatorY;
    }



    public abstract boolean isPressed(double x, double y);

    public abstract void setActuator(double x, double y);
    public abstract void resetActuator();
}
