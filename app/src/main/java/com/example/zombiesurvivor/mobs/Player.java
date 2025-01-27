package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Bow;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Inventory;
import com.example.zombiesurvivor.Item;
import com.example.zombiesurvivor.Joystick;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.PotionSoin;
import com.example.zombiesurvivor.WeaponBaton;

import java.util.ArrayList;

public class Player extends Mob {
    private static final int NB_SLOT_HOTBAR = 4;
    private static final double MAX_SPEEED = 0.2;
    private Inventory hotbar = new Inventory(NB_SLOT_HOTBAR);
    private int mana;
    private int maxMana;
    private Joystick joyStickDeplacement;
    private Bouton joyStickArme;
    private int solde;




    public Player(Context context, int posX, int posY, int tailleX, int tailleY, String floor, int maxHp,double enfoncementTop, double enfoncementBottom,
                  double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int manaMax, double speed, Joystick joystickDeplacement, Bouton joystickAttaque, Game partie) {
        super(context, posX, posY, tailleX, tailleY, floor,enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp , maxHp, speed, partie);
        this.joyStickDeplacement = joystickDeplacement;
        this.joyStickArme = joystickAttaque;
        this.maxMana = manaMax;
        this.mana = manaMax;

    }

    @Override
    public String[] getAnimations() {
        return new String[]{"_idle","_walking","_shootingbow", "_drinking", "_hittingsword"};
    }

    @Override
    public boolean move() {
        double futurePosX = posX + joyStickDeplacement.actuatorX * speed * MAX_SPEEED;
        double futurePosY = posY + joyStickDeplacement.getActuatorY() * speed * MAX_SPEEED;

        if(!(action == Action.WALKING_RIGHT || action == Action.WALKING_LEFT)) {
            futurePosX -= (joyStickDeplacement.actuatorX * speed * MAX_SPEEED)/1.33;
            futurePosY -= (joyStickDeplacement.getActuatorY() * speed * MAX_SPEEED)/1.33;
        }


            ArrayList<Movable> obstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                    joyStickDeplacement.actuatorX * speed, joyStickDeplacement.getActuatorY() * speed);

            if (obstacles.isEmpty()) {
                posX = futurePosX;
                posY = futurePosY;
                return true;
            } else {
                ArrayList<Movable> xObstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                        joyStickDeplacement.actuatorX * speed, 0);

