package com.example.zombiesurvivor.carte;

import android.content.Context;

import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Obstacle;

import java.util.ArrayList;
import java.util.Random;

    public class GenerateurNiveau {
        private static final int LARGEUR_CARTE = 100;  // Largeur de la carte (100 tuiles)
        private static final int HAUTEUR_CARTE = 100;  // Hauteur de la carte (100 tuiles)
        public static final int TAILLE_CASE = 100;

        // Paramètres du bruit de Perlin
        private static final float SEUIL_EAU = -0.1f;  // Valeur en dessous de laquelle on a de l'eau
        private static final float SEUIL_SOL = -0.1f;  // Valeur au-dessus de laquelle on a du sol
        private static final float SEUIL_VERDDURE = 0.15f;
        private static final float PERTURBATION = 0.05f;  // L'influence du bruit pour des variations douces

        private static PerlinNoise perlinNoise;

        public static Map genererCarte(Context context) {
            ArrayList<Obstacle> obstacles = new ArrayList<>();
            ArrayList<Floor> floors = new ArrayList<>();
            ArrayList<Obstacle> brouillards = new ArrayList<>();
            Random random = new Random();

            // Initialisation du bruit de Perlin
            perlinNoise = new PerlinNoise(random);

            // Matrice de tuiles représentant la carte
            TypeTile[][] carte = new TypeTile[LARGEUR_CARTE][HAUTEUR_CARTE];

            // Générer les hauteurs du terrain avec du bruit de Perlin
            float[][] carteHauteurs = new float[LARGEUR_CARTE][HAUTEUR_CARTE];
            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    // Générer un bruit pour chaque case
                    float bruit = perlinNoise.getNoise(x * PERTURBATION, y * PERTURBATION);
                    carteHauteurs[x][y] = bruit;
                }
            }

            // Remplir la carte avec des tuiles en fonction des hauteurs
            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    float hauteur = carteHauteurs[x][y];
                    String cheminImage;
                    TypeTile type;

                    if (hauteur < SEUIL_EAU) {
                        // Zones d'eau (vide)
                        type = TypeTile.VIDE;
                        cheminImage = "example_tile_vide";
                    } else if (hauteur < SEUIL_SOL) {
                        // Zones de terre (sol)
                        type = TypeTile.SOL;
                        cheminImage = "example_tile";
                    }  else {
                        // Toute la zone au-dessus du seuil devient du sol (pour simplification)
                        type = TypeTile.SOL;
                        cheminImage = "example_tile";
                    }

                    // Ajouter la tuile à la liste
                    int xPos = x * TAILLE_CASE;
                    int yPos = y * TAILLE_CASE;


                    carte[x][y] = type;
                }
            }

            // Ajouter des murs autour des îles
            ajouterMurs(carte, context, obstacles, floors);
            String[] cheminsDeco = {"example_tile_deco_herbe", "example_tile_deco_fleur", "example_tile_deco_roche"};
            ajouterDecorations(carte, context, obstacles, cheminsDeco);

            // Créer et retourner la carte avec les tuiles générées
            Map m = new Map(obstacles, floors);
            m.setMap(carte);
            return m;
        }

        // Fonction pour ajouter des murs autour des îles
        private static void ajouterMurs(TypeTile[][] carte, Context context, ArrayList<Obstacle> obstacles, ArrayList<Floor> sols) {
            for (int x = 1; x < LARGEUR_CARTE - 1; x++) {
                for (int y = 1; y < HAUTEUR_CARTE - 1; y++) {
                    if (carte[x][y] == TypeTile.SOL) {
                        // Vérifier les cases voisines (haut, bas, gauche, droite) pour l'eau
                        boolean haut = carte[x][y - 1] == TypeTile.VIDE;
                        boolean bas = carte[x][y + 1] == TypeTile.VIDE;
                        boolean gauche = carte[x - 1][y] == TypeTile.VIDE;
                        boolean droite = carte[x + 1][y] == TypeTile.VIDE;

                        boolean haut_gauche = carte[x - 1][y - 1] == TypeTile.VIDE;  // Coin haut-gauche
                        boolean haut_droite = carte[x + 1][y - 1] == TypeTile.VIDE;  // Coin haut-droit
                        boolean bas_gauche = carte[x - 1][y + 1] == TypeTile.VIDE;  // Coin bas-gauche
                        boolean bas_droite = carte[x + 1][y + 1] == TypeTile.VIDE;  // Coin bas-droit

                        String cheminImage = "example_tile";
                        carte[x][y] = TypeTile.BORDURE;

                        // Détection des murs de coin (externes)
                        if (haut && gauche) {
                            cheminImage = "example_tile_mur_coin_haut_gauche";  // Coin externe haut-gauche
                        } else if (haut && droite) {
                            cheminImage = "example_tile_mur_coin_haut_droit";   // Coin externe haut-droit
                        } else if (bas && gauche) {
                            cheminImage = "example_tile_mur_coin_bas_gauche";   // Coin externe bas-gauche
                        } else if (bas && droite) {
                            cheminImage = "example_tile_mur_coin_bas_droit";    // Coin externe bas-droit
                        }


                        // Murs simples (haut, bas, gauche, droite)
                        else if (haut && !bas && !gauche && !droite) {
                            cheminImage = "example_tile_mur_haut";  // Mur simple haut
                        } else if (bas && !haut && !gauche && !droite) {
                            cheminImage = "example_tile_mur_bas";   // Mur simple bas
                        } else if (gauche && !haut && !bas && !droite) {
                            cheminImage = "example_tile_mur_gauche";  // Mur simple gauche
                        } else if (droite && !haut && !bas && !gauche) {
                            cheminImage = "example_tile_mur_droit";   // Mur simple droit
                        } else if(!haut && !droite && haut_droite){
                            cheminImage = "example_tile_mur_coin_interne_bas_gauche";
                        } else if(!haut && !gauche && haut_gauche){
                            cheminImage = "example_tile_mur_coin_interne_bas_droit";
                        } else if(!bas && !gauche && bas_gauche){
                            cheminImage = "example_tile_mur_coin_interne_haut_droit";
                        } else if(!bas && !droite && bas_droite){
                            cheminImage = "example_tile_mur_coin_interne_haut_gauche";
                        } else{
                            carte[x][y] = TypeTile.SOL;
                        }


                        // Ajouter la tuile murale à la liste
                        int xPos = x * TAILLE_CASE;
                        int yPos = y * TAILLE_CASE;
                        if(carte[x][y] == TypeTile.VIDE){

                        } else if(carte[x][y] == TypeTile.SOL){
                            sols.add(new Floor(context, xPos, yPos, TAILLE_CASE, TAILLE_CASE, cheminImage, true, 80, 0, 0, 0, 0));
                        } else if(carte[x][y] == TypeTile.BORDURE){
                            obstacles.add(new Obstacle(context, xPos, yPos, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  cheminImage, true, 100, 90, 90));
                        }
                    }
                    //LE BROUILLARD
                    if(x==1 && y == 1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_topleft", true, 100, 999, 999));
                    else if(x == 1 && y == HAUTEUR_CARTE-1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_bottomleft", true, 100, 999, 999));
                    else if(x == LARGEUR_CARTE-1 && y == 1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_topright", true, 100, 999, 999));
                    else if(x == LARGEUR_CARTE-1 && y == HAUTEUR_CARTE-1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_bottomright", true, 100, 999, 999));

                    else if(y == 1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_top", true, 100, 999, 999));
                    else if(x == 1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_left", true, 100, 999, 999));
                    else if(x == LARGEUR_CARTE-1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_right", true, 100, 999, 999));
                    else if(x == HAUTEUR_CARTE-1) obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard_bottom", true, 100, 999, 999));

                    //FIN LE BROUILLARD
                }
            }
            //GROS BROUILLARD AU DESSUS
            for(int x = 1; x < LARGEUR_CARTE; x++){
                for(int y = -5; y <= 0; y++){
                    obstacles.add(new Obstacle(context, x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0,  "brouillard", true, 100, 999, 999));
                }
            }
        }

        private static void ajouterDecorations(TypeTile[][] carte, Context context, ArrayList<Obstacle> decorations, String[] cheminsDeco) {
            Random random = new Random();

            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    // Vérifier si la case actuelle est du sol, car on ne veut pas mettre de déco sur de l'eau ou une bordure
                    if (carte[x][y] == TypeTile.SOL) {
                        // Générer un nombre aléatoire entre 0 et 99, 5% de chance d'ajouter une décoration
                        if (random.nextInt(100) < 5) {
                            // Choisir un chemin de décoration au hasard
                            String cheminDeco = cheminsDeco[random.nextInt(cheminsDeco.length)];

                            // Ajouter la décoration comme un obstacle
                            int xPos = x * TAILLE_CASE;
                            int yPos = y * TAILLE_CASE;
                            decorations.add(new Obstacle(context, xPos, yPos, TAILLE_CASE, TAILLE_CASE, 0, 0, 0, 0, cheminDeco, true, 100, 0, 0));
                        }
                    }
                }
            }
        }

    }
