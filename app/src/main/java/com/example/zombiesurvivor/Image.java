package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Image {
    final Context context;
    protected double posX;
    protected double posY;
    protected int tailleX;
    protected int tailleY;
    protected String cheminImages;
    protected int timeCentiBetweenFrame;
    protected int currentCentiFrame;
    protected ArrayList<ArrayList<Bitmap>> animations = new ArrayList<ArrayList<Bitmap>>();

    public Image(Context context, double posX, double posY, int tailleX, int tailleY,
                 String cheminImages, int timeCentiBetweenFrame) {
        this.context = context;
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.cheminImages = cheminImages;
        this.timeCentiBetweenFrame = timeCentiBetweenFrame;
        this.currentCentiFrame = 0;
        this.animations = new ArrayList<ArrayList<Bitmap>>();

        //Decode all images that has the same name as the provided string
        int i = 1;
        animations.add(new ArrayList<Bitmap>());
        boolean moreImg = true;
        while(moreImg){
            int resId = context.getResources().getIdentifier(cheminImages+i, "drawable", context.getPackageName());
            if(resId!=0){
                System.out.println("Img trouvée : " + i + cheminImages);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                animations.get(0).add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                i++;
            }else{
                moreImg = false;
            }
        }
    }

    public boolean isTouched(double x, double y) {
        return x >= posX && x <= (posX + tailleX) &&
                y >= posY && y <= (posY + tailleY);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public int getTailleX() {
        return tailleX;
    }

    public void setTailleX(int tailleX) {
        this.tailleX = tailleX;
    }

    public int getTailleY() {
        return tailleY;
    }

    public void setTailleY(int tailleY) {
        this.tailleY = tailleY;
    }

    public void draw(Canvas canvas){

    }
    public void update(){

    }
}
