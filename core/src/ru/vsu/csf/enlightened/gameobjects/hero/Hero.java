package ru.vsu.csf.enlightened.gameobjects.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.gameobjects.Map;

public class Hero {

    private static final float JUMP_HEIGHT = 6;
    private static final float RUN_SPEED = 0.7f;
    private static final float MAX_VELOCITY = 6f;
    private static final float MASS = 0.7f;

    private World world;

    private Body body;
    private Body sword;
    private Body shield;

    private int numContacts;

    private boolean shieldUp;

    private Facing facing;

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

    public boolean isShieldUp() {
        return shieldUp;
    }

    public void setShieldUp(boolean shieldUp) {
        this.shieldUp = shieldUp;
        if (shieldUp)
            raiseShield();
        else
            hideShield();
    }

    public Hero(World world) {
        this.world = world;
        numContacts = 0;
        shieldUp = false;
        facing = Facing.RIGHT;

        BodyDef bodyDef = new BodyDef()
        {{
                type = BodyType.DynamicBody;
                position.set(1.5f, 3.5f);
            }};

        body = world.createBody(bodyDef);

        final PolygonShape polygonShape = new PolygonShape(){{
            setAsBox(0.4f, 0.4f);
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = polygonShape;
            density = MASS;
            friction = 0.2f;
            restitution = 0.0f;
            filter.categoryBits = Map.HERO_CATEGORY;
            filter.maskBits = Map.HERO_MASK;
        }};

        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        polygonShape.dispose();

        body.setUserData(this);

        //createSword();
        createShield();
    }


    public void createShield() {
        BodyDef bodyDef = new BodyDef() {{
            type = BodyType.KinematicBody;
            position.set(body.getWorldCenter().x + 0.6f, body.getWorldCenter().y + 0);
        }};

        shield = world.createBody(bodyDef);

        final PolygonShape polygonShape = new PolygonShape() {{
            setAsBox(0.1f, 0.35f);
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = polygonShape;
            density = 0.1f;
            restitution = 0.8f;
            filter.categoryBits = Map.SWORD_CATEGORY;
            filter.maskBits = Map.SWORD_MASK;
        }};

        shield.createFixture(fixtureDef);
        polygonShape.dispose();
        shield.setFixedRotation(true);
        shield.setActive(false);
    }

    public void raiseShield() {
        shield.setActive(true);
    }

    private void hideShield() {
        shield.setActive(false);
    }

    /*public void createSword() {
        BodyDef bodyDef = new BodyDef() {{
            type = BodyType.DynamicBody;
            position.set(1.2f, 5f);
        }};

        sword = world.createBody(bodyDef);

        final PolygonShape pShape = new PolygonShape() {{
            set(new Vector2[]{new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(0.75f, 0.0f)});
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = pShape;
            density = 0.1f;
            restitution = 0;
            friction = 0.8f;
            filter.categoryBits = Map.SWORD_CATEGORY;
            filter.maskBits = Map.SWORD_MASK;
        }};

        sword.createFixture(fixtureDef);
        sword.setFixedRotation(true);
        pShape.dispose();
    }*/


    public void update() {
        Vector2 vel = body.getLinearVelocity();

        if (Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            body.setLinearVelocity(vel);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.setLinearVelocity(vel.x * 0.7f, vel.y);
        }

        if (!isGrounded()) {
            body.getFixtureList().get(0).setFriction(0);
        }
        else {
            if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
                body.getFixtureList().get(0).setFriction(100f);
            }
            else {
                body.getFixtureList().get(0).setFriction(0.0f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY) {
            facing = Facing.LEFT;

            body.applyLinearImpulse(-RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
            body.setTransform(body.getPosition().x, body.getPosition().y, Facing.getAngle(facing));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY) {
            facing = Facing.RIGHT;

            body.applyLinearImpulse(RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
            body.setTransform(body.getPosition().x, body.getPosition().y, Facing.getAngle(facing));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && isGrounded()) {
            body.setLinearVelocity(vel.x, 0);
            body.setTransform(body.getPosition().x, body.getPosition().y + 0.01f, Facing.getAngle(facing));
            body.applyLinearImpulse(0, JUMP_HEIGHT * body.getMass(), body.getPosition().x, body.getPosition().y, true);
        }

        updateShield();
    }

    private void updateShield() {
        if (isShieldUp()) {
            switch (facing) {
                case RIGHT:
                    shield.setTransform(body.getPosition().x + 0.6f, body.getPosition().y, 0);
                    break;
                case LEFT:
                    shield.setTransform(body.getPosition().x - 0.6f, body.getPosition().y, 0);
                    break;
            }
        }
    }
}
