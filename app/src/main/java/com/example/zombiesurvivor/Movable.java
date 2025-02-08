package com.example.zombiesurvivor;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.LruCache;

import java.util.ArrayList;

public class Movable implements Cloneable {

    final Context context;
    protected double posX;
    protected double posY;
    protected int tailleX;
    protected int tailleY;
    public double enfoncementTop;
    public double enfoncementBottom;
    public double enfoncementLeft;
    public double enfoncementRight;
    protected double depassementTop;
    protected double depassementBottom;
    protected double depasssementLeft;
    protected double depassementRight;
    protected String cheminImages;
    protected boolean isAnimating;
    protected int timeCentiBetweenFrame;
    protected ArrayList<Tag> tags = new ArrayList<>();
    protected int currentCentiFrame;
    protected ArrayList<Bitmap> images = new ArrayList<Bitmap>();

    // Cache pour les bitmaps
    private static LruCache<String, Bitmap> imageCache;

    static {
        // Taille du cache en fonction de la mémoire disponible
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8; // 1/8 de la mémoire disponible
        imageCache = new LruCache<>(cacheSize);
    }

    /*
    Constructor
     */
    public Movable(Context context, double posX, double posY, int tailleX, int tailleY,
                   String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,
                   double enfoncementTop, double enfoncementBottom,
                   double enfoncementLeft, double enfoncementRight) {
        this.context = context;
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.cheminImages = cheminImages;
        this.isAnimating = isAnimating;
        this.timeCentiBetweenFrame = timeCentiBetweenFrame;
        this.enfoncementTop = enfoncementTop;
        this.enfoncementBottom = enfoncementBottom;
        this.enfoncementLeft = enfoncementLeft;
        this.enfoncementRight = enfoncementRight;

        // Chargement et mise en cache des images
        int i = 1;
        boolean moreImg = true;
        while (moreImg) {
            int resId = context.getResources().getIdentifier(cheminImages + i, "drawable", context.getPackageName());
            if (resId != 0) {
                String imageName = cheminImages + i; // Nom unique pour chaque image
                Bitmap bitmap = loadBitmap(imageName, resId, tailleX, tailleY);
                images.add(bitmap);
                i++;
            } else {
                moreImg = false;
            }
        }
    }
    public Movable(Context context, double posX, double posY, int tailleX, int tailleY,
                   String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,
                   double enfoncementTop, double enfoncementBottom,
                   double enfoncementLeft, double enfoncementRight,
                   double depasssementTop, double depassementBottom,
                   double depassementLeft, double depassementRight) {
        this.context = context;
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.cheminImages = cheminImages;
        this.isAnimating = isAnimating;
        this.timeCentiBetweenFrame = timeCentiBetweenFrame;
        this.enfoncementTop = enfoncementTop;
        this.enfoncementBottom = enfoncementBottom;
        this.enfoncementLeft = enfoncementLeft;
        this.enfoncementRight = enfoncementRight;
        this.depassementBottom = depassementBottom;
        this.depassementRight = depassementRight;
        this.depassementTop = depasssementTop;
        this.depasssementLeft = depassementLeft;

        // Chargement et mise en cache des images
        int i = 1;
        boolean moreImg = true;
        while (moreImg) {
            int resId = context.getResources().getIdentifier(cheminImages + i, "drawable", context.getPackageName());
            if (resId != 0) {
                String imageName = cheminImages + i; // Nom unique pour chaque image
                Bitmap bitmap = loadBitmap(imageName, resId, (int)(tailleX+depasssementLeft+depassementRight), (int) (tailleY+depassementBottom+depasssementTop));
                images.add(bitmap);
                i++;
            } else {
                moreImg = false;
            }
        }
    }


    /*
    Cache methods
     */
    private Bitmap getBitmapFromCache(String imageName) {
        return imageCache.get(imageName);
    }

