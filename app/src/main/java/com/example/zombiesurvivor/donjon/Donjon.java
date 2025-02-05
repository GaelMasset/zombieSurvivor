package com.example.zombiesurvivor.donjon;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Obstacle;

public class Donjon {
    private Movable[][] tiles;
    private int longueurTile;
    private int hauteurTile;

    public Donjon(int longueurTile, int hauteurTile, String cheminDj, double posX, double posY){
        this.hauteurTile = hauteurTile;
        this.longueurTile = longueurTile;

        tiles = new Movable[hauteurTile][longueurTile];

        for(int i = 0; i < longueurTile*hauteurTile; i++){
            tiles[i/hauteurTile][i%longueurTile] = new Obstacle(Game.getPartie().getContext(), posX+(i%longueurTile)*TAILLE_CASE,
                    (i/TAILLE_CASE)*posY, TAILLE_CASE,
                    TAILLE_CASE, 0, 0, 0, 0, cheminDj+"_"+i, true,
                    100,
                    9999, 9999);
        }
    }
}
