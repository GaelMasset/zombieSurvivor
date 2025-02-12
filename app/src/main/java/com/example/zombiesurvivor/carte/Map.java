package com.example.zombiesurvivor.carte;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.HAUTEUR_CARTE;
import static com.example.zombiesurvivor.carte.GenerateurNiveau.LARGEUR_CARTE;
import static com.example.zombiesurvivor.carte.GenerateurNiveau.LAYER_PLAYER;
import static com.example.zombiesurvivor.carte.GenerateurNiveau.NB_LAYER;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private ArrayList<ArrayList<Item>> items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Movable>>> objetsCarte;
    private Context context;

    public Map(ArrayList<ArrayList<ArrayList<Movable>>> carte){
        this.objetsCarte = carte;
        for(int i = 0; i < LARGEUR_CARTE; i++){
            items.add(new ArrayList<>());
            for(int j = 0; j < HAUTEUR_CARTE; j++){
                items.get(i).add(null);
            }
        }
    }


    public void drawMinMap(Canvas canvas, int left, int top, int right, int bottom) {
        if (objetsCarte == null || objetsCarte.isEmpty() || objetsCarte.get(0).isEmpty()) {
            return;
        }

        int longueur = objetsCarte.size();
        int hauteur = objetsCarte.get(0).size();
        float longueurCase = (float) (((double) (right - left))/longueur);
        float hauteurCase = (float) (((double) (bottom - top))/hauteur);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < objetsCarte.size(); i ++) {
            for (int j = 0; j < objetsCarte.get(i).size(); j ++) {
                ArrayList<Tag> tags = getAllTagsTile(i, j);
                if(tags.contains(Tag.EAU)){
                    paint.setColor(Color.parseColor("#adaee7"));
                } else if(tags.contains(Tag.ARBRE)){
                    paint.setColor(Color.parseColor("#7a2e0d"));
                } else if(tags.contains(Tag.DONJON)){
                    paint.setColor(Color.parseColor("#2fa3b5"));
                }else if(tags.contains(Tag.DECORATION)){
                    paint.setColor(Color.parseColor("#568c54"));
                }else if(tags.contains(Tag.SOL)){
                    paint.setColor(Color.parseColor("#9ddc91"));
                } else if(tags.contains(Tag.SOLIDE)){
                    paint.setColor(Color.parseColor("#692916"));
                }

                float caseLeft =  (left + i * longueurCase);
                float caseTop =  (top + j * hauteurCase);
                float caseRight =  (caseLeft + longueurCase);
                float caseBottom =  (caseTop + hauteurCase);

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
        for(ArrayList<Movable> liste: this.getMovablesAround(Game.getPartie().getJoueur(), 8, null)){
            for(Movable m: liste){
                m.update();
            }
        }
        for(Item i: getItemsAround(Game.getPartie().getJoueur(), 8)){
            i.update();
        }
    }
    public void draw(Canvas canvas) {
        Player j = Game.getPartie().getJoueur();
        int n = 0;
        for(ArrayList<Movable> liste: this.getMovablesAround(Game.getPartie().getJoueur(), 8, null)){
            if(n == LAYER_PLAYER){
                ArrayList<Movable> movablesDessusJoueur = new ArrayList<>();
                for(Movable m: liste) {
                    if (m.getPosY() + m.enfoncementTop - m.getDepassementTop() >= j.getPosY() + j.getTailleY()) {
                        movablesDessusJoueur.add(m);
                    } else {
                        m.draw(canvas);
                    }

                }
                j.draw(canvas);
                for(Movable m2: movablesDessusJoueur){
                    m2.draw(canvas);
                }
            } else{
                for(Movable m: liste) {
                    m.draw(canvas);
                }
            }

            n++;
        }

        for(Item i: getItemsAround(Game.getPartie().getJoueur(), 8)){
            i.drawIcone(canvas, i.getPosX(), i.getPosY(), i.getTailleX(), i.getTailleY());
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<ArrayList<Movable>> getMovablesAround(Movable m, int rayon, Tag t){
        ArrayList<ArrayList<Movable>> reducedMap = new ArrayList<>();
        for(int i =0; i < NB_LAYER; i++) {
            reducedMap.add(new ArrayList<>());
            for (int x = m.getPosXTile(true) - rayon; x < m.getPosXTile(true) + rayon; x++) {
                for (int y = m.getPosYTile(true) - rayon; y < m.getPosYTile(true) + rayon; y++) {
                    if (x < 0 || x >= this.objetsCarte.size()) continue;
                    if (y < 0 || y >= objetsCarte.get(x).size()) continue;
                    Movable movable = objetsCarte.get(x).get(y).get(i);
                    if ((movable != null && (t == null || movable.hasTag(t)))) {
                        reducedMap.get(i).add(movable);
                    }
                }
            }
        }
        return reducedMap;
    }
    public ArrayList<Movable> getMovablesListAround(Movable m, int rayon, Tag t) {
        ArrayList<Movable> liste = new ArrayList<>();
        // On récupère la position du Movable une seule fois
        int posX = m.getPosXTile(true);
        int posY = m.getPosYTile(true);

        // Parcours de la zone autour de m avec un rayon donné
        for (int x = posX - rayon; x < posX + rayon; x++) {
            if (x < 0 || x >= this.objetsCarte.size()) continue;  // Vérifie si x est dans la carte
            for (int y = posY - rayon; y < posY + rayon; y++) {
                if (y < 0 || y >= objetsCarte.get(x).size()) continue;  // Vérifie si y est dans les limites

                // Accès à l'objet une seule fois
                List<Movable> layerList = objetsCarte.get(x).get(y);

                // Vérifie si le layer existe avant d'accéder à chaque couche
                if (layerList != null) {
                    for (int i = 0; i < NB_LAYER; i++) {
                        Movable movable = layerList.get(i);
                        // Si le Movable est valide et a la bonne étiquette (si définie)
                        if (movable != null && (t == null || movable.hasTag(t))) {
                            liste.add(movable);
                        }
                    }
                }
            }
        }
        return liste;
    }

    public ArrayList<Item> getItemsAround(Movable m, int rayon){
        ArrayList<Item> item = new ArrayList<>();
        for(int x = m.getPosXTile(true)-rayon; x < m.getPosXTile(true) + rayon; x++){
            for(int y = m.getPosYTile(true)-rayon; y < m.getPosYTile(true) + rayon; y++){
                if(x < 0 || x >= this.items.size()) continue;
                if(y < 0 || y >= items.get(x).size()) continue;

                if(items.get(x).get(y) != null) {
                        item.add(items.get(x).get(y));
                }
            }
        }
        return item;
    }

    public ArrayList<ArrayList<ArrayList<Movable>>> getObjetsCarte() {
        return objetsCarte;
    }
    public void setObjetsCarte(int x, int y, int z, Movable mov){
        this.objetsCarte.get(x).get(y).set(z, mov);
    }

    public ArrayList<Movable> getFloors() {
        ArrayList<Movable> mov = new ArrayList<>();
        for(int x = 0; x < this.objetsCarte.size(); x++){
            for(int y = 0; y < this.objetsCarte.get(x).size(); y++){
                if(objetsCarte.get(x).get(y) != null && objetsCarte.get(x).get(y).get(0).hasTag(Tag.SOL)) mov.add(objetsCarte.get(x).get(y).get(0));
            }
        }
        return mov;
    }
    public ArrayList<Movable> getObstacles() {
        ArrayList<Movable> mov = new ArrayList<>();
        for(int x = 0; x < this.objetsCarte.size(); x++){
            for(int y = 0; y < this.objetsCarte.get(x).size(); y++){
                for(Movable m: objetsCarte.get(x).get(y)) {
                    if (objetsCarte.get(x).get(y) != null && m.hasTag(Tag.SOLIDE))
                        mov.add(m);
                }
            }
        }
        return mov;
    }

    public Item getItems(int x, int y) {
        return items.get(x).get(y);
    }

    public void setItems(Item i, int x, int y) {
        this.items.get(x).set(x, i);
    }

    public ArrayList<Tag> getAllTagsTile(int x, int y){
        ArrayList<Tag> tags = new ArrayList<>();
        for(Movable m: objetsCarte.get(x).get(y)){
            if(m != null) tags.addAll(m.getTags());
        }
        return tags;
    }
}
