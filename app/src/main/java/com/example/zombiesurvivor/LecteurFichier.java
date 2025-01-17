package com.example.zombiesurvivor;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.zombiesurvivor.Data.ListMovable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LecteurFichier {

    // Méthode pour charger une partie à partir d'un fichier
    public static void chargerPartie(Context context, Game partie, String cheminFichier) {
        double tempsDebut = System.currentTimeMillis();
        AssetManager assetManager = context.getAssets();

        try (InputStream inputStream = assetManager.open(cheminFichier);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String ligne;
            StringBuilder carteBuilder = new StringBuilder();

            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("Carte:")) {
                    continue;
                } else if (!ligne.trim().isEmpty()) {
                    carteBuilder.append(ligne).append("\n");
                } else {
                    carteBuilder.append("\n");
                }
            }

            traiterCarte(carteBuilder.toString(), partie);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        double tempsFin = System.currentTimeMillis();
        System.out.println("Fin du traitement, temps écoulé : " + (tempsFin - tempsDebut) / 1000 + " secondes");
    }

    // Méthode pour retourner une carte sous forme inversée
    public static String retournerCarte(String carte) {
        String[] lignes = carte.split("\n");
        StringBuilder carteRetournee = new StringBuilder();

        for (int i = lignes.length - 1; i >= 0; i--) {
            carteRetournee.append(lignes[i]);
            if (i > 0) {
                carteRetournee.append("\n");
            }
        }

        return carteRetournee.toString();
    }

    // Méthode pour traiter une carte et mettre à jour les éléments du jeu
    private static void traiterCarte(String carte, Game partie) {
        int x = 0, y = 0;
        int tailleCase = 150; // Taille des cases
        String[] lignes = carte.split("\n");

        for (String ligne : lignes) {
            if (ligne.trim().contains("//////////////////////////////////")) {
                x = 0;
                y += tailleCase;
                continue;
            }

            String[] obstacles = ligne.split("/");

            for (String obstacleCode : obstacles) {
                if (obstacleCode.trim().isEmpty()) {
                    continue;
                }

                boolean found = false;
                for (int i = 0; i < ListMovable.chemins.size(); i++) {
                    if (ListMovable.chemins.get(i).getCode().equals(obstacleCode)) {
                        if (ListMovable.chemins.get(i).getType().equals("floor")) {
                            partie.getCarte().getFloors().add(new Floor(
                                    partie.getContext(), x, y, tailleCase, tailleCase,
                                    ListMovable.chemins.get(i).getCheminImage(), false, 100,
                                    ListMovable.chemins.get(i).getEnfoncementTop(),
                                    ListMovable.chemins.get(i).getEnfoncementBottom(),
                                    ListMovable.chemins.get(i).getEnfoncementLeft(),
                                    ListMovable.chemins.get(i).getEnfoncementRight()
                            ));
                        } else if (ListMovable.chemins.get(i).getType().equals("wall")) {
                            partie.getCarte().getObstacles().add(new Obstacle(
                                    partie.getContext(), x, y, tailleCase, tailleCase,
                                    ListMovable.chemins.get(i).getEnfoncementTop(),
                                    ListMovable.chemins.get(i).getEnfoncementBottom(),
                                    ListMovable.chemins.get(i).getEnfoncementLeft(),
                                    ListMovable.chemins.get(i).getEnfoncementRight(),
                                    ListMovable.chemins.get(i).getCheminImage(), false, 100, 50,50
                            ));
                        }
                        x += tailleCase;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    x += tailleCase;
                    System.out.println("Obstacle inconnu : " + obstacleCode);
                }
            }
        }

        System.out.println("Fin de l'analyse, nombre de floors : " + partie.getCarte().getFloors().size());
    }
}
