package com.example.zombiesurvivor;

import android.content.Context;

import java.util.ArrayList;

public class Player extends EtreHumain{
    private static final double MAX_SPEEED = 0.2;
    private Game partie;
    private Joystick joyStickDeplacement;
    private Joystick joyStickArme;
    private Weapon weapon;
    private int solde;


    public Player(Context context, int posX, int posY, int tailleX, int tailleY, String floor,double enfoncementTop, double enfoncementBottom,
                  double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int speed, Joystick joystickDeplacement, Joystick joystickAttaque, Game partie, Weapon weapon) {
        super(context, posX, posY, tailleX, tailleY, floor,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp, speed, partie);
        this.joyStickDeplacement = joystickDeplacement;
        this.joyStickArme = joystickAttaque;
        this.weapon = weapon;
    }

    @Override
    public boolean move() {
        double futurePosX = posX + joyStickDeplacement.actuatorX * speed * MAX_SPEEED;
        double futurePosY = posY + joyStickDeplacement.actuatorY * speed * MAX_SPEEED;

        if (joyStickDeplacement.actuatorX != 0 || joyStickDeplacement.actuatorY != 0) isAnimating = true;
        else isAnimating = false;

        ArrayList<Movable> obstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                joyStickDeplacement.actuatorX * speed, joyStickDeplacement.actuatorY * speed);

        if (obstacles.isEmpty()) {
            posX = futurePosX;
            posY = futurePosY;
            return true;
        } else {
            ArrayList<Movable> xObstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                    joyStickDeplacement.actuatorX * speed, 0);

            ArrayList<Movable> yObstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                    0, joyStickDeplacement.actuatorY * speed);

            double deltaX = 0;
            double deltaY = 0;

            if (xObstacles.isEmpty()) {
                posX = futurePosX;
            } else if (joyStickDeplacement.actuatorX != 0) {
                Movable nearestX = findNearestObstacle(xObstacles, true);
                if (joyStickDeplacement.actuatorX > 0) {
                    deltaX = nearestX.getPosX() + nearestX.enfoncementLeft - (posX + tailleX - enfoncementRight);
                } else {
                    deltaX = (nearestX.getPosX() + nearestX.getTailleX() - nearestX.enfoncementRight) - (posX + enfoncementLeft);
                }
                posX += Math.abs(deltaX) < Math.abs(joyStickDeplacement.actuatorX * speed) ? deltaX : joyStickDeplacement.actuatorX * speed;
            }

            if (yObstacles.isEmpty()) {
                posY = futurePosY;
            } else if (joyStickDeplacement.actuatorY != 0) {
                Movable nearestY = findNearestObstacle(yObstacles, false);
                if (joyStickDeplacement.actuatorY > 0) {
                    deltaY = nearestY.getPosY() + nearestY.enfoncementTop - (posY + tailleY - enfoncementBottom);
                } else {
                    deltaY = (nearestY.getPosY() + nearestY.getTailleY() - nearestY.enfoncementBottom) - (posY + enfoncementTop);
                }
                posY += Math.abs(deltaY) < Math.abs(joyStickDeplacement.actuatorY * speed) ? deltaY : joyStickDeplacement.actuatorY * speed;
            }
        }
        return false;
    }


    private Movable findNearestObstacle(ArrayList<Movable> obstacles, boolean checkX) {
        if (obstacles.isEmpty()) return null;

        Movable nearest = obstacles.get(0);
        double minDist = Double.MAX_VALUE;

        for (Movable obstacle : obstacles) {
            double dist;
            if (checkX) {
                double m1Right = posX + tailleX - enfoncementRight;
                double m1Left = posX + enfoncementLeft;
                double m2Right = obstacle.getPosX() + obstacle.getTailleX() - obstacle.enfoncementRight;
                double m2Left = obstacle.getPosX() + obstacle.enfoncementLeft;

                dist = joyStickDeplacement.actuatorX > 0 ?
                        m2Left - m1Right : // Distance en X droite
                        m1Left - m2Right; // Distance en X gauche
            } else {
                double m1Bottom = posY + tailleY - enfoncementBottom;
                double m1Top = posY + enfoncementTop;
                double m2Bottom = obstacle.getPosY() + obstacle.getTailleY() - obstacle.enfoncementBottom;
                double m2Top = obstacle.getPosY() + obstacle.enfoncementTop;

                dist = joyStickDeplacement.actuatorY > 0 ?
                        m2Top - m1Bottom : // Distance en Y bas
                        m1Top - m2Bottom; // Distance en Y haut
            }

            if (dist < minDist) {
                minDist = dist;
                nearest = obstacle;
            }
        }
        return nearest;
    }








    public boolean fire(){
        if(joyStickArme.getIsPressed()){
            return weapon.fire(joyStickArme);
        }
        return false;
    }

    @Override
    public void update(){
        super.update();
        move();
        fire();
    }

    public void setGame(Game game) {
        this.partie = game;
    }
}
