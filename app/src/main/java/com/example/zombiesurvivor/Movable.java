package com.example.zombiesurvivor;

import android.graphics.Canvas;

import java.util.ArrayList;

public abstract class Movable {

    protected double posX;
    protected double posY;
    protected int tailleX;
    protected int tailleY;
    protected String cheminImages;
    protected boolean isAnimating;

    /*
    Constructor
     */
    public Movable(double posX, double posY, int tailleX, int tailleY,
                   String cheminImages, boolean isAnimating){
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.cheminImages = cheminImages;
        this.isAnimating = isAnimating;

    }

    /*
    Getters
     */
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getTailleX() {
        return tailleX;
    }

    public int getTailleY() {
        return tailleY;
    }

    /*
    Setters
     */

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setTailleX(int tailleX) {
        this.tailleX = tailleX;
    }

    public void setTailleY(int tailleY) {
        this.tailleY = tailleY;
    }

    /*
    Methods
     */
    public void draw(Canvas canvas){

    }

    /*
    Static methods
     */

    /*
    Check if the to movables are touching
     */
    public static boolean areTouching(Movable m1, Movable m2) {
        // Définition des coordonnées des coins des deux rectangles
        double m1Right = m1.getPosX() + m1.getTailleX();
        double m1Bottom = m1.getPosY() + m1.getTailleY();

        double m2Right = m2.getPosX() + m2.getTailleX();
        double m2Bottom = m2.getPosY() + m2.getTailleY();

        // Vérifier si les deux rectangles se touchent ou se chevauchent
        return !(m1Right <= m2.getPosX() || // m1 est à gauche de m2
                m1.getPosX() >= m2Right || // m1 est à droite de m2
                m1Bottom <= m2.getPosY() || // m1 est au-dessus de m2
                m1.getPosY() >= m2Bottom); // m1 est en dessous de m2
    }

    /*
    Check if a movable is touching one of m2 movable
     */
    public static Movable isOneTouching(Movable m1, ArrayList<Movable> m2){
        for(Movable m: m2){
            if(areTouching(m1, m)) return m;
        }
        return null;
    }

}
