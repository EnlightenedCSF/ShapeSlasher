package ru.vsu.csf.enlightened.gameobjects.enemies.ai;

import com.badlogic.gdx.physics.box2d.Body;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;

public class VisibleArea {

    private Body body;
    private Dummy parent;

    public VisibleArea(Body body, Dummy parent) {
        this.body = body;
        this.parent = parent;
        this.body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    public Dummy getParent() {
        return parent;
    }

    public void setParent(Dummy parent) {
        this.parent = parent;
    }
}
