package com.example.zombiesurvivor.carte;

import android.content.Context;

import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;
import com.example.zombiesurvivor.donjon.Donjon;

import java.util.ArrayList;
import java.util.Random;

    public class GenerateurNiveau {
        public static final int LARGEUR_CARTE = 100;  // Largeur de la carte (100 tuiles)
        public static final int HAUTEUR_CARTE = 100;  // Hauteur de la carte (100 tuiles)
        public static final int TAILLE_CASE = 100;
        public static final int NB_LAYER = 5;

        // Paramètres du bruit de Perlin
        private static final float SEUIL_EAU = 0.15f;  // Valeur en dessous de laquelle on a de l'eau
        private static final float SEUIL_SOL = 0.15f;  // Valeur au-dessus de laquelle on a du sol
        private static final float SEUIL_VERDDURE = 0.15f;
        private static final float PERTURBATION = 0.05f;  // L'influence du bruit pour des variations douces

        public static int LAYER_TREE = 3;
        public static int LAYER_PLAYER = 3;
        public static int LAYER_FLOOR = 0;
        public static int LAYER_SHADOW = 2;
        public static int LAYER_WALLS = 1;

        private static PerlinNoise perlinNoise;
        private static ArrayList<ArrayList<ArrayList<Movable>>> objetsCarte = new ArrayList<>();

        public static Map genererCarte(Context context) {
            Random random = new Random();
            //Pour avoir la bonne longueur et des emplacements dans objetsCarte
            for(int i = 0; i < LARGEUR_CARTE; i++){
                objetsCarte.add(new ArrayList<>());
                for(int j = 0; j < HAUTEUR_CARTE; j++){
                    objetsCarte.get(i).add(new ArrayList<>());
                    for(int k = 0; k < NB_LAYER; k++){
                        objetsCarte.get(i).get(j).add(null);
                    }
                }
            }

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
                    TypeTile type;
                    if (carteHauteurs[x][y] < SEUIL_EAU) {
                        type = TypeTile.VIDE;
                    } else if (carteHauteurs[x][y] < SEUIL_SOL) {
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
            ajouterArbres(context);
            ajouterDonjon(carte, 15, 1);

            // Créer et retourner la carte avec les tuiles générées
            Map m = new Map(objetsCarte);
            return m;
        }

        private static void ajouterSpawnPoint(TypeTile[][] carte, Context context) {
            for(int i = LARGEUR_CARTE/2; i > 0; i--){
                for(int j = HAUTEUR_CARTE/2; j > 0; j--){
                    if(carte[i][j] == TypeTile.SOL){
                        carte[i][j] = TypeTile.PLAYERSPAWN;
                        objetsCarte.get(i).get(j).set(1, new Movable(
                                i*TAILLE_CASE, j*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE, "spawnpoint", true,
                                80, 0, 0, 0, 0));
                        Game.getPartie().getJoueur().setSpawnPoint(i*TAILLE_CASE, j*TAILLE_CASE);
                        return;
                    }
                }
            }
        }

        // Fonction pour ajouter des murs autour des îles
        private static void ajouterMurs(TypeTile[][] carte) {
            ArrayList<Movable> obs = ListOfMovable.getAllBorders();
            int i = 0;
            for (int x = 1; x < LARGEUR_CARTE-1; x++) {
                for (int y = 1; y < HAUTEUR_CARTE-1; y++) {
                    if (carte[x][y] == TypeTile.SOL) {
                        boolean haut, bas, gauche, droite, haut_gauche, haut_droite, bas_gauche, bas_droite;
                        if (x == LARGEUR_CARTE - 2) droite = true;
                        else droite = carte[x + 1][y] == TypeTile.VIDE;
                        if (x == 1) gauche = true;
                        else gauche = carte[x - 1][y] == TypeTile.VIDE;
                        if (y == 1) haut = true;
                        else haut = carte[x][y - 1] == TypeTile.VIDE;
                        if (y == HAUTEUR_CARTE - 2) bas = true;
                        else bas = carte[x][y + 1] == TypeTile.VIDE;
                        if (x == LARGEUR_CARTE - 2 && y == HAUTEUR_CARTE - 2) haut_droite = true;
                        else haut_droite = carte[x + 1][y - 1] == TypeTile.VIDE;
                        if (x == 1 && y == HAUTEUR_CARTE - 2) haut_gauche = true;
                        else haut_gauche = carte[x - 1][y - 1] == TypeTile.VIDE;
                        if (x == LARGEUR_CARTE - 2 && y == 1) bas_droite = true;
                        else bas_droite = carte[x + 1][y + 1] == TypeTile.VIDE;
                        if (x == 1 && y == 1) bas_gauche = true;
                        else bas_gauche = carte[x - 1][y + 1] == TypeTile.VIDE;


                        int indexImg = 0;
                        carte[x][y] = TypeTile.BORDURE;

                        if (haut && gauche) {
                            indexImg = 0;
                        } else if (haut && droite) {
                            indexImg = 2;
                        } else if (bas && gauche) {
                            indexImg = 4;
                        } else if (bas && droite) {
                            indexImg = 6;
                        }


                        // Murs simples (haut, bas, gauche, droite)
                        else if (haut && !bas && !gauche && !droite) {
                            indexImg = 1;
                        } else if (bas && !haut && !gauche && !droite) {
                            indexImg = 5;
                        } else if (gauche && !haut && !bas && !droite) {
                            indexImg = 7;
                        } else if (droite && !haut && !bas && !gauche) {
                            indexImg = 3;
                        } else if (!haut && !droite && haut_droite) {
                            indexImg = 10;
                        } else if (!haut && !gauche && haut_gauche) {
                            indexImg = 11;
                        } else if (!bas && !gauche && bas_gauche) {
                            indexImg = 9;
                        } else if (!bas && !droite && bas_droite) {
                            indexImg = 8;
                        } else {
                            carte[x][y] = TypeTile.SOL;
                            indexImg = 12;
                        }

                        // Ajouter la tuile murale à la liste
                        int xPos = x * TAILLE_CASE;
                        int yPos = y * TAILLE_CASE;
                        if (indexImg == 12) {
                            objetsCarte.get(x).get(y).set(LAYER_FLOOR, new Movable(obs.get(12)));
                            objetsCarte.get(x).get(y).get(LAYER_FLOOR).setPosX(xPos);
                            objetsCarte.get(x).get(y).get(LAYER_FLOOR).setPosY(yPos);
                            objetsCarte.get(x).get(y).get(LAYER_FLOOR).addTag(Tag.SOL);
                        } else if (carte[x][y] == TypeTile.BORDURE) {
                            objetsCarte.get(x).get(y).set(LAYER_WALLS, new Movable(obs.get(indexImg)));
                            objetsCarte.get(x).get(y).get(LAYER_WALLS).setPosX(xPos);
                            objetsCarte.get(x).get(y).get(LAYER_WALLS).setPosY(yPos);
                            objetsCarte.get(x).get(y).get(LAYER_WALLS).addTag(Tag.SOLIDE);
                            objetsCarte.get(x).get(y).get(LAYER_WALLS).removeTag(Tag.SOL);
                        }
                    }
                    int xPos = x * TAILLE_CASE;
                    int yPos = y * TAILLE_CASE;
                    if (carte[x][y] == TypeTile.VIDE) {
                        objetsCarte.get(x).get(y).set(LAYER_FLOOR, new Movable(obs.get(13)));
                        objetsCarte.get(x).get(y).get(LAYER_FLOOR).setPosX(xPos);
                        objetsCarte.get(x).get(y).get(LAYER_FLOOR).setPosY(yPos);
                        objetsCarte.get(x).get(y).get(LAYER_FLOOR).removeTag(Tag.SOL);
                        objetsCarte.get(x).get(y).get(LAYER_FLOOR).addTag(Tag.EAU);
                    }
                    i++;
                    System.out.println("i : " + i);
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
                            //On recupere la taille
                            objetsCarte.get(x).get(y).set(0, new Movable(xPos, yPos, TAILLE_CASE, TAILLE_CASE, cheminDeco, true, 100, 0, 0, 0, 0));
                            //Taille vaut indexNb+1 donc on recupere indexNb
                            objetsCarte.get(x).get(y).get(0).addTag(Tag.DECORATION);
                        }
                    }
                }
            }
        }

        static String cheminsArbres = "lol";
        static String ombresArbres = "lol_shadow";
        private static void ajouterArbres(Context context) {
            Random random = new Random();
            PerlinNoise perlin = new PerlinNoise(random);
            float pertub = 0.5f;

            for (int x = 0; x < LARGEUR_CARTE; x++) {
                for (int y = 0; y < HAUTEUR_CARTE; y++) {
                    if(perlin.getNoise(x*pertub, y*pertub) > 0.3f){
                        if (objetsCarte.get(x).get(y).get(0) != null && objetsCarte.get(x).get(y).get(0).hasTag(Tag.SOL)) {
                            int xPos = x * TAILLE_CASE;
                            int yPos = y * TAILLE_CASE;
                            int indexArbre = random.nextInt(ListOfMovable.getAllTrees().size());
                            objetsCarte.get(x).get(y).set(LAYER_TREE, new Movable(ListOfMovable.getTree(indexArbre)));
                            objetsCarte.get(x).get(y).set(LAYER_SHADOW, new Movable(ListOfMovable.getTreeShadow(indexArbre)));
                            objetsCarte.get(x).get(y).get(LAYER_TREE).setPosX(xPos);
                            objetsCarte.get(x).get(y).get(LAYER_TREE).setPosY(yPos);
                            objetsCarte.get(x).get(y).get(LAYER_SHADOW).setPosX(xPos);
                            objetsCarte.get(x).get(y).get(LAYER_SHADOW).setPosY(yPos);
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

                    if (objetsCarte.get(xi).get(yj).get(0) == null || !objetsCarte.get(xi).get(yj).get(0).hasTag(Tag.SOL) && objetsCarte.get(xi).get(yj).size() <= 1)
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
                        int nbIndex = objetsCarte.get(yj).get(xi).size();
                        objetsCarte.get(yj).get(xi).add(dj.getTile(i * 3 + j));

                        if (objetsCarte.get(yj).get(xi).get(nbIndex) != null) {
                            objetsCarte.get(yj).get(xi).get(nbIndex).removeTag(Tag.SOL);
                            objetsCarte.get(yj).get(xi).get(nbIndex).addTag(Tag.DONJON);

                            objetsCarte.get(yj).get(xi).get(nbIndex).setPosX(yj * TAILLE_CASE);
                            objetsCarte.get(yj).get(xi).get(nbIndex).setPosY(xi * TAILLE_CASE);
                        }
                    }
                }
            }
        }



    }
