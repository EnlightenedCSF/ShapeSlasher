package ru.vsu.csf.enlightened.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import ru.vsu.csf.enlightened.gameobjects.Map;
import ru.vsu.csf.enlightened.renderers.MapRenderer;

public class GameScreen extends SlasherScreen{

    private Map map;
    private MapRenderer mapRenderer;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        map = new Map();
        mapRenderer = new MapRenderer(map);

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean keyDown(int keycode) {
                map.keyDown(keycode);
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                map.keyUp(keycode);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());

        super.render(delta);

        mapRenderer.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}