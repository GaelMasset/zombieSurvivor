package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.mobs.Loup;
import com.example.zombiesurvivor.mobs.Mob;
import com.example.zombiesurvivor.mobs.Player;

import java.util.ArrayList;

public class Game {
    private Player joueur;
    private Map carte;
    private Camera camera;
    private ArrayList<Mob> zombies = new ArrayList<Mob>();
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
        for(Mob mobs: zombies){
            mobs.draw(canvas);
        }
    }

    public void update(){
        this.joueur.update();
        this.carte.update();
        this.camera.update();
        for(int i = 0; i < balles.size(); i++){
            balles.get(i).update();
        }
        for(Mob mobs: zombies){
            mobs.update();
        }
    }

    public void addMonster(){
        this.zombies.add(new Loup(context, 2500, 3000, 63, 66, "monster_wolf", 0, 0, 0, 0, true, 50, 50, 50, 0.2, this, 30, 100));
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
    public ArrayList<? extends Movable> getMobs() {
        return zombies;
    }
}
