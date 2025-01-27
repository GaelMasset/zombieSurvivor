package com.example.zombiesurvivor.compte;

public class Compte {
    private static Compte compte;

    private int pieces;
    private int gemmes;
    private int xp;

    public static Compte getCompte(){
        if(compte == null) compte = new Compte();
        return compte;
    }

    public int getPieces() {
        return pieces;
    }

    public int getGemmes() {
        return gemmes;
    }

    public int getXp() {
        return xp;
    }
}