    private void addBitmapToCache(String imageName, Bitmap bitmap) {
        if (getBitmapFromCache(imageName) == null) {
            imageCache.put(imageName, bitmap);
        }
    }

    private Bitmap loadBitmap(String imageName, int resId, int width, int height) {
        Bitmap bitmap = getBitmapFromCache(imageName);
        if (bitmap == null) {
            // Décodage et mise en cache si l'image n'est pas déjà dans le cache
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            addBitmapToCache(imageName, bitmap);
        }
        return bitmap;
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
    public int getPosXTile(boolean center) {
        if(center){return (int) (posX+((double) tailleX /2))/TAILLE_CASE;}
        return (int) posX/TAILLE_CASE;
    }
    public int getPosYTile(boolean center) {
        if(center){return (int) (posY+((double) tailleY /2))/TAILLE_CASE;}
        return (int) posY/TAILLE_CASE;
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
    public void draw(Canvas canvas) {
        canvas.drawBitmap(images.get(currentCentiFrame / timeCentiBetweenFrame), (float) ((float) posX-depasssementLeft),
                (float) ((float) posY-depassementTop), null);
    }

    public void update() {
        if (isAnimating) {
            currentCentiFrame++;
            currentCentiFrame = currentCentiFrame % (images.size() * timeCentiBetweenFrame);
        } else {
            currentCentiFrame = 0;
        }
    }

    /*
    Static methods
     */
    public static boolean areTouching(Movable m1, Movable m2, boolean usingEnfoncement) {
        double m1Left, m1Right, m1Top, m1Bottom;
        double m2Left, m2Right, m2Top, m2Bottom;

        if (usingEnfoncement) {
            m1Left = m1.getPosX() + m1.enfoncementLeft;
            m1Right = m1.getPosX() + m1.getTailleX() - m1.enfoncementRight;
            m1Top = m1.getPosY() + m1.enfoncementTop;
            m1Bottom = m1.getPosY() + m1.getTailleY() - m1.enfoncementBottom;

            m2Left = m2.getPosX() + m2.enfoncementLeft;
            m2Right = m2.getPosX() + m2.getTailleX() - m2.enfoncementRight;
            m2Top = m2.getPosY() + m2.enfoncementTop;
            m2Bottom = m2.getPosY() + m2.getTailleY() - m2.enfoncementBottom;
        } else {
            m1Left = m1.getPosX();
            m1Right = m1.getPosX() + m1.getTailleX();
            m1Top = m1.getPosY();
            m1Bottom = m1.getPosY() + m1.getTailleY();

            m2Left = m2.getPosX();
            m2Right = m2.getPosX() + m2.getTailleX();
            m2Top = m2.getPosY();
            m2Bottom = m2.getPosY() + m2.getTailleY();
        }

        return !(m1Right <= m2Left ||
                m1Left >= m2Right ||
                m1Bottom <= m2Top ||
                m1Top >= m2Bottom);
    }

    public static Movable isOneTouching(Movable m1, ArrayList<? extends Movable> m2) {
        for (Movable m : m2) {
            if (areTouching(m1, m, true)) return m;
        }
        return null;
    }

    public static ArrayList<Movable> getAllGonnaTouchMovables(Movable m1, ArrayList<? extends Movable> m2, double x, double y) {
        ArrayList<Movable> touchingMovables = new ArrayList<>();
        Movable nextMov = m1.clone();
        nextMov.posX += x;
        nextMov.posY += y;

        for (Movable m : m2) {
            if (areTouching(nextMov, m, true)) {
                touchingMovables.add(m);
            }
        }
        return touchingMovables;
    }

    public Bitmap getBitmap(int i) {
        return this.images.get(i);
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }
    public ArrayList<Tag> getTags(){
        return tags;
    }
    public boolean hasTag(Tag t){
        return tags.contains(t);
    }

    public void removeTag(Tag tag) {
        if(this.hasTag(tag)) tags.remove(tag);
    }
}
