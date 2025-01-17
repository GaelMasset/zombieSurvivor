package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

public class ImageHotbar extends Image{
    public boolean isSelected;
    private ImageHotbar[] ligneAppartenant;

    public ImageHotbar(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, int timeCentiBetweenFrame) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, timeCentiBetweenFrame);
    }

    public void select(){
        for(ImageHotbar i: ligneAppartenant){
            i.unselect();
        }
        this.isSelected = true;
    }
    public void unselect(){
        this.isSelected = false;
    }

    @Override
    public void draw(Canvas canvas){
        if(!isSelected) {
            canvas.drawBitmap(animations.get(0).get(0), (float) posX, (float) posY, null);
        } else{
            canvas.drawBitmap(animations.get(0).get(1), (float) posX, (float) posY, null);
        }
    }

    public void setHotbar(ImageHotbar[] hotbar) {
        this.ligneAppartenant = hotbar;
    }
}
