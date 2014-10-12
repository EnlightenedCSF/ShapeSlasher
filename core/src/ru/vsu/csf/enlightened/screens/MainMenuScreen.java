package ru.vsu.csf.enlightened.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.vsu.csf.enlightened.ShapeSlasher;
import ru.vsu.csf.enlightened.screens.button.Button;
import ru.vsu.csf.enlightened.screens.button.MenuButton;
import ru.vsu.csf.enlightened.screens.button.pressaction.PressBehaviour;

public class MainMenuScreen extends SlasherScreen{

    public static final float WIDTH_CENTER  = Gdx.graphics.getWidth() / 2;
    public static final float TOP_OFFSET    = 350;
    public static final float MARGIN        = 70;

    private TextureRegion r;

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        r = new TextureRegion(new Texture(Gdx.files.internal("assets/menuBtns/start.png")));

        buttons.add(new MenuButton("start", 0) {{
            setAction(new PressBehaviour() {
                @Override
                public void execute() {
                    game.setScreen(new GameScreen(game));
                }
            });}
        });

        buttons.add(new MenuButton("exit", 1) {{
            setAction(new PressBehaviour() {
                @Override
                public void execute() {
                    Gdx.app.exit();
                }
            });}
        });

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                for (Button button : buttons)
                    button.mouseHover(screenX, ShapeSlasher.HEIGHT - screenY);
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                for (Button button1 : buttons)
                    button1.click(screenX, ShapeSlasher.HEIGHT - screenY);
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        for (Button button : buttons)
            button.render(batch);

        batch.draw(r, 20, 20);

        batch.end();


    }
}
