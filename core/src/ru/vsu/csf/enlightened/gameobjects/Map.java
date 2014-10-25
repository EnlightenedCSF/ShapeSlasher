package ru.vsu.csf.enlightened.gameobjects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.ComboTree;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.gameobjects.collisions.HeroCollideListener;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

import java.util.ArrayList;

public class Map {

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

    public Map() {
        world = new World(new Vector2(0, -GRAVITY), true);
        world.setContactListener(new HeroCollideListener());

        hero = new Hero(world);
        enemies = new ArrayList<Dummy>();

        keyController = new ComboTree(hero);

        generateLevel();

        searchForSolidGround();


        enemies.add(new Dummy(world, 5, 5));
        enemies.add(new Dummy(world, 7, 5));
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
            density = 0.001f;
            filter.categoryBits = EntityTypes.GROUND_CATEGORY;
            filter.maskBits = EntityTypes.GROUND_MASK;
        }};

        grounds.get(0).createFixture(fixtureDef);
        grounds.get(0).setUserData(this);
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
                else if (j == 8 && i % 7 == 6) {
                    tiles[i][j] = 1;
                }
            }
        }
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void update(float delta) {
        keyController.tick(delta);
        hero.update(delta);
    }

    public void keyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT)
            hero.setShieldUp(true);
        else
            keyController.keyDown(keycode);
    }

    public void keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT)
            hero.setShieldUp(false);
        else
            keyController.keyUp(keycode);
    }


    /*Body square;
    Body circle;

    private void test() {

         square = world.createBody(new BodyDef() {{
             type = BodyType.DynamicBody;
             position.set(0, 0);
        }});

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = new PolygonShape(){{setAsBox(0.5f, 0.5f);}};
            density = 0.5f;
            restitution = 1;
            filter.maskBits = 0;
        }};

        square.createFixture(fixtureDef);

        circle = world.createBody(new BodyDef() {{
            type = BodyType.DynamicBody;
            position.set(0.5f, 0.5f);
        }});

        circle.createFixture(new FixtureDef() {{
            shape = new CircleShape() {{ setRadius( 0.5f );}};
            density = 0.5f;
            restitution = 1;
            filter.maskBits = 0;
        }});

        RevoluteJointDef jointDef = new RevoluteJointDef();
        //jointDef.initialize(square, circle, circle.getWorldCenter());
        jointDef.bodyA = square;
        jointDef.bodyB = circle;
        jointDef.localAnchorA.set(circle.getPosition());
        jointDef.localAnchorB.set(square.getPosition());
        world.createJoint(jointDef);

    }*/
}

