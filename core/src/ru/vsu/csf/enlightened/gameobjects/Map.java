package ru.vsu.csf.enlightened.gameobjects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.ComboTree;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.gameobjects.collisions.HeroCollideListener;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Map {
    private static final int NOTHING    = 0;
    private static final int GROUND     = 1;
    private static final int PLAYER     = 2;
    private static final int ENEMY      = 3;

    private static final float GRAVITY = 12;

    private int[][] tiles;
    private ArrayList<Body> grounds;

    private Hero hero;
    private ArrayList<Dummy> enemies;

    private World world;

    private ComboTree keyController;

    public World getWorld() {
        return world;
    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<Dummy> getEnemies() {
        return enemies;
    }

    public Map() {
        world = new World(new Vector2(0, -GRAVITY), true);
        world.setContactListener(new HeroCollideListener());
        enemies = new ArrayList<Dummy>();
    }

    public void loadLevel(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            String[] sizes = reader.readLine().split(" ");
            tiles = new int[Integer.parseInt(sizes[0])][Integer.parseInt(sizes[1])];

            for (int j = 0; j < tiles[0].length; j++) {
                String[] chunks = reader.readLine().split(" ");

                for (int i = 0; i < tiles.length; i++) {
                    int code = Integer.parseInt(chunks[i]);
                    switch (code) {
                        case NOTHING:
                        case GROUND:
                            tiles[i][j] = code;
                            break;
                        case PLAYER:
                            tiles[i][j] = NOTHING;
                            hero = new Hero(world, i, tiles[0].length - j - 1);
                            break;
                        case ENEMY:
                            tiles[i][j] = NOTHING;
                            enemies.add(new Dummy(world, i, tiles[0].length - j - 1));
                            break;
                    }
                }
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        searchForSolidGround();
        keyController = new ComboTree(hero);
    }

    private void searchForSolidGround() {
        int width = tiles.length;
        int height = tiles[0].length;

        grounds = new ArrayList<Body>();

        int len = 0;
        int startCol = 0;
        boolean start = false;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (tiles[i][j] == 1) {
                    if (!start) {
                        start = true;
                        startCol = i;
                    }

                    len++;
                }
                else if (start) {
                    createGroundBlock(j, startCol, len);
                    start = false;
                    len = 0;
                }
            }

            if (start) {
                createGroundBlock(j, startCol, len);
                start = false;
                len = 0;
            }
        }
    }

    private void createGroundBlock(final int height, final int startIndex, final int length) {

        final int h = tiles[0].length;

        BodyDef groundBodyDef = new BodyDef() {{
            position.set(new Vector2(startIndex + length/2.0f, 0.5f + h - (height + 1)));
        }};

        grounds.add(0, world.createBody(groundBodyDef));

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = new PolygonShape() {{
                setAsBox(0.5f * length, 0.5f);
            }};
            friction = 0;
            density = 0.001f;
            filter.categoryBits = EntityTypes.GROUND;
            filter.maskBits = EntityTypes.GROUND_MASK;
        }};

        grounds.get(0).createFixture(fixtureDef);
        grounds.get(0).setUserData(this);
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void update(float delta) {
        keyController.tick(delta);
        hero.update(delta);

        for (Dummy d : enemies) {
            d.update(delta);
        }
    }

    public void keyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            hero.setShieldUp(true);
            keyController.endCombo();
        }
        else if (keycode == ComboTree.THROW_PROJECTILE_BUTTON_CODE) {
            hero.throwProjectile();
            keyController.endCombo();
        }
        else
            keyController.keyDown(keycode);
    }

    public void keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            hero.setShieldUp(false);
            keyController.endCombo();
        }
    }
}

