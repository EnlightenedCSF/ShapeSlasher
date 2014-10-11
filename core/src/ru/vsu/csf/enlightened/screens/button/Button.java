package ru.vsu.csf.enlightened.screens.button;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.vsu.csf.enlightened.screens.button.pressaction.PressBehaviour;

public abstract class Button {

    protected boolean isHovered;
    protected Sprite normal;
    protected Sprite hovered;

    private PressBehaviour action;

    public Button() {
        isHovered = false;
        action = null;
    }

    public void render(Batch batch) {
        if (isHovered)
            hovered.draw(batch);
        else
            normal.draw(batch);
    }

    public void mouseHover(int x, int y) {
        isHovered = normal.getBoundingRectangle().contains(x, y);
    }

    public void setAction(PressBehaviour action) {
        this.action = action;
    }

    public void click(int x, int y) {
        if (hovered.getBoundingRectangle().contains(x, y))
            action.execute();
    }
}
