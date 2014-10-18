package ru.vsu.csf.enlightened.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import ru.vsu.csf.enlightened.gameobjects.Map;

public class MapRenderer {

    public static final int BLOCK_COUNT_WIDTH = 12;
    public static final int BLOCK_COUNT_HEIGHT = 7;
    public static final int BLOCK_WIDTH = 70;
    public static final int BLOCK_HEIGHT = 70;

    private Map map;

    private OrthographicCamera camera;
    private Vector3 lerpVector;

    private SpriteBatch batch;
    private TextureRegion tile;
    private Sprite hero;

    private SpriteCache cache;
    private int[][] blocks;

    private Box2DDebugRenderer debugRenderer;

    public MapRenderer(Map map) {
        this.batch = new SpriteBatch();

        this.debugRenderer = new Box2DDebugRenderer();

        this.map = map;
        this.camera = new OrthographicCamera(BLOCK_COUNT_WIDTH, BLOCK_COUNT_HEIGHT);
        this.camera.position.set(0, 2, 0);
        lerpVector = new Vector3(0, 0, 0);

        this.cache = new SpriteCache(map.getTiles().length * map.getTiles()[0].length, false);
        this.blocks = new int[(int) Math.ceil(map.getTiles().length / (float) BLOCK_COUNT_WIDTH)][(int) Math.ceil(map.getTiles()[0].length / (float) BLOCK_COUNT_HEIGHT)];

        this.tile = new TextureRegion(new Texture(Gdx.files.internal("assets/tiles/ground.png")), 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);

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

    public void render(float delta) {
        map.update(delta);

        hero.setPosition(map.getHero().getBody().getPosition().x, map.getHero().getBody().getPosition().y);

        camera.position.lerp(lerpVector.set(map.getHero().getBody().getPosition().x, map.getHero().getBody().getPosition().y + 0.5f, 0), 2 * delta);
        camera.update();

        cache.setProjectionMatrix(camera.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        cache.begin();

        for (int j = 0; j < blocks[0].length; j++) {
            for (int[] block : blocks) {
                cache.draw(block[j]);
            }
        }

        cache.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(hero, hero.getX() - 0.5f, hero.getY() - 0.5f, 1, 1);
        batch.end();

        debugRenderer.render(map.getWorld(), camera.combined);
        map.getWorld().step(1 / 45f, 6, 2);
    }
}
