package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public abstract class Item extends Movable{
    protected String nom;
    protected int timeCentiToUse;
    protected int postUseAnimTime;
    private int currentUseTimer = -1;
    private boolean used = true;
    private boolean isStackable;
    private int tailleStack;
    private Bitmap icone;
    private ArrayList<ArrayList<Bitmap>> animationsItem = new ArrayList<ArrayList<Bitmap>>();

    protected Game partie;


    public Item(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime ,double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, int timeCentiToUse, boolean isStackable, int tailleStack) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
        this.timeCentiToUse = timeCentiToUse;
        this.isStackable = isStackable;
        this.postUseAnimTime = postUseAnimTime;
        System.out.println("Inintialisation de " + timeCentiToUse + " et " + postUseAnimTime);
        if(isStackable) tailleStack = tailleStack;
        else tailleStack = 1;

        //Decode all images that has the same name as the provided string
        int slot = 0;
        for(String animation: getAnimations()) {
            animationsItem.add(new ArrayList<Bitmap>());
            int i = 1;
            boolean moreImg = true;
            while (moreImg) {
                int resId = context.getResources().getIdentifier(cheminImages + animation +i, "drawable", context.getPackageName());
                if (resId != 0) {
                    System.out.println(animation + i);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    animationsItem.get(slot).add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                    i++;
                } else {
                    moreImg = false;
                }
            }
            int resId = context.getResources().getIdentifier(cheminImages + "_icon", "drawable", context.getPackageName());
            if (resId != 0) {
                System.out.println("IOcconez trouvée" + this.getClass());
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                icone=(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
            }
            System.out.println(animationsItem.size() + "   " + animationsItem.get(slot).size());

            slot++;
        }
    }
    public void setGame(Game partie){
        this.partie = partie;
    }

    protected String[] getAnimations() {
        return new String[]{

        };
    }

    public boolean use(Game game){
        System.out.println("UTILISATION");
        used=true;
        return true;
    }
    public void update(){
        if(!animationOver()) currentUseTimer--;
        if(canUse()){
            use(this.partie);
        }
        System.out.println("anim : " + currentUseTimer);
    }

    public boolean animationOver(){return currentUseTimer <= 0;}
    private boolean canUse() {
        return currentUseTimer <= postUseAnimTime && !used;
    }

    public void startUsing(){
        if(used) {
            System.out.println("Debut anim : " + timeCentiToUse + " " + postUseAnimTime);
            this.currentUseTimer = timeCentiToUse+postUseAnimTime;
            used = false;
            currentCentiFrame = 0;
        }
    }

    public boolean getIsBeingUsed() {
        return this.currentUseTimer>0;
    }

    public ArrayList<ArrayList<Bitmap>> getAnimationsItem() {
        return animationsItem;
    }

    public boolean getIsStackable() {
        return isStackable;
    }
    public int getTailleStack(){
        return tailleStack;
    }

    public void drawIcone(Canvas canvas, double x, double y, double tailleX, double tailleY){
        canvas.drawBitmap(Bitmap.createScaledBitmap(icone, (int) tailleX, (int) tailleY, false), (float) x, (float) y, null);
    }
}