                ArrayList<Movable> yObstacles = getAllGonnaTouchMovables(this, partie.getCarte().getObstacles(),
                        0, joyStickDeplacement.getActuatorY() * speed);

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
                } else if (joyStickDeplacement.getActuatorY() != 0) {
                    Movable nearestY = findNearestObstacle(yObstacles, false);
                    if (joyStickDeplacement.getActuatorY() > 0) {
                        deltaY = nearestY.getPosY() + nearestY.enfoncementTop - (posY + tailleY - enfoncementBottom);
                    } else {
                        deltaY = (nearestY.getPosY() + nearestY.getTailleY() - nearestY.enfoncementBottom) - (posY + enfoncementTop);
                    }
                    posY += Math.abs(deltaY) < Math.abs(joyStickDeplacement.getActuatorY() * speed) ? deltaY : joyStickDeplacement.getActuatorY() * speed;
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

                dist = joyStickDeplacement.getActuatorY() > 0 ?
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

    @Override
    public void update() {
        updateAction();
        switch (action){
            case USING:

        }
        getBonNbAnimJoueur();
        if (isAnimating) {
            currentCentiFrame++;
            switch (action) {
                case IDLE:
                    timeCentiBetweenFrame = 50;
                    currentCentiFrame = currentCentiFrame % (animations.get(0).size() * timeCentiBetweenFrame);

                    break;
                case WALKING_LEFT:
                case WALKING_RIGHT:
                    timeCentiBetweenFrame = 18;
                    currentCentiFrame = currentCentiFrame % (animations.get(1).size() * timeCentiBetweenFrame);
                    break;
                case USING:
                    if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof Bow){
                        timeCentiBetweenFrame = 25;
                        currentCentiFrame = currentCentiFrame % (animations.get(2).size() * timeCentiBetweenFrame);
                    }
                    if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof PotionSoin){
                        timeCentiBetweenFrame = 50;
                        currentCentiFrame = currentCentiFrame % (animations.get(3).size() * timeCentiBetweenFrame);
                    }
                    if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof WeaponBaton){
                        timeCentiBetweenFrame = 20;
                        currentCentiFrame = currentCentiFrame % (animations.get(4).size() * timeCentiBetweenFrame);
                    }
                default:
                    break;
            }
        } else {
            currentCentiFrame = 0;
        }

        move();
        if(hotbar.getSelectedStack() != null)hotbar.getSelectedStack().getItem().update();


        if(hotbar.getSelectedStack() != null)hotbar.getSelectedStack().getItem().setPosX(this.getPosX());
        if(hotbar.getSelectedStack() != null)hotbar.getSelectedStack().getItem().setPosY(this.getPosY());

    }

    private int getBonNbAnimJoueur() {
        if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof Bow) return 2;
        if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof PotionSoin) return 3;
        if(hotbar.getSelectedStack() != null && hotbar.getSelectedStack().getItem() instanceof WeaponBaton) return 4;
        return 0;
    }


    private void updateAction() {
        if(hotbar.getSelectedStack() != null && !hotbar.getSelectedStack().getItem().animationOver()){
            action=Action.USING;
            return;
        }
        if(joyStickArme.getIsPressed()){
            action = Action.CHARGING_ATTACK;
            return;
        } else if(joyStickDeplacement.getIsPressed()) {
            if (joyStickDeplacement.getActuatorX() > 0) {
                action = Action.WALKING_RIGHT;
                lastDirection = Action.WALKING_RIGHT;
            } else {
                action = Action.WALKING_LEFT;
                lastDirection = Action.WALKING_LEFT;
            }
            return;
        } else{
            action = Action.IDLE;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // Dessiner le joueur en fonction de son action et sa direction
        switch (action) {
            case WALKING_RIGHT:
            case WALKING_LEFT:
                switch (lastDirection) {
                    case WALKING_RIGHT:
                        canvas.drawBitmap(
                                animations.get(1).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null)canvas.drawBitmap(
                                hotbar.getSelectedStack().getItem().getAnimationsItem().get(1).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                    case WALKING_LEFT:
                        canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        animations.get(1).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null)canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        hotbar.getSelectedStack().getItem().getAnimationsItem().get(1).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                }
                break;
            case IDLE:
                switch (lastDirection) {
                    case WALKING_LEFT:
                        canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        animations.get(0).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null)canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        hotbar.getSelectedStack().getItem().getAnimationsItem().get(0).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                    case WALKING_RIGHT:
                        canvas.drawBitmap(
                                animations.get(0).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null)canvas.drawBitmap(
                                hotbar.getSelectedStack().getItem().getAnimationsItem().get(0).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                }
                break;
            case USING:
                switch (lastDirection) {
                    case WALKING_LEFT:
                        canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        animations.get(getBonNbAnimJoueur()).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null) canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        hotbar.getSelectedStack().getItem().getAnimationsItem().get(2).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                    case WALKING_RIGHT:
                        canvas.drawBitmap(
                                animations.get(getBonNbAnimJoueur()).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        if(hotbar.getSelectedStack() != null)canvas.drawBitmap(
                                hotbar.getSelectedStack().getItem().getAnimationsItem().get(2).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                }
        }
    }

    @Override
    public void damage(int hp){
        super.damage(hp);
        System.out.println("OUIIILLLLLEELELLELELE");
    }

    public void setGame(Game game) {
        this.partie = game;
    }

    public Bouton getJoystickAttaque() {
        return this.joyStickArme;
    }

    public Item getItem() {
        return this.hotbar.getSelectedStack().getItem();
    }
    public boolean useItem(){
        if(hotbar.getSelectedStack() != null)this.hotbar.getSelectedStack().getItem().startUsing();
        currentCentiFrame = 0;
        return true;
    }

    public Action getAction() {
        return this.action;
    }

    public Action getLastDirection() {
        return this.lastDirection;
    }


    public int getMana() {
        return this.mana;
    }
    public int getMaxMana(){
        return this.maxMana;
    }



    public Inventory getHotbar() {
        return hotbar;
    }

    public void setSpawnPoint(double x, double y){
        this.posX = x;
        this.posY = y;
    }
}
