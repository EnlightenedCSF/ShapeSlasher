package ru.vsu.csf.enlightened.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Hero {

    private static final float JUMP_HEIGHT = 6f;
    private static final float RUN_SPEED = 0.7f;
    private static final float MAX_VELOCITY = 5f;
    private static final float MASS = 0.78f;

    private Body body;

    private int numContacts;

    public Body getBody() {
        return body;
    }

    public boolean isGrounded() {
        return numContacts > 0;
    }

    public void incNumContacts() {
        this.numContacts++;
        body.setLinearVelocity(new Vector2(0, 0));
    }

    public void decNumContacts() {
        this.numContacts--;
    }

    public Hero(World world) {
        numContacts = 0;

        BodyDef bodyDef = new BodyDef()
        {{
                type = BodyType.DynamicBody;
                position.set(1, 5);
            }};

        body = world.createBody(bodyDef);

        final CircleShape circleShape = new CircleShape(){{
            setRadius(0.5f);
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = circleShape;
            density = MASS;
            friction = 0.2f;
            restitution = 0.2f;
        }};

        body.createFixture(fixtureDef);
        circleShape.dispose();

        body.setUserData(this);
    }

    public void update() {
        Vector2 vel = body.getLinearVelocity();

        if (Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            body.setLinearVelocity(vel);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.setLinearVelocity(vel.x * 0.9f, vel.y);
        }

        if (!isGrounded()) {
            body.getFixtureList().get(0).setFriction(0);
        }
        else {
            if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
                body.getFixtureList().get(0).setFriction(100f);
            }
            else {
                body.getFixtureList().get(0).setFriction(0.2f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY) {
            body.applyLinearImpulse(-RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY) {
            body.applyLinearImpulse(RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && isGrounded()) {
            body.setLinearVelocity(vel.x, 0);
            body.setTransform(body.getPosition().x, body.getPosition().y + 0.01f, 0);
            body.applyLinearImpulse(0, JUMP_HEIGHT * body.getMass(), body.getPosition().x, body.getPosition().y, true);
        }
    }
}
