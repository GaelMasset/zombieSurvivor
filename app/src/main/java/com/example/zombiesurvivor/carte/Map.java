package com.example.zombiesurvivor.carte;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.HAUTEUR_CARTE;
import static com.example.zombiesurvivor.carte.GenerateurNiveau.LARGEUR_CARTE;
import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Item;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;
import com.example.zombiesurvivor.mobs.Player;

import java.util.ArrayList;

public class Map {
    private Item[][] items;
    private TypeTile[][] typeTiles;
    private Movable[][] objetsCarte;
    private Context context;

    public Map(Movable[][] carte){
        this.objetsCarte = carte;
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
                if(objetsCarte[j][i] == null){
                    paint.setColor(Color.parseColor("#adaee7"));
                }else if(objetsCarte[j][i].getTags().contains(Tag.ARBRE)){
                    paint.setColor(Color.parseColor("#7a2e0d"));
                } else if(objetsCarte[j][i].getTags().contains(Tag.DONJON)){
                    paint.setColor(Color.parseColor("#2fa3b5"));
                }else if(objetsCarte[j][i].getTags().contains(Tag.DECORATION)){
                    paint.setColor(Color.parseColor("#568c54"));
                }else if(objetsCarte[j][i].getTags().contains(Tag.SOLIDE)){
                    paint.setColor(Color.parseColor("#8d6941"));
                }else if(objetsCarte[j][i].getTags().contains(Tag.SOL)){
                    paint.setColor(Color.parseColor("#9ddc91"));
                } else{
                    paint.setColor(Color.parseColor("#adaee7"));
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
            if(m.getPosXTile(true) >= 0 && m.getPosXTile(true) < longueur && m.getPosYTile(true) >= 0 && m.getPosYTile(true) < hauteur){
                float joueurLeft = (float) (left + m.getPosXTile(true) * longueurCase - 5);
                float joueurTop = (float) (top + m.getPosYTile(true) * hauteurCase - 5);
                float joueurRight = (float) (joueurLeft + 10) ;
                float joueurBottom = (float) (joueurTop + 10);
                canvas.drawRect(joueurLeft, joueurTop, joueurRight, joueurBottom, mobpaint);
            }
        }
    }




    public void update() {
        for(Movable m: this.getMovablesAround(Game.getPartie().getJoueur(), 8, null)){
            m.update();
        }
        for(Item i: getItemsAround(Game.getPartie().getJoueur(), 8)){
            i.update();
        }
    }
    public void draw(Canvas canvas) {
        Player j = Game.getPartie().getJoueur();
        ArrayList<Movable> movablesDessusJoueur = new ArrayList<>();
        for(Movable m: this.getMovablesAround(Game.getPartie().getJoueur(), 8, null)){
            if(m.getPosY()+ m.enfoncementTop >= j.getPosY()+j.getTailleY()){
                movablesDessusJoueur.add(m);
            } else{
                m.draw(canvas);
            }
        }
        j.draw(canvas);
        for(Movable m: movablesDessusJoueur){
            m.draw(canvas);
        }
        for(Item i: getItemsAround(Game.getPartie().getJoueur(), 8)){
            i.drawIcone(canvas, i.getPosX(), i.getPosY(), i.getTailleX(), i.getTailleY());
        }
    }

    public TypeTile[][] getMap() {
        return typeTiles;
    }

    public void setMap(TypeTile[][] map) {
        this.typeTiles = map;
        this.items = new Item[typeTiles.length][typeTiles[0].length];

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Movable> getMovablesAround(Movable m, int rayon, Tag t){
        ArrayList<Movable> mov = new ArrayList<Movable>();
        for(int x = m.getPosXTile(true)-rayon; x < m.getPosXTile(true) + rayon; x++){
            for(int y = m.getPosYTile(true)-rayon; y < m.getPosYTile(true) + rayon; y++){
                if(x < 0 || x >= this.objetsCarte.length) continue;
                if(y < 0 || y >= objetsCarte[x].length) continue;

                if(objetsCarte[x][y] != null) {
                    if (t == null || objetsCarte[x][y].hasTag(t)) {
                        mov.add(objetsCarte[x][y]);
                    }
                }
            }
        }
        return mov;
    }
    public ArrayList<Item> getItemsAround(Movable m, int rayon){
        ArrayList<Item> item = new ArrayList<>();
        for(int x = m.getPosXTile(true)-rayon; x < m.getPosXTile(true) + rayon; x++){
            for(int y = m.getPosYTile(true)-rayon; y < m.getPosYTile(true) + rayon; y++){
                if(x < 0 || x >= this.items.length) continue;
                if(y < 0 || y >= items[x].length) continue;

                if(items[x][y] != null) {
                        item.add(items[x][y]);
                }
            }
        }
        return item;
    }

    public Movable[][] getObjetsCarte() {
        return objetsCarte;
    }
    public void setObjetsCarte(int x, int y, Movable mov){
        this.objetsCarte[x][y] = mov;
    }

    public ArrayList<Movable> getFloors() {
        ArrayList<Movable> mov = new ArrayList<>();
        for(int x = 0; x < this.objetsCarte.length; x++){
            for(int y = 0; y < this.objetsCarte[x].length; y++){
                if(objetsCarte[x][y] != null && objetsCarte[x][y].hasTag(Tag.SOL)) mov.add(objetsCarte[x][y]);
            }
        }
        return mov;
    }
    public ArrayList<Movable> getObstacles() {
        ArrayList<Movable> mov = new ArrayList<>();
        for(int x = 0; x < this.objetsCarte.length; x++){
            for(int y = 0; y < this.objetsCarte[x].length; y++){
                if(objetsCarte[x][y] != null && objetsCarte[x][y].hasTag(Tag.SOLIDE)) mov.add(objetsCarte[x][y]);
            }
        }
        return mov;
    }

    public Item getItems(int x, int y) {
        return items[x][y];
    }

    public void setItems(Item i, int x, int y) {
        this.items[x][y] = i;
    }
}
