package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Bouton extends Movable{
    private boolean isPressed;

    private ArrayList<Bitmap> imagesClicked = new ArrayList<Bitmap>();
    public Bouton(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, String cheminImagesClicked, boolean isAnimating, int timeCentiBetweenFrame) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, 0,0,0,0);
        //Decode all images that has the same name as the provided string
        int i = 1;
        boolean moreImg = true;
        while(moreImg){
            int resId = context.getResources().getIdentifier(cheminImagesClicked+i, "drawable", context.getPackageName());
            if(resId!=0){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                imagesClicked.add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                i++;
            }else{
                moreImg = false;
            }
        }
    }

    public boolean getIsPressed(){
        return this.isPressed;
    }

    public boolean isPressed(double x, double y){
        return x >= posX && x <= posX + tailleX && y >= posY && y <= posY + tailleY;
    }

    public void setIsPressed(boolean b) {
        this.isPressed = b;
    }

    @Override
    public void draw(Canvas canvas){
        if(!isPressed) canvas.drawBitmap(images.get(currentCentiFrame/timeCentiBetweenFrame), (float) posX, (float) posY, null);
        else canvas.drawBitmap(imagesClicked.get(currentCentiFrame/timeCentiBetweenFrame), (float) posX, (float) posY, null);


    }
}
