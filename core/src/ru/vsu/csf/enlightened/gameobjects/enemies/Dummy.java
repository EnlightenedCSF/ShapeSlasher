package ru.vsu.csf.enlightened.gameobjects.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.AttackTemplate;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.BehaviourStates;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.ObstacleSensor;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.VisibleArea;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

public class Dummy {

    private final float BODY_SIZE        = 0.5f;
    private final float VISIBLE_DISTANCE = 3f;
    private final int FLEEING_HP_THRESHOLD = 25;

    private World world;
    private Body body;

    private VisibleArea visibleArea;
    private ObstacleSensor obstacleSensor;

    private Facing facing;

    private int hp;
    private BehaviourStates state;

    private boolean isWalkingToB;
    private Vector2 pointA;
    private Vector2 pointB;

    private Vector2 playerPos;

    public int getHp() {
        return hp;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Dummy(World world, float x, float y) {
        this.world = world;
        this.hp = 100;

        createBody(x, y);
    }

    public boolean decreaseHP(int damage) {
        hp -= damage;
        if (hp <= FLEEING_HP_THRESHOLD) {
            state = BehaviourStates.FLEE;
            Gdx.app.log("enemy", "I'm outta here!");
        }
        return this.hp <= 0;
    }

    private void createBody(final float x, final float y) {
        body = world.createBody(new BodyDef() {{
            type = BodyType.DynamicBody;
            position.set(x, y);
        }});

        final PolygonShape poly = new PolygonShape() {{
            setAsBox(BODY_SIZE, BODY_SIZE);
        }};

        body.createFixture(new FixtureDef() {{
            shape = poly;
            friction = 50f;
            restitution = 0.1f;
            density = 0.8f;
            filter.categoryBits = EntityTypes.ENEMY;
            filter.maskBits = EntityTypes.ENEMY_MASK;
        }});

        poly.dispose();

        body.setUserData(this);
        body.setFixedRotation(true);

        createAntiHeroVisionSensor();
        createAntiObstacleSensor();


        facing = Facing.LEFT;
        state = BehaviourStates.STAND_STILL;
    }

    private void createAntiHeroVisionSensor() {
        visibleArea = new VisibleArea(world.createBody(new BodyDef() {{
            position.set(facing == Facing.LEFT ? new Vector2(body.getPosition().x - (BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y) :
                    new Vector2(body.getPosition().x + (BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y));
            type = BodyType.KinematicBody;
        }
        }), this);

        final PolygonShape polyShape = new PolygonShape() {{
            setAsBox(VISIBLE_DISTANCE, 0.6f);
        }
        };

        visibleArea.getBody().createFixture(new FixtureDef() {{
            shape = polyShape;
            isSensor = true;
            filter.categoryBits = EntityTypes.ENEMY_VISION_S;
            filter.maskBits = EntityTypes.ENEMY_VISION_M;
        }
        });

        polyShape.dispose();
    }


    private void createAntiObstacleSensor() {
        obstacleSensor = new ObstacleSensor(world.createBody(new BodyDef() {{
            position.set(facing == Facing.LEFT ? new Vector2(body.getPosition().x - (BODY_SIZE + 0.2f), body.getPosition().y) :
                                                 new Vector2(body.getPosition().x + (BODY_SIZE + 0.2f), body.getPosition().y));
            type = BodyType.KinematicBody;
        }
        }), this);

        final PolygonShape poly = new PolygonShape() {{
            setAsBox(0.2f, 0.37f);
        }
        };

        obstacleSensor.getBody().createFixture(new FixtureDef() {
            {
                shape = poly;
                isSensor = true;
                filter.categoryBits = EntityTypes.ENEMY_OBSTACLE_S;
                filter.maskBits = EntityTypes.ENEMY_OBSTACLE_M;
            }
        });

        poly.dispose();
    }

    public void beAttacked(CurrentAttack attack) {

        AttackTemplate template = Attacks.getAttacks().get(attack.index).get(attack.facing);

        int damage = template.damage;
        this.decreaseHP(damage);

        Vector2 direction = new Vector2(template.knockbackDirection).nor().scl(template.knockbackPower);
        Vector2 position = body.getPosition();

        body.getPosition().set(position.x, position.y + 0.05f);
        body.applyLinearImpulse(direction.x, direction.y, position.x, position.y, true);
    }

    public void bePierced(Body knife) {
        this.decreaseHP(Attacks.PROJECTILE_DAMAGE);

        Vector2 direction = new Vector2((float)Math.cos(knife.getAngle()), (float)Math.sin(knife.getAngle())).scl(Attacks.PROJECTILE_KNOCKBACK);
        Vector2 position = body.getPosition();

        body.getPosition().set(position.x, position.y + 0.05f);
        body.applyLinearImpulse(direction.x, direction.y, position.x, position.y, true);
    }

    public void update() {
        switch (state) {
            case PATROL:

                break;
            case ATTACK:

                break;
            case FLEE:

                break;
            case STAND_STILL:
            default:
                break;
        }

        updateSensors();
    }

    private void updateSensors() {
        switch (facing) {
            case RIGHT:
                visibleArea.getBody().setTransform(body.getPosition().x + (2*BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y, 0);
                obstacleSensor.getBody().setTransform(body.getPosition().x + (BODY_SIZE + 0.2f), body.getPosition().y, 0);
                break;
            case LEFT:
                visibleArea.getBody().setTransform(body.getPosition().x - (2*BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y, 0);
                obstacleSensor.getBody().setTransform(body.getPosition().x - (BODY_SIZE + 0.2f), body.getPosition().y, 0);
                break;
        }
    }

    public void seeHero(Hero hero) {
        switch (state) {
            case STAND_STILL:
            case PATROL:
                if (hp > FLEEING_HP_THRESHOLD) {
                    state = BehaviourStates.ATTACK;
                    playerPos = hero.getPosition();
                    Gdx.app.log("enemy", "Switch to Attack mode!");
                }
                else {
                    state = BehaviourStates.FLEE;
                    Gdx.app.log("enemy", "Switch to Fleeing mode!");
                }
                break;
        }
    }
}
