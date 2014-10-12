package ru.vsu.csf.enlightened.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import ru.vsu.csf.enlightened.gameobjects.Map;

public class MapRenderer {

    public static final int BLOCK_COUNT_WIDTH = 12;
    public static final int BLOCK_COUNT_HEIGHT = 7;
    public static final int BLOCK_WIDTH = 70;
    public static final int BLOCK_HEIGHT = 70;

    public static final float CAM_VELOCITY = 9;

    private Map map;

    private OrthographicCamera camera;
    private Vector2 camVelocity;


    private TextureRegion tile;

    private SpriteCache cache;
    private int[][] blocks;


    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Body body;

    public MapRenderer(Map map) {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        this.map = map;
        this.camera = new OrthographicCamera(BLOCK_COUNT_WIDTH, BLOCK_COUNT_HEIGHT);
        this.camera.position.set(0, 2, 0);
        this.camVelocity = new Vector2(0, 0);

        this.cache = new SpriteCache(map.getTiles().length * map.getTiles()[0].length, false);
        this.blocks = new int[(int) Math.ceil(map.getTiles().length / (float) BLOCK_COUNT_WIDTH)][(int) Math.ceil(map.getTiles()[0].length / (float) BLOCK_COUNT_HEIGHT)];

        this.tile = new TextureRegion(new Texture(Gdx.files.internal("assets/tiles/ground.png")), 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);

        createBlocks();
        createShapes();
    }

    private void createShapes() {
        //region Circle
        BodyDef bodyDef = new BodyDef()
        {{
                type = BodyType.DynamicBody;
                position.set(1, 4);
            }};

         body = world.createBody(bodyDef);

        final CircleShape circleShape = new CircleShape(){{
            setRadius(0.5f);
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = circleShape;
            density = 0.5f;
            friction = 0.4f;
            restitution = 0.0f;
        }};

        body.createFixture(fixtureDef);
        circleShape.dispose();
        //endregion


        //region Ground
        BodyDef groundBodyDef = new BodyDef() {{
            position.set(new Vector2(1f, 0.5f));
        }};

        Body ground = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape() {{
            setAsBox(1f, 0.5f);
        }};
        ground.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        //endregion
    }

    private void createBlocks() {
        int width = map.getTiles().length;
        int height = map.getTiles()[0].length;

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * BLOCK_COUNT_HEIGHT; y < blockY * BLOCK_COUNT_HEIGHT + BLOCK_COUNT_HEIGHT; y++) {
                    for (int x = blockX * BLOCK_COUNT_WIDTH; x < blockX * BLOCK_COUNT_WIDTH + BLOCK_COUNT_WIDTH; x++) {
                        if (x >= width)
                            continue;
                        if (y >= height)
                            continue;
                        int correctY = height - y - 1;

                        if (map.getTiles()[x][y] == 1)
                            cache.add(tile, x, correctY, 1, 1);
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }
    }

    public void render(float delta) {
        processButtons();

        camVelocity.scl(delta);
        camVelocity.x *= CAM_VELOCITY;
        camVelocity.y *= CAM_VELOCITY;

        camera.position.add(camVelocity.x, camVelocity.y, 0);
        camera.update();

        cache.setProjectionMatrix(camera.combined);
        cache.begin();

        for (int j = 0; j < blocks[0].length; j++) {
            for (int[] block : blocks) {
                cache.draw(block[j]);
            }
        }
        cache.end();

        debugRenderer.render(world, camera.combined);
        world.step(1/45f, 6, 2);
    }

    public void moveCam() {
        camera.position.add(0.1f, 0.1f, 0);
    }

    private void processButtons() {
        camVelocity.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            camVelocity.add(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            camVelocity.add(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camVelocity.add(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camVelocity.add(0, -1);

        float maxVelocity = 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.W) && body.getLinearVelocity().y < maxVelocity)
            body.applyLinearImpulse(0, 0.5f, body.getPosition().x, body.getPosition().y, true);
    }
}
