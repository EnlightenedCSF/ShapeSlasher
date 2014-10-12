package ru.vsu.csf.enlightened.gameobjects;

public class Map {

    private int[][] tiles;

    public Map() {
        tiles = new int[20][10];

        int width = tiles.length;
        int height = tiles[0].length;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (i < 5 && j == 9)
                    tiles[i][j] = 1;
                else
                    tiles[i][j] = 0;
            }
        }
    }

    public int[][] getTiles() {
        return tiles;
    }
}

