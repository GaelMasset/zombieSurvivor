package com.example.zombiesurvivor.Base;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.zombiesurvivor.R;

public class MainActivity extends AppCompatActivity {

    public static int screenHeight;
    public static int screenWidth;
    public static int canvasHeight;
    public static int canvasWidth;
    public static int offsetX;
    public static int offsetY;
    public static float targetHauteur = 1200;
    public static float facteurZoom = (canvasHeight/ targetHauteur);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Cacher la barre de navigation
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  // Active le mode plein écran

        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        // Calcul du ratio 2:1 pour la largeur et la hauteur
        int canvasWidth = screenWidth;
        int canvasHeight = screenWidth / 2;

        if (canvasHeight > screenHeight) {
            // Si la hauteur dépasse la hauteur de l'écran, ajuster la largeur
            canvasHeight = screenHeight;
            canvasWidth = screenHeight * 2;
        }
        MainActivity.canvasHeight = canvasHeight;
        MainActivity.canvasWidth = canvasWidth;



        // Calcul des marges pour centrer l'élément
        offsetX = (int) ((screenWidth - canvasWidth) * 0.25);
        offsetY = (int) ((screenHeight - canvasHeight) *0.25);

        System.out.println("Longueur écran : " + screenWidth);
        System.out.println("Longueur canvas " + canvasWidth);
        System.out.println("Hauteur écran : " + screenHeight);
        System.out.println("Hauteur canvas " + canvasHeight);
        System.out.println("Offset X " + offsetX);
        System.out.println("Offset Y : " + offsetY);

        setContentView(new Page(this));
    }

    public static double pourcentLongueur(double pourcent){
        return pourcent * canvasWidth/100;
    }
    public static double pourcentHauteur(double pourcent){
        return pourcent * (canvasHeight/100);
    }
    public static int pourcentLongueur(int pourcent){
        return (int) (pourcent * canvasWidth/100);
    }
    public static int pourcentHauteur(int pourcent){
        return (int) (pourcent * (canvasHeight/100));
    }
    public static Bitmap createMirroredBitmap(Bitmap originalBitmap) {
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f); // Inversion horizontale

        Bitmap mirroredBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

        return mirroredBitmap;
    }
}