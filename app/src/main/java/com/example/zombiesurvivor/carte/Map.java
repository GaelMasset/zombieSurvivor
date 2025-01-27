package com.example.zombiesurvivor.carte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Door;
import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.R;

import java.util.ArrayList;

public class Map {
    private TypeTile[][] map;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Floor> sols;
    private Context context;

    public Map(ArrayList<Obstacle> obstacles, ArrayList<Floor> floors){
        this.obstacles = obstacles;
        this.sols = floors;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < sols.size(); i++) {
            sols.get(i).draw(canvas);
        }
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).draw(canvas);
        }
    }
    public void drawMinMap(Canvas canvas, int left, int top, int right, int bottom) {
        if (map == null || map.length == 0 || map[0].length == 0) {
            return; // Vérifie que la carte n'est pas vide ou nulle
        }
        Image hud = new Image(context, left, top, right - left, bottom - top, "map_hud", 80);

        left = (int) (left + hud.getTailleX()*(1f/6));
        right = (int) (right - hud.getTailleX()*(1f/6));
        top = (int) (top + hud.getTailleY()*(9f/60));
        bottom = (int) (bottom - hud.getTailleY()*(9f/60));

        int longueur = map.length;  // Nombre de colonnes
        int hauteur = map[0].length; // Nombre de lignes

        // Calcul des dimensions de chaque case
        int longueurCase = (right - left) / longueur;
        int hauteurCase = (bottom - top) / hauteur;

        // Créer un objet Paint réutilisable
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL); // Remplissage des cases

        // Parcourir la carte
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < longueur; j++) {
                TypeTile tile = map[j][i]; // Accéder à la tuile actuelle

                // Définir une couleur différente selon le type de tuile
                switch (tile) {
                    case SOL:
                        paint.setColor(Color.parseColor("#9ddc91"));
                        break;
                    case VIDE:
                        paint.setColor(Color.parseColor("#adaee7"));
                        break;
                    case BORDURE:
                        paint.setColor(Color.parseColor("#8d6941"));
                        break;
                    default:
                        paint.setColor(Color.parseColor("#FFFFFF"));
                        break;
                }

                // Calculer les coordonnées de la case
                int caseLeft = left + j * longueurCase;
                int caseTop = top + i * hauteurCase;
                int caseRight = caseLeft + longueurCase;
                int caseBottom = caseTop + hauteurCase;

                // Dessiner le rectangle
                canvas.drawRect(caseLeft, caseTop, caseRight, caseBottom, paint);
            }
        }
        hud.draw(canvas);
    }


    public void update() {
        for (int i = 0; i < sols.size(); i++) {
            sols.get(i).update();
        }
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).update();
        }
    }


    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }
    public ArrayList<Floor> getFloors() {
        return this.sols;
    }

    public TypeTile[][] getMap() {
        return map;
    }

    public void setMap(TypeTile[][] map) {
        this.map = map;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
