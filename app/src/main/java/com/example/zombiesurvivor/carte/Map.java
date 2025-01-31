package com.example.zombiesurvivor.carte;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.mobs.Mob;

import java.util.ArrayList;

public class Map {
    private TypeTile[][] typeTiles;
    private Movable[][] objetsCarte;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Floor> sols;
    private Context context;

    public Map(ArrayList<Obstacle> obstacles, ArrayList<Floor> floors){
        this.obstacles = obstacles;
        this.sols = floors;
    }


    public void drawMinMap(Canvas canvas, int left, int top, int right, int bottom) {
        if (typeTiles == null || typeTiles.length == 0 || typeTiles[0].length == 0) {
            return;
        }

        int longueur = typeTiles.length;
        int hauteur = typeTiles[0].length;

        float longueurCase = (float) (((double) (right - left))/longueur);
        float hauteurCase = (float) (((double) (bottom - top))/hauteur);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < hauteur; i ++) {
            for (int j = 0; j < longueur; j ++) {
                TypeTile tile = typeTiles[j][i];

                switch (tile) {
                    case SOL:
                        paint.setColor(Color.parseColor("#9ddc91")); // Vert clair pour le sol
                        break;
                    case VIDE:
                        paint.setColor(Color.parseColor("#adaee7")); // Bleu clair pour le vide
                        break;
                    case BORDURE:
                        paint.setColor(Color.parseColor("#8d6941")); // Marron pour les bordures
                        break;
                    default:
                        paint.setColor(Color.parseColor("#FFFFFF")); // Blanc par défaut
                        break;
                }

                float caseLeft = (float) (left + j * longueurCase);
                float caseTop = (float) (top + i * hauteurCase);
                float caseRight = (float) (caseLeft + longueurCase);
                float caseBottom = (float) (caseTop + hauteurCase);

                canvas.drawRect(caseLeft, caseTop, caseRight, caseBottom, paint);
            }
        }

        double joueurPosX = Game.getPartie().getJoueur().getPosX() / GenerateurNiveau.TAILLE_CASE;
        double joueurPosY = Game.getPartie().getJoueur().getPosY() / GenerateurNiveau.TAILLE_CASE;

        if (joueurPosX >= 0 && joueurPosX < longueur && joueurPosY >= 0 && joueurPosY < hauteur) {
            paint.setColor(Color.RED);

            float joueurLeft = (float) (left + joueurPosX * longueurCase - 5);
            float joueurTop = (float) (top + joueurPosY * hauteurCase - 5);
            float joueurRight = (float) (joueurLeft + 10) ;
            float joueurBottom = (float) (joueurTop + 10);

            canvas.drawRect(joueurLeft, joueurTop, joueurRight, joueurBottom, paint);
        }

        Paint mobpaint = new Paint();
        mobpaint.setColor(Color.GRAY);
        for(Movable m : Game.getPartie().getMobs()){
            if(m.getPosXTile() >= 0 && m.getPosXTile() < longueur && m.getPosYTile() >= 0 && m.getPosYTile() < hauteur){
                float joueurLeft = (float) (left + m.getPosXTile() * longueurCase - 5);
                float joueurTop = (float) (top + m.getPosYTile() * hauteurCase - 5);
                float joueurRight = (float) (joueurLeft + 10) ;
                float joueurBottom = (float) (joueurTop + 10);
                canvas.drawRect(joueurLeft, joueurTop, joueurRight, joueurBottom, mobpaint);
            }
        }
    }




    public void update() {
        for (int i = 0; i < sols.size(); i++) {
            sols.get(i).update();
        }
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).update();
        }
    }
    public void draw(Canvas canvas) {
        for (int i = 0; i < sols.size(); i++) {
            sols.get(i).draw(canvas);
        }
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).draw(canvas);
        }
    }


    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }
    public ArrayList<Floor> getFloors() {
        return this.sols;
    }

    public TypeTile[][] getMap() {
        return typeTiles;
    }

    public void setMap(TypeTile[][] map) {
        this.typeTiles = map;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
