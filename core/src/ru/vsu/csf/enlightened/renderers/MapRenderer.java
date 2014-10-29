package ru.vsu.csf.enlightened.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ru.vsu.csf.enlightened.ShapeSlasher;
import ru.vsu.csf.enlightened.gameobjects.Map;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;

import java.util.Arrays;

public class MapRenderer {

    public static final int BLOCK_COUNT_WIDTH = 12;
    public static final int BLOCK_COUNT_HEIGHT = 7;
    public static final int BLOCK_WIDTH = 70;
    public static final int BLOCK_HEIGHT = 70;
    private static final boolean IS_DRAWING_GRAPHICS = true;

    private Map map;

    private Vector3 mousePos;

    private OrthographicCamera camera;
    private Vector3 lerpVector;

    private SpriteBatch batch;
    private SpriteBatch skyBatch;

    private TextureRegion sky;
    private TextureRegion tile;
    private Sprite hero;

    private SpriteCache cache;
    private int[][] blocks;

    private Box2DDebugRenderer debugRenderer;
    private BitmapFont debugFont;

    private static MapRenderer instance;
    private MapRenderer() {

    }
    public static MapRenderer getRenderer() {
        if (instance == null)
            instance = new MapRenderer();

        return instance;
    }

    public void init(Map map) {
        this.mousePos = new Vector3();

        this.batch = new SpriteBatch();
        this.skyBatch = new SpriteBatch(10) {{
            getProjectionMatrix().setToOrtho2D(0, 0, ShapeSlasher.WIDTH, ShapeSlasher.HEIGHT);
        }
        };

        this.debugRenderer = new Box2DDebugRenderer();
        this.debugFont = new BitmapFont() {{
            setColor(Color.YELLOW);
        }
        };

        this.map = map;

        this.camera = new OrthographicCamera(BLOCK_COUNT_WIDTH, BLOCK_COUNT_HEIGHT);
        this.camera.position.set(0, 2, 0);
        lerpVector = new Vector3(0, 0, 0);

        this.cache = new SpriteCache(map.getTiles().length * map.getTiles()[0].length, false);
        this.blocks = new int[(int) Math.ceil(map.getTiles().length / (float) BLOCK_COUNT_WIDTH)][(int) Math.ceil(map.getTiles()[0].length / (float) BLOCK_COUNT_HEIGHT)];

        this.tile = new TextureRegion(new Texture(Gdx.files.internal("assets/tiles/ground2.png")), 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);
        this.sky = new TextureRegion(new Texture(Gdx.files.internal("assets/backgrounds/sky2.png")));
        this.hero = new Sprite(new Texture(Gdx.files.internal("assets/characters/hero.png")));

        createBlocks();
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


    private Vector2 getPositionOfObject(Vector2 pos) {
        Vector2 center = new Vector2(pos.x, pos.y);
        center.sub(camera.position.x + 0.2f, camera.position.y);

        return new Vector2( ShapeSlasher.WIDTH/2.0f  + center.x / BLOCK_COUNT_WIDTH  * ShapeSlasher.WIDTH,
                            ShapeSlasher.HEIGHT/2.0f + center.y / BLOCK_COUNT_HEIGHT * ShapeSlasher.HEIGHT );
    }


    public void render(float delta) {
        map.update(delta);

        hero.setPosition(map.getHero().getBody().getPosition().x, map.getHero().getBody().getPosition().y);

        camera.position.lerp(lerpVector.set(map.getHero().getBody().getPosition().x, map.getHero().getBody().getPosition().y + 0.5f, 0), 2 * delta);
        camera.update();

        if (IS_DRAWING_GRAPHICS) {
            /*skyBatch.begin();
            skyBatch.draw(sky, 0, 0, ShapeSlasher.WIDTH, ShapeSlasher.HEIGHT);
            skyBatch.end();

            cache.setProjectionMatrix(camera.combined);
            Gdx.gl.glDisable(GL20.GL_BLEND);
            cache.begin();

            for (int j = 0; j < blocks[0].length; j++) {
                for (int[] block : blocks) {
                    cache.draw(block[j]);
                }
            }

            cache.end();*/




            skyBatch.begin();

            Vector2 coords = getPositionOfObject(new Vector2(hero.getX(), hero.getY()));
            debugFont.draw(skyBatch, String.valueOf(map.getHero().getHp()), coords.x, coords.y);

            for (Dummy enemy : map.getEnemies()) {
                coords = getPositionOfObject(enemy.getPosition());
                debugFont.draw(skyBatch, String.valueOf(enemy.getHp()), coords.x, coords.y);
            }

            skyBatch.end();

            /*batch.setProjectionMatrix(camera.combined);
            batch.begin();


            *//*Vector2 coords = new Vector2(hero.getX(), hero.getY());
            Vector3 result = screenCamera.unproject(new Vector3(coords.x, coords.y, 0));

            debugFont.draw(batch, String.valueOf(map.getHero().getHp()), result.x, result.y);
*//*
            batch.end();*/
        }

        debugRenderer.render(map.getWorld(), camera.combined);
        map.getWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);

        map.getHero().getProjectiles().clearCollided();

        //Gdx.app.log("Cam", String.valueOf(camera.position));
    }

    public void updateMousePosition(int screenX, int screenY) {
        Vector3 coords = new Vector3(screenX, screenY, 0);
        mousePos = camera.unproject(coords);
    }

    public Vector3 getMousePos() {
        return mousePos;
    }
}
