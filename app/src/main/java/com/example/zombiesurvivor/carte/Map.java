package com.example.zombiesurvivor.carte;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.Obstacle;

import java.util.ArrayList;

public class Map {
    private TypeTile[][] map;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Floor> sols;
    private Context context;
    private Game partie;

    public Map(ArrayList<Obstacle> obstacles, ArrayList<Floor> floors){
        this.obstacles = obstacles;
        this.sols = floors;
    }


    public void drawMinMap(Canvas canvas, int left, int top, int right, int bottom) {
        if (map == null || map.length == 0 || map[0].length == 0) {
            return; // Vérifie que la carte n'est pas vide ou nulle
        }

        // Création et dessin du HUD
        Image hud = new Image(context, left, top, right - left, bottom - top, "map_hud", 80);
        // Appliquer une marge de 30 pixels pour ajuster les dimensions de la minimap
        System.out.println((right-left)/map.length);
        left += 30;
        right -= 30;
        top += 30;
        bottom -= 30;

        // Dimensions de la carte
        int longueur = map.length;  // Nombre de colonnes
        int hauteur = map[0].length; // Nombre de lignes

        // Calcul des dimensions de chaque case
        float longueurCase = (float) (((double) right - left) / longueur);
        float hauteurCase = (float) (((double) bottom - top) / hauteur);

        // Créer un objet Paint réutilisable
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL); // Remplissage des cases

        // Parcourir la carte et dessiner les cases
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < longueur; j++) {
                TypeTile tile = map[j][i]; // Accéder à la tuile actuelle

                // Définir une couleur différente selon le type de tuile
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

                // Calculer les coordonnées de la case
                float caseLeft = left + j * longueurCase;
                float caseTop = top + i * hauteurCase;
                float caseRight = caseLeft + longueurCase;
                float caseBottom = caseTop + hauteurCase;

                // Dessiner la case
                canvas.drawRect(caseLeft, caseTop, caseRight, caseBottom, paint);
            }
        }

        // Dessiner la position du joueur sur la minimap
        double joueurPosX = partie.getJoueur().getPosX() / GenerateurNiveau.TAILLE_CASE;
        double joueurPosY = partie.getJoueur().getPosY() / GenerateurNiveau.TAILLE_CASE;

        if (joueurPosX >= 0 && joueurPosX < longueur && joueurPosY >= 0 && joueurPosY < hauteur) {
            paint.setColor(Color.RED); // Rouge pour le joueur
            float joueurLeft = (float) (left + joueurPosX * longueurCase - 5);
            float joueurTop = (float) (top + joueurPosY * hauteurCase - 5);
            float joueurRight = joueurLeft + 10;
            float joueurBottom = joueurTop + 10;

            // Dessiner un rectangle représentant le joueur
            canvas.drawRect(joueurLeft, joueurTop, joueurRight, joueurBottom, paint);
        }
        hud.draw(canvas); // Dessiner le HUD en premier
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
        return map;
    }

    public void setMap(TypeTile[][] map) {
        this.map = map;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setGame(Game partie){
        this.partie = partie;
    }
}
