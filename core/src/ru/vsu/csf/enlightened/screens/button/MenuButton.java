package ru.vsu.csf.enlightened.screens.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.vsu.csf.enlightened.screens.MainMenuScreen;

/** Created by enlightenedcsf on 10.10.14. */
public class MenuButton extends Button {

    public MenuButton(String name, int index) {
        super();

        normal = new Sprite(new Texture(Gdx.files.internal("assets/menuBtns/" + name + ".png")));
        hovered = new Sprite(new Texture(Gdx.files.internal("assets/menuBtns/" + name + "Hovered.png")));

        normal.setPosition(MainMenuScreen.WIDTH_CENTER - normal.getWidth()/2, MainMenuScreen.TOP_OFFSET - index*MainMenuScreen.MARGIN);
        hovered.setPosition(MainMenuScreen.WIDTH_CENTER - normal.getWidth()/2, MainMenuScreen.TOP_OFFSET - index*MainMenuScreen.MARGIN);
    }

    public void render(Batch batch) {
        super.render(batch);
    }
}
