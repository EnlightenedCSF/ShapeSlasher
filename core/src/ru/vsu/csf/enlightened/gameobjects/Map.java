package ru.vsu.csf.enlightened.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.gameobjects.collisions.HeroCollideListener;

public class Map {

    private int[][] tiles;
    private Body[] grounds;

    private Hero hero;

    private World world;


    public World getWorld() {
        return world;
    }

    public Hero getHero() {
        return hero;
    }

    public Map() {
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new HeroCollideListener());

        hero = new Hero(world);

        generateLevel();
        searchForSolidGround();
    }

    private void searchForSolidGround() {
        int width = tiles.length;
        int height = tiles[0].length;

        grounds = new Body[width];

        for (int col = 0; col < width; col++) {

            int row_start = -1;
            int len = 0;

            for (int row = 0; row < height; row++) {
                if (tiles[col][row] == 1) {
                    if (row_start == -1)
                        row_start = row;
                    len++;
                }
            }

            final int finalCol = col;
            final int finalLen = len;
            BodyDef groundBodyDef = new BodyDef() {{
                position.set(new Vector2(0.5f + finalCol, 0.5f * finalLen));
            }};

            grounds[col] = world.createBody(groundBodyDef);

            PolygonShape groundBox = new PolygonShape() {{
                setAsBox(0.5f, 0.5f * finalLen);
            }};
            grounds[col].createFixture(groundBox, 0.0f);

            groundBox.dispose();
        }
    }

    private void generateLevel() {
        tiles = new int[20][10];

        int width = tiles.length;
        int height = tiles[0].length;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (j == 9) {
                    tiles[i][j] = 1;
                }
                else if (j == 8 && i % 3 == 2) {
                    tiles[i][j] = 1;
                }
            }
        }
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void update(float delta) {
        hero.update();
    }
}

