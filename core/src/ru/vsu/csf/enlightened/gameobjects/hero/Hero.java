package ru.vsu.csf.enlightened.gameobjects.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.AttackTemplate;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.controlling.attacking.projectile.Projectiles;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.renderers.MapRenderer;

import java.util.ArrayList;

public class Hero {

    private static final float JUMP_HEIGHT = 6;
    private static final float RUN_SPEED = 0.7f;
    private static final float MAX_VELOCITY = 6f;
    private static final float MASS = 0.7f;
    private static final float HERO_SIZE = 0.4f;
    private static final float PROJECTILE_SPAWN_DISTANCE = 0.8f;
    public  static final float PROJECTILE_SPEED = 0.25f;
    private static final float STANDARD_ATTACK_DURATION = 0.2f;

    private World world;

    private Body body;
    private Facing facing;
    private Body sensor;
    private boolean isGrounded;

    private Body shield;
    private boolean shieldUp;

    private CurrentAttack currentAttack;
    //private ArrayList<Body> flyingProjectiles;
    private Projectiles projectiles;

    private int hp;

    public Body getBody() {
        return body;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setIsGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
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

    public int getHp() {
        return hp;
    }

    public Projectiles getProjectiles() {
        return projectiles;
    }

    public Hero(World world) {
        this.world = world;

        shieldUp = false;
        facing = Facing.RIGHT;
        hp = 100;
        projectiles = new Projectiles(world);

        BodyDef bodyDef = new BodyDef()
        {{
                type = BodyType.DynamicBody;
                position.set(1.5f, 3.5f);
            }};

        body = world.createBody(bodyDef);

        final PolygonShape polygonShape = new PolygonShape(){{
            setAsBox(HERO_SIZE, HERO_SIZE);
        }};

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = polygonShape;
            density = MASS;
            friction = 0.2f;
            restitution = 0.0f;
            filter.categoryBits = EntityTypes.HERO;
            filter.maskBits = EntityTypes.HERO_MASK;
        }};
        body.createFixture(fixtureDef);

        body.setFixedRotation(true);
        polygonShape.dispose();

        body.setUserData(this);

        createSensor();
        createShield();
    }

    private void createSensor() {
        BodyDef bodyDef = new BodyDef() {{
            type = BodyType.DynamicBody;
            position.set(body.getPosition().x, body.getPosition().y - 0.45f);
        }
        };

        sensor = world.createBody(bodyDef);

        final PolygonShape poly = new PolygonShape() {{
            setAsBox(0.3f, 0.05f);
        }
        };

        FixtureDef fixtureDef = new FixtureDef() {{
            shape = poly;
            filter.categoryBits = EntityTypes.JUMP_SENSOR;
            filter.maskBits = EntityTypes.JUMP_SENSOR_MASK;
            isSensor = true;
        }
        };

        sensor.createFixture(fixtureDef);
        sensor.setFixedRotation(true);
        sensor.setUserData(this);

        poly.dispose();
    }

    private void createShield() {
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
            filter.categoryBits = EntityTypes.SWORD;
            filter.maskBits = EntityTypes.SWORD_MASK;
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


    public void update(float delta) {
        if (currentAttack != null) {
            currentAttack.duration -= delta;
            if (currentAttack.duration < 0) {
                world.destroyBody(currentAttack.body);
                currentAttack = null;
            }
        }

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
        updateSensor();
        projectiles.update();
    }



    private void updateSensor() {
        sensor.setTransform(body.getPosition().x, body.getPosition().y - 0.45f, 0);
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

    public void attack(int index) {
        if (currentAttack != null)
            return;

        final AttackTemplate template = Attacks.getAttacks().get(index).get(facing);

        BodyDef def = new BodyDef() {{
            type = BodyType.KinematicBody;
            position.set(template.bodyDef.position.x + body.getPosition().x, template.bodyDef.position.y + body.getPosition().y);
        }
        };

        Body atk = world.createBody(def);
        atk.createFixture(template.fixtureDef);

        currentAttack = new CurrentAttack(index, facing, atk, STANDARD_ATTACK_DURATION);

        atk.setUserData(currentAttack);
    }

    public void throwProjectile() {
        Vector3 mouse = MapRenderer.getRenderer().getMousePos();

        final Vector2 spawnPosition = new Vector2(mouse.x, mouse.y);
        spawnPosition.sub(body.getPosition()).nor().scl(PROJECTILE_SPAWN_DISTANCE).add(body.getPosition());

        final Vector2 rotation = (new Vector2(mouse.x, mouse.y)).sub(body.getPosition());
        float angle = (float) Math.atan2(rotation.y, rotation.x);

        BodyDef projectileBody = new BodyDef() {{
            position.set(spawnPosition);
            type = BodyType.DynamicBody;
        }
        };

        FixtureDef projectileFixtureDef = Attacks.getProjectile();

        projectiles.addNew(projectileBody, projectileFixtureDef, angle);
    }
}