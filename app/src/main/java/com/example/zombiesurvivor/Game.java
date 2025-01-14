package com.example.zombiesurvivor;

import static com.example.zombiesurvivor.Base.MainActivity.facteurZoom;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Game {
    private Player joueur;
    private Map carte;
    private Camera camera;
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<Bullet> balles = new ArrayList<Bullet>();
    private Context context;


    public Game(Context context, Player joueur, Map carte) {
        this.joueur = joueur;
        this.carte = carte;
        this.camera = new CameraPlayer(this);
        this.context = context;
        joueur.setGame(this);
    }

    public void draw(Canvas canvas){
        canvas.translate(-camera.getPosX(), camera.getPosY());
        this.carte.draw(canvas);
        this.joueur.draw(canvas);

        for(Bullet balle: balles){
            balle.draw(canvas);
        }
    }

    public void update(){
        this.joueur.update();
        this.carte.update();
        this.camera.update();
        for(int i = 0; i < balles.size(); i++){
            balles.get(i).update();
        }

    }

    public Player getJoueur(){
        return this.joueur;
    }

    public Map getCarte() {
        return this.carte;
    }
    public ArrayList<Bullet> getBalles() {
        return this.balles;
    }
    public Context getContext(){
        return this.context;
    }
}
