package ru.vsu.csf.enlightened.gameobjects.enemies.ai;

import com.badlogic.gdx.physics.box2d.Body;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;

public class GroundSensor {

    private Body body;
    private Dummy parent;

    public GroundSensor(Body body, Dummy parent) {
        this.body = body;
        this.parent = parent;
    }

    public Body getBody() {
        return body;
    }

    public Dummy getParent() {
        return parent;
    }
}
