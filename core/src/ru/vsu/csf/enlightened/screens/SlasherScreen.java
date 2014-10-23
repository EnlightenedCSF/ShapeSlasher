package ru.vsu.csf.enlightened.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.vsu.csf.enlightened.ShapeSlasher;
import ru.vsu.csf.enlightened.screens.button.Button;

import java.util.ArrayList;

public abstract class SlasherScreen implements Screen {

    protected Game game;

    protected Batch batch;
    protected ArrayList<Button> buttons;

    public SlasherScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch() {{
            getProjectionMatrix().setToOrtho2D(0, 0, ShapeSlasher.WIDTH, ShapeSlasher.HEIGHT);
        }};
        buttons = new ArrayList<Button>();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
