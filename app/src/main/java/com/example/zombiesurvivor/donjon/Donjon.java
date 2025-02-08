package com.example.zombiesurvivor.donjon;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;

import java.util.ArrayList;

public class Donjon {
    private ArrayList<Movable> tiles = new ArrayList<>();
    private int longueurTile;
    private int hauteurTile;



    public Donjon(int longueurTile, int hauteurTile, String cheminDj, double posX, double posY){
        this.hauteurTile = hauteurTile;
        this.longueurTile = longueurTile;

        for(int i = 1; i <= longueurTile*hauteurTile; i++){
            tiles.add(new Movable(Game.getPartie().getContext(), posX+(i%longueurTile)*TAILLE_CASE,
                    (i/TAILLE_CASE)*posY, TAILLE_CASE,
                    TAILLE_CASE, cheminDj+i, true, 100,
                    0, 0,0,0
                    ));
            tiles.get(i-1).addTag(Tag.DONJON);
        }
        tiles.set(0, new Movable(Game.getPartie().getContext(), posX*TAILLE_CASE, posY*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, "example_2tile",
                true, 100, 50, 0, 0, 0,
                TAILLE_CASE, 0, 0, 0));
        tiles.get(0).addTag(Tag.SOLIDE);

    }


    public int getLongueurTile() {
        return longueurTile;
    }

    public int getHauteurTile() {
        return hauteurTile;
    }

    public Movable getTile(int nb) {
        if(nb >= tiles.size()) return null;
        return tiles.get(nb);
    }
}
