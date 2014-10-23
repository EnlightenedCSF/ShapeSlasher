package ru.vsu.csf.enlightened.gameobjects.enemies;

import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.gameobjects.Map;

public class Dummy {

    private Body body;
    private World world;

    public Dummy(World world, float x, float y) {
        this.world = world;

        createBody(x, y);
    }

    private void createBody(final float x, final float y) {
        body = world.createBody(new BodyDef() {{
            type = BodyType.DynamicBody;
            position.set(x, y);
        }});

        final PolygonShape poly = new PolygonShape() {{
            setAsBox(0.5f, 0.5f);
        }};

        body.createFixture(new FixtureDef() {{
            shape = poly;
            friction = 100f;
            restitution = 0;
            density = 0.9f;
            filter.categoryBits = Map.ENEMY_CATEGORY;
            filter.maskBits = Map.ENEMY_MASK;
        }});

        poly.dispose();

        body.setFixedRotation(true);
    }
}
