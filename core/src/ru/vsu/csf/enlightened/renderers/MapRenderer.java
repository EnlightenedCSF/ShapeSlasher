package ru.vsu.csf.enlightened.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.vsu.csf.enlightened.gameobjects.Map;

public class MapRenderer {

    public static final int BLOCK_COUNT_WIDTH = 24;
    public static final int BLOCK_COUNT_HEIGHT = 15;
    public static final int BLOCK_WIDTH = 35;
    public static final int BLOCK_HEIGHT = 36;

    private Map map;
    private OrthographicCamera camera;

    private TextureRegion tile;

    private SpriteCache cache;
    private int[][] blocks;

    public MapRenderer(Map map) {
        this.map = map;
        this.camera = new OrthographicCamera(BLOCK_COUNT_WIDTH, BLOCK_COUNT_HEIGHT);
        this.camera.position.set(1, 1, 0);

        this.cache = new SpriteCache(map.getTiles().length * map.getTiles()[0].length, false);
        this.blocks = new int[(int) Math.ceil(map.getTiles().length / (float) BLOCK_COUNT_WIDTH)][(int) Math.ceil(map.getTiles()[0].length / (float) BLOCK_COUNT_HEIGHT)];

        this.tile = new TextureRegion(new Texture(Gdx.files.internal("assets/tiles/ground.png")), 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);

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
        camera.update();

        cache.setProjectionMatrix(camera.combined);
        cache.begin();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                cache.draw(blocks[i][j]);
            }
        }

        cache.end();

    }

    public void moveCam() {
        camera.position.add(0.1f, 0.1f, 0);
    }
}
