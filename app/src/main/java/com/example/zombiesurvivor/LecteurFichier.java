package com.example.zombiesurvivor;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.zombiesurvivor.Data.ListMovable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LecteurFichier {

    // Méthode statique pour lire le fichier et récupérer les informations
    public static void chargerPartie(Context context, Game partie, String cheminFichier) {
        double temps = System.currentTimeMillis();
        AssetManager assetManager = context.getAssets();
        try {
            // Ouvrir le fichier "niveau1.txt" dans le dossier assets
            InputStream inputStream = assetManager.open(cheminFichier);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String ligne;
            String carte = "";

            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("Fond :")) {

                }else if (ligne.startsWith("Titre :")) {

                } else if (ligne.startsWith("Carte:")) {
                    continue;
                } else {
                    if (!ligne.trim().isEmpty()) {
                        carte += ligne + "\n";
                    } else {
                        carte += "\n";
                    }
                }
            }

            traiterCarte(carte, partie);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        System.out.println("Fin du traitement, temps pour réaliser : " + (System.currentTimeMillis()-temps)/1000 + "secondes");
    }

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




    private static void traiterCarte(String carte, Game partie) {
        int x = 0;
        int y = 0;

        // Obstacle de référence pour déterminer les dimensions
        int tailleCase = 150;

        // Diviser la carte en lignes
        String[] lignes = carte.split("\n");

        // Parcours de chaque ligne
        for (String ligne : lignes) {
            // Si la ligne contient uniquement des /, c'est un séparateur visuel
            if (ligne.trim().contains("//////////////////////////////////")) {
                // Séparer les obstacles dans la ligne
                x = 0;  // Réinitialiser la position x au début de la ligne
                y += tailleCase;  // Passer à la ligne suivante
                continue;  // Passer à la ligne suivante
            }

            // Diviser la ligne en sections de 5 caractères
            String[] obstacles = ligne.split("/");

            // Parcours des sections (chaque section est un obstacle)
            for (String obstacleCode : obstacles) {
                if (obstacleCode.trim().isEmpty()) {
                    continue;  // Si la section est vide, on l'ignore
                }
                if(obstacleCode.equals("SPAWN")){

                }
                boolean found = false;
                for (int i = 0; i < ListMovable.chemins.size(); i++) {
                    if (ListMovable.chemins.get(i).getCode().equals(obstacleCode)) {
                        if(ListMovable.chemins.get(i).getType() == "floor"){
                            partie.getCarte().getFloors().add(new Floor(
                                    partie.getContext(), x, y, tailleCase, tailleCase, ListMovable.chemins.get(i).getCheminImage(), false, 100
                            ,ListMovable.chemins.get(i).getEnfoncementTop()
                                    ,ListMovable.chemins.get(i).getEnfoncementBottom()
                                    ,ListMovable.chemins.get(i).getEnfoncementLeft()
                                    ,ListMovable.chemins.get(i).getEnfoncementRight()));
                        }
                        if(ListMovable.chemins.get(i).getType().equals("wall")){
                            partie.getCarte().getObstacles().add(new UndestroyableObstacle(
                                    partie.getContext(), x, y, tailleCase, tailleCase
                                    ,ListMovable.chemins.get(i).getEnfoncementTop()
                                    ,ListMovable.chemins.get(i).getEnfoncementBottom()
                                    ,ListMovable.chemins.get(i).getEnfoncementLeft()
                                    ,ListMovable.chemins.get(i).getEnfoncementRight(),
                                    ListMovable.chemins.get(i).getCheminImage(), false,
                                    100, 50
                            ));
                        }
                        x += (int) tailleCase;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    x += (int) tailleCase;
                    System.out.println("Jsp mecc");
                }
            }
        }
        System.out.println("Fin de l'analyse, nb floor : " + partie.getCarte().getFloors().size());
    }

    /*/public void saveMap(Game partie) {
        String baseFileName = "niveauSave";
        File file = new File(partie.getContext().getFilesDir(), baseFileName + ".txt");

        int counter = 1;
        while (file.exists()) {
            file = new File(partie.getContext().getFilesDir(), baseFileName + "_" + counter + ".txt");
            counter++;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Écrire le titre du niveau
            writer.write("Titre : " + partie.getTitre() + "\n");

            // Écrire le fond du niveau
            writer.write("Fond : " + partie.getCheminFond() + "\n");

            // Écrire la carte
            writer.write("Carte:\n");

            double hauteur = 0;
            double longueur = 0;
            //Cherche le bout de la carte
            for(Obstacle o: partie.getObstacles()){
                if(o.getX()+o.getSizeX() > longueur) longueur = o.getX()+o.getSizeX();
                if(MainActivity.screenHeight - (o.getY()+o.getSizeY()) > hauteur) hauteur = MainActivity.screenHeight - (o.getY()+o.getSizeY());
            }
            hauteur-=MainActivity.screenHeight;
            System.out.println("Longueur : " + longueur);
            System.out.println("Hauteur : " + hauteur);

            System.out.println("Hauteur écran : " + MainActivity.screenHeight);
            System.out.println("-Hauteur : " + -hauteur);
            // Crée une représentation textuelle de la carte
            for (int y = MainActivity.screenHeight; y > -hauteur-200; y-= (int) partie.getObstacles().get(0).getSizeY()) {
                StringBuilder line = new StringBuilder();
                System.out.println("Je cherche pour Y : " + y);
                for (int x = 0; x < longueur; x+=(int) partie.getObstacles().get(0).getSizeX()) {
                    Obstacle obstacle = Obstacle.isOneHere(partie.getObstacles(), x+50, y-50);
                    System.out.println("Je cherche pour X : " + x);
                    if (obstacle != null) {
                        String chemin = obstacle.getCheminImage();
                        for(int i=0; i<Obstacle.listeObstacles.length; i++){
                            if(chemin == Obstacle.listeObstacles[i]){
                                line.append(Obstacle.codeObstacles[i]+"/");
                            }
                        }
                    } else {
                        line.append("XXXX/"); // Représentation de l'espace vide
                    }
                }

                // Enlever le dernier "/"
                if (line.length() > 0) {
                    line.deleteCharAt(line.length() - 1);
                }

                // Ajouter la ligne à la carte
                writer.write(line.toString() + "\n/////////////////////////////////////////////////////////////\n");

            }

            // Fermer le writer
            writer.close();

            System.out.println("Carte sauvegardée dans : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la sauvegarde de la carte : " + e.getMessage());
        }
    }*/


    /*public static ArrayList<String> getAllFilesNames(Context context) {
        // Créer une nouvelle ArrayList pour stocker les fichiers "niveau"
        ArrayList<String> niveaux = new ArrayList<>();

        // Récupérer le gestionnaire d'actifs
        AssetManager assetManager = context.getAssets();

        try {
            // Liste tous les fichiers et dossiers dans le dossier "assets"
            String[] files = assetManager.list("");

            // Parcours chaque fichier dans assets
            if (files != null) {
                for (String file : files) {
                    // Vérifie si le nom du fichier commence par "niveau"
                    if (file.startsWith("niveau")) {
                        niveaux.add(file); // Ajouter à la liste si le fichier commence par "niveau"
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return niveaux;
        // À ce point, `niveaux` contient les noms de tous les fichiers qui commencent par "niveau"
    }

    public static ArrayList<String> getAllTitles(Context context) {
        // Créer une nouvelle ArrayList pour stocker les fichiers "niveau"
        ArrayList<String> titles = new ArrayList<>();

        // Récupérer le gestionnaire d'actifs
        AssetManager assetManager = context.getAssets();

        try {
            // Liste tous les fichiers et dossiers dans le dossier "assets"
            String[] files = assetManager.list("");

            // Parcours chaque fichier dans assets
            if (files != null) {
                for (String file : files) {
                    // Vérifie si le nom du fichier commence par "niveau"
                    if (file.startsWith("niveau")) {
                        // Ouvrir le fichier et lire son contenu
                        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(file)));

                        String line;
                        // Lire le fichier ligne par ligne
                        while ((line = reader.readLine()) != null) {
                            // Vérifier si la ligne commence par "Titre : "
                            if (line.startsWith("Titre : ")) {
                                // Extraire la partie après "Titre : "
                                String title = line.substring("Titre : ".length()).trim();
                                titles.add(title); // Ajouter le titre à la liste
                                break; // Quitter la boucle après avoir trouvé le titre
                            }
                        }

                        // Fermer le lecteur
                        reader.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return titles;
    }*/
}
