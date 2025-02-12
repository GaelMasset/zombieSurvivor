package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.PixelText;
import com.example.zombiesurvivor.Tag;
import com.example.zombiesurvivor.carte.ListOfMovable;
import com.example.zombiesurvivor.compte.Compte;

public class FenetreAccueil extends Fenetre {
    private Movable fond;
    private Bouton boutonStartGame;
    private Bouton ballon;
    private Image hudJoueur;
    private Bouton boutonXp;
    private Bouton boutonPiece;
    private Bouton boutonGemme;
    private Bouton boutonInfo;
    private Bouton boutonPlay;
    private Bouton boutonOption;
    private PixelText nbPiece;
    private PixelText nbGemmes;


    public FenetreAccueil(Page p) {
        super(p);

        //Initailise game
        Game.getPartie();
        Game.setContext(p.getContext());
        ListOfMovable.initialize();

        fond = new Movable(0,0,canvasWidth,canvasHeight,"background_accueil", true, 100, 0, 0, 0, 0);
        boutonStartGame = new Bouton(pourcentLongueur(94.0/3) ,  0, (int) pourcentLongueur(103.0/3), canvasHeight, "background_accueil_ile","background_accueil_ile", true, 80);
        ballon = new Bouton(pourcentLongueur(198/3) ,0 ,pourcentLongueur(93/3), canvasHeight, "background_accueil_ile_ballon","background_accueil_ile_ballon", true, 80);

        hudJoueur = new Image(p.getContext(), pourcentLongueur(18/3),
                pourcentHauteur(21/1.5), pourcentLongueur(140/3), (int) pourcentHauteur(107/1.5), "accueil_hud_perso", 10);

        boutonXp = new Bouton(pourcentLongueur(22.0/3), pourcentHauteur(27.0/1.5), (int) pourcentLongueur(55.0/3), (int) pourcentHauteur(17/1.5), "accueil_hud_boutonxp","accueil_hud_boutonxp_clicked", true, 80);
        boutonPiece = new Bouton(pourcentLongueur(22.0/3), pourcentHauteur(48.0/1.5), (int) pourcentLongueur(55.0/3), (int) pourcentHauteur(17/1.5), "accueil_hud_boutonpiece","accueil_hud_boutonpiece_clicked", true, 80);
        boutonGemme = new Bouton(pourcentLongueur(22.0/3), pourcentHauteur(69.0/1.5), (int) pourcentLongueur(55.0/3), (int) pourcentHauteur(17/1.5), "accueil_hud_boutongemme","accueil_hud_boutongemme_clicked", true, 80);
        boutonInfo = new Bouton(pourcentLongueur(22.0/3), pourcentHauteur(105/1.5), (int) pourcentLongueur(18.0/3), (int) pourcentHauteur(18/1.5), "accueil_hud_boutoninfo","accueil_hud_boutoninfo_clicked", true, 80);
        boutonPlay = new Bouton(pourcentLongueur(59.0/3), pourcentHauteur(105/1.5), (int) pourcentLongueur(18.0/3), (int) pourcentHauteur(18/1.5), "accueil_hud_boutonplay","accueil_hud_boutonplay_clicked", true, 80);
        boutonOption = new Bouton(pourcentLongueur(3.0/3), pourcentHauteur(3.0/1.5), (int) pourcentLongueur(17.0/3), (int) pourcentHauteur(17/1.5), "button_option","button_option_clicked", true, 80);

        nbPiece = new PixelText(p.getContext(), ""+Compte.getCompte().getPieces(), pourcentLongueur(44.5/3), pourcentHauteur(54/1.5), pourcentHauteur(7/1.5),pourcentLongueur(0.18) ,true);
        nbGemmes = new PixelText(p.getContext(), ""+Compte.getCompte().getGemmes(), pourcentLongueur(44.5/3), pourcentHauteur(75/1.5), pourcentHauteur(7/1.5),pourcentLongueur(0.18) ,true);


    }

    @Override
    public void update() {
        fond.update();
        boutonStartGame.update();
        ballon.update();
        hudJoueur.update();
    }

    @Override
    public void draw(Canvas canvas) {
        fond.draw(canvas);
        boutonStartGame.draw(canvas);
        ballon.draw(canvas);
        hudJoueur.draw(canvas);
        boutonXp.draw(canvas);
        boutonPiece.draw(canvas);
        boutonGemme.draw(canvas);
        boutonInfo.draw(canvas);
        boutonPlay.draw(canvas);
        nbPiece.draw(canvas);
        nbGemmes.draw(canvas);
        boutonOption.draw(canvas);
    }

    @Override
    public boolean onClick(MotionEvent event) {
        int motionaction = event.getActionMasked();

        int pointerCount = event.getPointerCount();
        double xClicked = event.getX() - offsetX;
        double yClicked = event.getY() - offsetY;

        for (int i = 0; i < pointerCount; i++) {
            switch (motionaction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    System.out.println("Click en bas");
                    if(ballon.isPressed(xClicked, yClicked)) goTo(new FenetreTest(page));
                    if(boutonXp.isPressed(xClicked, yClicked)) boutonXp.setIsPressed(true);
                    if(boutonPiece.isPressed(xClicked, yClicked)) boutonPiece.setIsPressed(true);
                    if(boutonGemme.isPressed(xClicked, yClicked)) boutonGemme.setIsPressed(true);
                    if(boutonInfo.isPressed(xClicked, yClicked)) boutonInfo.setIsPressed(true);
                    if(boutonOption.isPressed(xClicked, yClicked)) boutonOption.setIsPressed(true);
                    if(boutonPlay.isPressed(xClicked, yClicked)){
                        boutonPlay.setIsPressed(true);
                        goTo(new FenetreChoixNiveau(this.page));
                    }

                    System.out.println("Jeu en lol");
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    System.out.println("Click en move");
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    boutonXp.setIsPressed(false);
                    boutonPiece.setIsPressed(false);
                    boutonGemme.setIsPressed(false);
                    boutonInfo.setIsPressed(false);
                    boutonPlay.setIsPressed(false);
                    boutonOption.setIsPressed(false);
                    System.out.println("Click en haut");
                    return true;
                }
            }
        }
        return true;
    }
}
