package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


import java.util.ArrayList;

public abstract class Movable implements Cloneable{

    final Context context;
    protected double posX;
    protected double posY;
    protected int tailleX;
    protected int tailleY;
    protected double enfoncementTop;
    protected double enfoncementBottom;
    protected double enfoncementLeft;
    protected double enfoncementRight;
    protected String cheminImages;
    protected boolean isAnimating;
    final int timeCentiBetweenFrame;
    private int currentCentiFrame;
    private ArrayList<Bitmap> images = new ArrayList<Bitmap>();

    /*
    Constructor
     */
    public Movable(Context context, double posX, double posY, int tailleX, int tailleY,
                   String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,
                   double enfoncementTop, double enfoncementBottom,
                   double enfoncementLeft, double enfoncementRight){
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.cheminImages = cheminImages;
        this.isAnimating = isAnimating;
        this.timeCentiBetweenFrame = timeCentiBetweenFrame;
        this.enfoncementBottom = enfoncementBottom;
        this.enfoncementTop = enfoncementTop;
        this.enfoncementLeft = enfoncementLeft;
        this.enfoncementRight = enfoncementRight;
        this.context = context;

        //Decode all images that has the same name as the provided string
        int i = 1;
        boolean moreImg = true;
        while(moreImg){
            int resId = context.getResources().getIdentifier(cheminImages+i, "drawable", context.getPackageName());
            if(resId!=0){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                images.add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                i++;
            }else{
                moreImg = false;
            }
        }


    }

    /*
    Clone method
     */
    @Override
    public Movable clone() {
        Movable clone = null;
        try {
            clone = (Movable) super.clone();
            clone.posX = this.posX;
            clone.posY = this.posY;
            clone.tailleX = this.tailleX;
            clone.tailleY = this.tailleY;
            clone.cheminImages = this.cheminImages;
            clone.isAnimating = this.isAnimating;
            clone.images = new ArrayList<>(this.images);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
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
        canvas.drawBitmap(images.get(currentCentiFrame/timeCentiBetweenFrame), (float) posX, (float) posY, null);
    }
    public void update(){
        if(isAnimating) {
            currentCentiFrame++;
            currentCentiFrame = currentCentiFrame%(images.size()*timeCentiBetweenFrame);
        }
        else currentCentiFrame = 0;
    }

    /*
    Static methods
     */

    /*
    Check if the to movables are touching
     */
    public static boolean areTouching(Movable m1, Movable m2) {
        // Définition des coordonnées des coins des deux rectangles avec enfoncement
        double m1Left = m1.getPosX() + m1.enfoncementLeft;
        double m1Right = m1.getPosX() + m1.getTailleX() - m1.enfoncementRight;
        double m1Top = m1.getPosY() + m1.enfoncementTop;
        double m1Bottom = m1.getPosY() + m1.getTailleY() - m1.enfoncementBottom;

        double m2Left = m2.getPosX() + m2.enfoncementLeft;
        double m2Right = m2.getPosX() + m2.getTailleX() - m2.enfoncementRight;
        double m2Top = m2.getPosY() + m2.enfoncementTop;
        double m2Bottom = m2.getPosY() + m2.getTailleY() - m2.enfoncementBottom;

        // Vérifier si les deux rectangles se touchent ou se chevauchent en tenant compte des enfoncements
        return !(m1Right <= m2Left || // m1 est à gauche de m2
                m1Left >= m2Right || // m1 est à droite de m2
                m1Bottom <= m2Top || // m1 est au-dessus de m2
                m1Top >= m2Bottom); // m1 est en dessous de m2
    }


    /*
    Check if a movable is touching one of m2 movable
     */
    public static Movable isOneTouching(Movable m1, ArrayList<? extends Movable> m2){
        for(Movable m: m2){
            if(areTouching(m1, m)) return m;
        }
        return null;
    }


    /*
    Check if a movable is going to touch any movables in m2 and return all that would be touched
    */
    public static ArrayList<Movable> getAllGonnaTouchMovables(Movable m1, ArrayList<? extends Movable> m2, double x, double y) {
        ArrayList<Movable> touchingMovables = new ArrayList<>();
        Movable nextMov = m1.clone();
        nextMov.posX += x;
        nextMov.posY += y;

        for(Movable m: m2) {
            if(areTouching(nextMov, m)) {
                touchingMovables.add(m);
            }
        }
        return touchingMovables;
    }

}
