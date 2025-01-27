package com.example.zombiesurvivor.map;


import static com.example.zombiesurvivor.Base.MainActivity.offsetX;
import static com.example.zombiesurvivor.Base.MainActivity.offsetY;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.PixelText;

public class IleDescriptor {
    private PixelText titre;
    private Bouton ile;
    private Bouton playButton;
    private Bouton infoButton;
    private Image hud;
    private boolean isSelected;

    public IleDescriptor(PixelText titre,Bouton ile, Bouton playButton, Bouton infoButton, Image hud){
        this.titre = titre;
        this.ile = ile;
        this.playButton = playButton;
        this.infoButton = infoButton;
        this.hud = hud;
    }

    public PixelText getTitre() {
        return titre;
    }

    public Bouton getIle() {
        return ile;
    }

    public Bouton getPlayButton() {
        return playButton;
    }

    public Bouton getInfoButton() {
        return infoButton;
    }

    public Image getHud() {
        return hud;
    }
    public boolean getIsSelected(){
        return isSelected;
    }
    public void setIsSelected(boolean b){
        this.isSelected = b;
    }

    public void update(){
        this.ile.update();
        if(isSelected) {
            this.hud.update();
            this.playButton.update();
            this.infoButton.update();
        }
    }
    public void draw(Canvas canvas){
        this.ile.draw(canvas);
        if(isSelected) {
            this.hud.draw(canvas);
            this.playButton.draw(canvas);
            this.infoButton.draw(canvas);
            this.titre.draw(canvas);
        }
    }

    public void unselectAll() {
        playButton.setIsPressed(false);
        infoButton.setIsPressed(false);
    }
}
