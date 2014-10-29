package ru.vsu.csf.enlightened.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import ru.vsu.csf.enlightened.ShapeSlasher;
import ru.vsu.csf.enlightened.controlling.ComboTree;
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
        map.loadLevel("levels/1.ssl");
        mapRenderer = MapRenderer.getRenderer();
        mapRenderer.init(map);

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    map.keyDown(Input.Keys.F);
                }
                else if (button == Input.Buttons.RIGHT) {
                    map.keyDown(ComboTree.THROW_PROJECTILE_BUTTON_CODE);
                }
                return true;
            }

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

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                //screenY = ShapeSlasher.HEIGHT - screenY;
                mapRenderer.updateMousePosition(screenX, screenY);
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