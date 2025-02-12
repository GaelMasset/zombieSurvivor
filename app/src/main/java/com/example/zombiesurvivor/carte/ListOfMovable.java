package com.example.zombiesurvivor.carte;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;

import java.util.ArrayList;
import java.util.Random;

public class ListOfMovable {

    /**
     *
     * All static trees and getTrees function
     *
     */
    private static Movable tree1 = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "tree1_",
            true, 100, (27.0/32)*(TAILLE_CASE*2),
            0, (10.0/32)*TAILLE_CASE*2, (11.0/32)*TAILLE_CASE*2,
            TAILLE_CASE,0,0, TAILLE_CASE);
    private static Movable tree2 = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "tree2_",
            true, 100, (28.0/32)*TAILLE_CASE*2, 0, (9.0/32)*TAILLE_CASE*2,
            (14.0/32)*TAILLE_CASE*2,
            TAILLE_CASE,0,0,TAILLE_CASE);
    private static Movable tree1shadow = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "tree1_shadow_",
            true, 100, 0, 0, 0, 0,
            TAILLE_CASE,0,0,TAILLE_CASE);
    private static Movable tree2shadow = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "tree2_shadow_",
            true, 100, 0, 0, 0, 0,
            TAILLE_CASE,0,0,TAILLE_CASE);
    public static void initialize(){
        tree1.addTag(Tag.ARBRE);
        tree1.addTag(Tag.SOLIDE);
        tree2.addTag(Tag.ARBRE);
        tree2.addTag(Tag.SOLIDE);
    }
    public static Movable getRandomTree(){
        Random r = new Random();
        return getAllTrees().get(r.nextInt(getAllTrees().size()));
    }
    public static Movable getTree(int index){
        return getAllTrees().get(index);
    }
    public static ArrayList<Movable> getAllTrees(){
        ArrayList<Movable> m = new ArrayList<>();
        m.add(tree1);
        m.add(tree2);
        return m;
    }
    public static Movable getTreeShadow(int index){
        return getAllTreeShadows().get(index);
    }

    private static ArrayList<Movable> getAllTreeShadows() {
        ArrayList<Movable> m = new ArrayList<>();
        m.add(tree1shadow);
        m.add(tree2shadow);
        return m;
    }

    /**
     *
     * All static Borders and their get functions
     *
     */
    public static Movable obsHautGauche = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_haut_gauche", true, 100,
            0, 0, 0, 0);
    public static Movable obsHaut = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_haut", true, 100,
            0, 0, 0, 0);
    public static Movable obsHautDroite = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_haut_droit", true, 100,
            0, 0, 0, 0);
    public static Movable obsDroite = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_droit", true, 100,
            0, 0, 0, 0);
    public static Movable obsBasGauche = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_bas_gauche", true, 100,
            0, 0, 0, 0);
    public static Movable obsBas = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_bas", true, 100,
            0, 0, 0, 0);
    public static Movable obsBasDroite = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_bas_droit", true, 100,
            0, 0, 0, 0);
    public static Movable obsGauche = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_gauche", true, 100,
            0, 0, 0, 0);
    public static Movable obsHautGaucheInterne = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_interne_haut_gauche", true, 100,
            0, 0, 0, 0);
    public static Movable obsHautDroiteInterne = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_interne_haut_droit", true, 100,
            0, 0, 0, 0);
    public static Movable obsBasDroiteInterne = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_interne_bas_droit", true, 100,
            0, 0, 0, 0);
    public static Movable obsBasGaucheInterne = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_mur_coin_interne_bas_gauche", true, 100,
            0, 0, 0, 0);
    public static Movable base = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile", true, 100,
            0, 0, 0, 0);
    public static Movable vide = new Movable(0, 0, TAILLE_CASE, TAILLE_CASE, "example_tile_void", true, 100,
            0, 0, 0, 0);

    public static ArrayList<Movable> getAllBorders() {
        ArrayList<Movable> m = new ArrayList<>();
        m.add(obsHautGauche);
        m.add(obsHaut);
        m.add(obsHautDroite);
        m.add(obsDroite);
        m.add(obsBasGauche);
        m.add(obsBas);
        m.add(obsBasDroite);
        m.add(obsGauche);
        m.add(obsHautGaucheInterne);
        m.add(obsHautDroiteInterne);
        m.add(obsBasGaucheInterne);
        m.add(obsBasDroiteInterne);
        m.add(base);
        m.add(vide);
        return m;
    }

}
