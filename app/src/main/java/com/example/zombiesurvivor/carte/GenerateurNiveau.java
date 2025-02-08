package com.example.zombiesurvivor.carte;

import android.content.Context;

import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;
import com.example.zombiesurvivor.donjon.Donjon;

import java.util.Random;

    public class GenerateurNiveau {
        public static final int LARGEUR_CARTE = 100;  // Largeur de la carte (100 tuiles)
        public static final int HAUTEUR_CARTE = 100;  // Hauteur de la carte (100 tuiles)
        public static final int TAILLE_CASE = 100;

        // Paramètres du bruit de Perlin
        private static final float SEUIL_EAU = -0.2f;  // Valeur en dessous de laquelle on a de l'eau
        private static final float SEUIL_SOL = -0.2f;  // Valeur au-dessus de laquelle on a du sol
        private static final float SEUIL_VERDDURE = 0.15f;
        private static final float PERTURBATION = 0.05f;  // L'influence du bruit pour des variations douces

        private static PerlinNoise perlinNoise;
        private static Movable[][] objetsCarte = new Movable[LARGEUR_CARTE][HAUTEUR_CARTE];

        public static Map genererCarte(Context context) {
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
                    TypeTile type;
                    if (hauteur < SEUIL_EAU) {
                        type = TypeTile.VIDE;
                    } else if (hauteur < SEUIL_SOL) {
                        type = TypeTile.SOL;
                    }  else {
                        type = TypeTile.SOL;
                    }
                    carte[x][y] = type;
                }
            }

            ajouterMurs(carte);
            ajouterSpawnPoint(carte, context);

            String[] cheminsDeco = {"example_tile_deco_herbe", "example_tile_deco_fleur",
                    "example_tile_deco_herbe_moins", "example_tile_deco_fleur_moins"};
            ajouterDecorations(carte, context, cheminsDeco);
            ajouterDonjon(carte, 15, 1);

            // Créer et retourner la carte avec les tuiles générées
            Map m = new Map(objetsCarte);
            m.setMap(carte);
            return m;
        }

        private static void ajouterSpawnPoint(TypeTile[][] carte, Context context) {
            for(int i = LARGEUR_CARTE/2; i > 0; i--){
                for(int j = HAUTEUR_CARTE/2; j > 0; j--){
                    if(carte[i][j] == TypeTile.SOL){
                        carte[i][j] = TypeTile.PLAYERSPAWN;
                        objetsCarte[i][j] = new Movable(context,
                                i*TAILLE_CASE, j*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, "spawnpoint", true,
                                80, 0, 0, 0, 0);
                        Game.getPartie().getJoueur().setSpawnPoint(i*TAILLE_CASE, j*TAILLE_CASE);
                        return;
                    }
                }
            }
        }

        // Fonction pour ajouter des murs autour des îles
        private static void ajouterMurs(TypeTile[][] carte) {
            for (int x = 1; x < LARGEUR_CARTE-1; x++) {
                for (int y = 1; y < HAUTEUR_CARTE-1; y++) {
                    if (carte[x][y] == TypeTile.SOL) {
                        boolean haut, bas, gauche, droite, haut_gauche, haut_droite, bas_gauche, bas_droite;
                        if(x == LARGEUR_CARTE-2) droite = true;
                        else droite = carte[x + 1][y] == TypeTile.VIDE;
                        if(x == 1) gauche = true;
                        else gauche = carte[x - 1][y] == TypeTile.VIDE;
                        if(y == 1) haut = true;
                        else haut = carte[x][y - 1] == TypeTile.VIDE;
                        if(y == HAUTEUR_CARTE-2) bas = true;
                        else bas = carte[x][y + 1] == TypeTile.VIDE;
                        if(x == LARGEUR_CARTE-2 && y == HAUTEUR_CARTE-2) haut_droite = true;
                        else haut_droite = carte[x + 1][y - 1] == TypeTile.VIDE;
                        if(x == 1 && y == HAUTEUR_CARTE-2) haut_gauche = true;
                        else haut_gauche = carte[x - 1][y - 1] == TypeTile.VIDE;
                        if(x == LARGEUR_CARTE-2 && y == 1) bas_droite = true;
                        else bas_droite = carte[x + 1][y + 1] == TypeTile.VIDE;
                        if(x == 1 && y == 1) bas_gauche = true;
                        else bas_gauche = carte[x - 1][y + 1] == TypeTile.VIDE;


                        String cheminImage = "example_tile";
                        carte[x][y] = TypeTile.BORDURE;

                        if (haut && gauche) {
                            cheminImage = "example_tile_mur_coin_haut_gauche";
                        } else if (haut && droite) {
                            cheminImage = "example_tile_mur_coin_haut_droit";
                        } else if (bas && gauche) {
                            cheminImage = "example_tile_mur_coin_bas_gauche";
                        } else if (bas && droite) {
                            cheminImage = "example_tile_mur_coin_bas_droit";
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
                            objetsCarte[x][y] = new Movable(Game.getPartie().getContext(), x*TAILLE_CASE, y*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE
                                    , "example_tile_vide", true, 100,
                                    0, 0, 0, 0);
                            objetsCarte[x][y].removeTag(Tag.SOL);
                        } else if(carte[x][y] == TypeTile.SOL){
                            objetsCarte[x][y] = new Movable(Game.getPartie().getContext(), x*TAILLE_CASE, y*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE
                                    , "example_tile", true, 100,
                                    0, 0, 0, 0);
                            objetsCarte[x][y].addTag(Tag.SOL);
                        } else if(carte[x][y] == TypeTile.BORDURE){
                            objetsCarte[x][y] = new Movable(Game.getPartie().getContext(), x*TAILLE_CASE, y*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE
                                    , cheminImage, true, 100,
                                    0, 0, 0, 0);
                            objetsCarte[x][y].addTag(Tag.SOLIDE);
                            objetsCarte[x][y].removeTag(Tag.SOL);
                        }
                    }
                }
            }

        }

        private static void ajouterDecorations(TypeTile[][] carte, Context context, String[] cheminsDeco) {
            Random random = new Random();
            PerlinNoise perlin = new PerlinNoise(random);
            float pertub = 0.2f;

            ajouterArbres(context);

            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    if(perlin.getNoise(x*pertub, y*pertub) > 0.2f){
                        if (carte[x][y] == TypeTile.SOL) {
                            int rand = random.nextInt(100);
                            String cheminDeco = "";
                            if(rand < 20) cheminDeco = cheminsDeco[0];
                            else cheminDeco = cheminsDeco[1];
                            int xPos = x * TAILLE_CASE;
                            int yPos = y * TAILLE_CASE;
                            carte[x][y] = TypeTile.DECO;
                            objetsCarte[x][y] = new Movable(context, xPos, yPos, TAILLE_CASE, TAILLE_CASE, cheminDeco, true, 100, 0, 0, 0, 0);
                            objetsCarte[x][y].addTag(Tag.DECORATION);
                        }
                    }
                }
            }
        }

        private static void ajouterArbres(Context context) {
            Random random = new Random();
            PerlinNoise perlin = new PerlinNoise(random);
            float pertub = 0.5f;

            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    if(perlin.getNoise(x*pertub, y*pertub) > 0.3f){
                        if (objetsCarte[x][y] != null && objetsCarte[x][y].hasTag(Tag.SOL)) {
                            System.out.println("AJOUT ARBRE");
                            int xPos = x * TAILLE_CASE;
                            int yPos = y * TAILLE_CASE;
                            objetsCarte[x][y] = new Movable(context, xPos, yPos, TAILLE_CASE, TAILLE_CASE, "example_tree"
                                    , true, 100,
                                    TAILLE_CASE*0.75, 0, 0, 0,
                                    TAILLE_CASE,0,0,0);
                            objetsCarte[x][y].addTag(Tag.ARBRE);
                            objetsCarte[x][y].addTag(Tag.SOLIDE);
                        }
                    }
                }
            }
        }

        private static void ajouterDonjon(TypeTile[][] carte, int nbDonjon, int taille) {
            Random random = new Random();
            int maxAttempts = 1000;
            while (nbDonjon > 0 && maxAttempts > 0) {
                int x = random.nextInt(LARGEUR_CARTE);
                int y = random.nextInt(HAUTEUR_CARTE);
                if (zoneLibre(x, y, taille)) {
                    transformTile(x, y, taille, TypeTile.DONJON, carte);
                    nbDonjon--;
                }
                maxAttempts--;  // Diminue le compteur d'essais
            }
        }



        public static boolean zoneLibre(int x, int y, int radius){
            if (x < radius || y < radius || x >= LARGEUR_CARTE - radius || y >= HAUTEUR_CARTE - radius) {
                return false;  // Empêche de placer un donjon sur les bords de la carte
            }

            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int xi = x + i;
                    int yj = y + j;

                    if (xi < 0 || yj < 0 || xi >= LARGEUR_CARTE || yj >= HAUTEUR_CARTE)
                        return false;  // Sécurité pour éviter un dépassement d'index

                    if (objetsCarte[xi][yj] == null || !objetsCarte[xi][yj].hasTag(Tag.SOL))
                        return false;  // Vérifie qu'il n'y a pas d'obstacle
                }
            }

            return true;
        }

        public static void transformTile(int x, int y, int radius, TypeTile typeTile, TypeTile[][] carte) {
            // Création du donjon avec une taille fixe 3x3
            Donjon dj = new Donjon(3, 3, "example_dungeon_", x - radius, y - radius);

            // Parcourir la zone du donjon
            for (int i = 0; i < 3; i++) {   // Itérer sur les lignes du donjon
                for (int j = 0; j < 3; j++) { // Itérer sur les colonnes du donjon
                    int xi = x + (i - 1);  // Centrer le donjon sur (x, y)
                    int yj = y + (j - 1);

                    if (xi >= 0 && yj >= 0 && xi < LARGEUR_CARTE && yj < HAUTEUR_CARTE) {
                        carte[yj][xi] = typeTile;
                        objetsCarte[yj][xi] = dj.getTile(i * 3 + j);

                        if (objetsCarte[yj][xi] != null) {
                            objetsCarte[yj][xi].removeTag(Tag.SOL);
                            objetsCarte[yj][xi].addTag(Tag.DONJON);

                            objetsCarte[yj][xi].setPosX(yj * TAILLE_CASE);
                            objetsCarte[yj][xi].setPosY(xi * TAILLE_CASE);
                        }
                    }
                }
            }
        }



    }
