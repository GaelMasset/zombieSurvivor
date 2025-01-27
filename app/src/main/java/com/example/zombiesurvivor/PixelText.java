package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.zombiesurvivor.Base.MainActivity;

public class PixelText {
    private Context context;
    private Bitmap[] characterBitmaps;
    private String text;
    private boolean center;
    private Character[] liste = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    private Character[] listeMaj = {'L'};

    private double coX, coY, taille;
    private double spacing;

    public PixelText(Context context, String texte, double coX, double coY, double taille, double letterSpacing, boolean center) {
        this.context = context;
        this.coX = coX;
        this.coY = coY;
        this.taille = taille;
        this.text = texte;
        this.spacing = letterSpacing;
        this.center = center;
        loadCharacterBitmaps();
    }

    private void loadCharacterBitmaps() {
        characterBitmaps = new Bitmap[256]; // Pour chaque caractère ASCII

        Bitmap base = BitmapFactory.decodeResource(context.getResources(), R.drawable.char_1);
        int baseWidth = base.getWidth();
        int baseHeight = base.getHeight();
        int newHeight = (int) taille;
        int newWidth = (int) (baseWidth * ((double) newHeight/baseHeight));

        System.out.println("new height : " + newHeight);
        System.out.println("new width : " + newWidth);

        //Chiffres et lettres minuscules
        for(Character c: liste){
            int resId = context.getResources().getIdentifier("char_"+c, "drawable", context.getPackageName());
            Bitmap m = BitmapFactory.decodeResource(context.getResources(), resId);
            newHeight = (int) taille;
            newWidth = (int) (m.getWidth() * ((double) newHeight/baseHeight));
            characterBitmaps[c] = Bitmap.createScaledBitmap(m, newWidth, newHeight, false);
        }
        //Lettres majuscules
        for(Character c: listeMaj){
            int resId = context.getResources().getIdentifier("char_"+Character.toLowerCase(c)+Character.toLowerCase(c), "drawable", context.getPackageName());
            Bitmap m = BitmapFactory.decodeResource(context.getResources(), resId);
            newHeight = (int) taille;
            newWidth = (int) (m.getWidth() * ((double) newHeight/baseHeight));
            characterBitmaps[c] = Bitmap.createScaledBitmap(m, newWidth, newHeight, false);
        }


    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        int x = (int) coX;
        int y = (int) coY;
        double characterSpacing = this.spacing;

        if (!center) {
            // Dessin normal, sans centrage
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                Bitmap charBitmap = characterBitmaps[currentChar];
                if (charBitmap != null) {
                    canvas.drawBitmap(charBitmap, x, y, paint);
                    x += (int) (charBitmap.getWidth() + characterSpacing); // Avance après chaque lettre
                }
            }
        } else {
            // Centrer le texte
            int totalWidth = 0;

            // Calculer la largeur totale du texte
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                Bitmap charBitmap = characterBitmaps[currentChar];
                if (charBitmap != null) {
                    totalWidth += charBitmap.getWidth();
                    if (i < text.length() - 1) {
                        totalWidth += (int) characterSpacing; // Ajouter l'espacement entre les caractères
                    }
                }
            }

            // Calculer le point de départ pour centrer
            x -= totalWidth / 2;

            // Dessiner le texte centré
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                Bitmap charBitmap = characterBitmaps[currentChar];
                if (charBitmap != null) {
                    canvas.drawBitmap(charBitmap, x, y, paint);
                    x += (int) (charBitmap.getWidth() + characterSpacing); // Avance après chaque lettre
                }
            }
        }
    }

    public void setText(String text){
        this.text = text;
    }
}
