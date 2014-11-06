package ru.vsu.csf.enlightened.gameobjects.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.AttackTemplate;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.controlling.attacking.EnemyAttack;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.BehaviourStates;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.GroundSensor;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.ObstacleSensor;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.VisibleArea;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

public class Dummy {

    private static final float JUMP_HEIGHT = 6;
    private static final float PATROL_DELAY_TIME = 3;
    private static final float WARNED_TIME = 2;

    private static final float RUN_SPEED = 0.45f;
    private static final float MAX_VELOCITY = 4f;

    private final float BODY_SIZE        = 0.5f;
    private final float VISIBLE_DISTANCE = 3f;
    private final int FLEEING_HP_THRESHOLD = 25;

    private static final float STANDARD_ATTACK_DURATION = 0.2f;
    private static final float ATTACK_DELAY = 0.65f;

    private World world;
    private Body body;

    private VisibleArea visibleArea;
    private ObstacleSensor obstacleSensor;
    private GroundSensor groundSensor;

    private boolean isGrounded;
    private boolean isJumpingSideWay = false;

    private boolean seesObstacle;
    private boolean isMoving;
    private Facing facing;

    private int hp;
    private BehaviourStates state;

    private boolean isWalkingToB;
    private Vector2 pointA;
    private Vector2 pointB;
    private float patrolDelayTime;

    private boolean seesHero = false;
    private float warnedTime;
    private Hero hero;

    private EnemyAttack enemyAttack;
    private float attackDelay;

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
        if (isGrounded)
            isJumpingSideWay = false;
    }

    public void setSeesObstacle(boolean seesObstacle) {
        this.seesObstacle = seesObstacle;
    }

    public int getHp() {
        return hp;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Конструктор
     * @param world Ссылка на игровой мир
     * @param x Координата, где должен появиться моб
     * @param y Координата, где должен появиться моб
     */
    public Dummy(World world, float x, float y) {
        this.world = world;
        this.hp = 100;
        this.isGrounded = false;
        this.setSeesObstacle(false);
        this.isMoving = false;

        this.patrolDelayTime = 0;
        this.hero = null;
        this.enemyAttack = null;

        createBody(x, y);

        facing = Facing.LEFT;
        state = BehaviourStates.PATROL;

        isWalkingToB = false;
        pointA = new Vector2(body.getPosition().x - 2.5f, body.getPosition().y);
        pointB = new Vector2(body.getPosition().x + 0.5f, body.getPosition().y);

        attackDelay = 0;
    }

    public boolean decreaseHP(int damage) {
        hp -= damage;
        if (hp <= FLEEING_HP_THRESHOLD) {
            state = BehaviourStates.FLEE;
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
        createGroundSensor();
    }


    private void createGroundSensor() {
        groundSensor = new GroundSensor(world.createBody(new BodyDef() {{
            position.set(body.getPosition().x, body.getPosition().y - 0.6f);
            type = BodyType.DynamicBody;
        }
        }), this);

        final  PolygonShape polygonShape = new PolygonShape() {{
            setAsBox(0.4f, 0.05f);
        }
        };

        groundSensor.getBody().createFixture(new FixtureDef() {{
            shape = polygonShape;
            isSensor = true;
            filter.categoryBits = EntityTypes.JUMP_SENSOR;
            filter.maskBits = EntityTypes.JUMP_SENSOR_MASK;
        }
        });

        polygonShape.dispose();

        groundSensor.getBody().setUserData(groundSensor);
    }

    private void createAntiObstacleSensor() {
        obstacleSensor = new ObstacleSensor(world.createBody(new BodyDef() {{
            position.set(facing == Facing.LEFT ? new Vector2(body.getPosition().x - (BODY_SIZE + 0.2f), body.getPosition().y) :
                    new Vector2(body.getPosition().x + (BODY_SIZE + 0.2f), body.getPosition().y));
            type = BodyType.DynamicBody;
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
                filter.categoryBits = EntityTypes.JUMP_SENSOR;
                filter.maskBits = EntityTypes.JUMP_SENSOR_MASK;
            }
        });

        poly.dispose();

        obstacleSensor.getBody().setUserData(obstacleSensor);
    }

    private void createAntiHeroVisionSensor() {
        visibleArea = new VisibleArea(world.createBody(new BodyDef() {{
            position.set(facing == Facing.LEFT ? new Vector2(body.getPosition().x - (BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y) :
                    new Vector2(body.getPosition().x + (BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y));
            type = BodyType.DynamicBody;
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

    public void update(float delta) {
        if (enemyAttack != null) {
            enemyAttack.duration -= delta;
            if (enemyAttack.duration < 0) {
                world.destroyBody(enemyAttack.body);
                enemyAttack = null;
            }
        }

        updatePhysics();

        switch (state) {
            case PATROL:
                if (isWalkingToB) {
                    if (getXDist(body.getPosition(), pointB) < 0.1) {
                        isWalkingToB = false;
                        patrolDelayTime = PATROL_DELAY_TIME;
                    }
                }
                else if (getXDist(body.getPosition(), pointA) < 0.1) {
                    isWalkingToB = true;
                    patrolDelayTime = PATROL_DELAY_TIME;
                }

                if (isGrounded) {
                    if (patrolDelayTime > 0) {
                        patrolDelayTime -= delta;
                    } else {
                        if (!isWalkingToB) {      //to A
                            walkToPoint(pointA);
                        } else {
                            walkToPoint(pointB);
                        }
                    }
                }
                break;
            case ATTACK:

                if (seesHero) {

                    float targetX;
                    if (hero.getBody().getPosition().x - body.getPosition().x > 0) {
                        targetX = hero.getPosition().x - 1.2f;
                        facing = Facing.RIGHT;
                    }
                    else {
                        targetX = hero.getPosition().x + 1.2f;
                        facing = Facing.LEFT;
                    }

                    Vector2 target = new Vector2(targetX, hero.getPosition().y);
                    walkToPoint(target);

                    if (getXDist(body.getPosition(), target) < 0.05f) {
                        if (attackDelay >= 0)
                            attackDelay -= delta;
                        if (attackDelay < 0)
                            attack();
                    }

                    //Gdx.app.log("enemy", "Sees hero: " + seesHero + "; Attack delay: " + attackDelay + "");
                }
                else {
                    warnedTime -= delta;

                    if (warnedTime < 0) {
                        warnedTime = 0;
                        state = BehaviourStates.PATROL;
                    }
                }
                break;
            case FLEE:

                break;
            case STAND_STILL:
            default:
                break;
        }

        updateSensors();
    }

    private void updatePhysics() {
        Vector2 vel = body.getLinearVelocity();

        if (Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            body.setLinearVelocity(vel);
        }

        if (!isMoving)
            body.setLinearVelocity(vel.x * 0.7f, vel.y);

        if (!isGrounded()) {
            body.getFixtureList().get(0).setFriction(0);
        }
        else {
            if (!isMoving) {
                body.getFixtureList().get(0).setFriction(100f);
            }
            else {
                body.getFixtureList().get(0).setFriction(0.0f);
            }
        }

        if (!isGrounded) {
            if (isJumpingSideWay) {
                if (facing == Facing.LEFT)
                    stepLeft();
                else
                    stepRight();
            }
        }
    }

    private void stepLeft() {
        facing = Facing.LEFT;

        body.applyLinearImpulse(-RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
        body.setTransform(body.getPosition().x, body.getPosition().y, Facing.getAngle(facing));
    }

    private void stepRight() {
        facing = Facing.RIGHT;

        body.applyLinearImpulse(RUN_SPEED, 0, body.getPosition().x, body.getPosition().y, true);
        body.setTransform(body.getPosition().x, body.getPosition().y, Facing.getAngle(facing));
    }

    private void jump() {
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.setTransform(body.getPosition().x, body.getPosition().y + 0.01f, Facing.getAngle(facing));
        body.applyLinearImpulse(0, JUMP_HEIGHT * body.getMass(), body.getPosition().x, body.getPosition().y, true);
    }

    private void jumpLeft() {
        jump();
        isJumpingSideWay = true;
        stepLeft();
    }

    private void jumpRight() {
        jump();
        isJumpingSideWay = true;
        stepRight();
    }

    private void walkToPoint(Vector2 point) {
        if (Math.abs(body.getPosition().x - point.x) < 0.05) {
            return;
        }

        if (point.x - body.getPosition().x > 0) { //target is on the right side
            if (!seesObstacle)
                stepRight();
            else
                jumpRight();
        }
        else {                                   // target is on the left side
            if (!seesObstacle)
                stepLeft();
            else
                jumpLeft();
        }
    }

    private void attack() {
        if (enemyAttack != null)
            return;

        final AttackTemplate template = Attacks.getAttacks().get(1).get(facing);

        BodyDef def = new BodyDef() {{
            type = BodyType.KinematicBody;
            position.set(template.bodyDef.position.x + body.getPosition().x, template.bodyDef.position.y + body.getPosition().y);
        }
        };

        Body atk = world.createBody(def);
        atk.createFixture(new FixtureDef() {{
            shape = template.fixtureDef.shape;
            filter.categoryBits = EntityTypes.ENEMY_SWORD;
            filter.categoryBits = EntityTypes.ENEMY_SWORD_MASK;
            isSensor = true;
         }
        });

        enemyAttack = new EnemyAttack(1, facing, atk, STANDARD_ATTACK_DURATION);

        atk.setUserData(this.enemyAttack);

        this.attackDelay = ATTACK_DELAY;
    }

    private float getXDist(Vector2 from, Vector2 to) {
        return Math.abs(from.x - to.x);
    }

    private void updateSensors() {
        groundSensor.getBody().setTransform(body.getPosition().x, body.getPosition().y - 0.6f, 0);

        switch (facing) {
            case RIGHT:
                obstacleSensor.getBody().setTransform(body.getPosition().x + (BODY_SIZE + 0.2f), body.getPosition().y, 0);
                break;
            case LEFT:
                obstacleSensor.getBody().setTransform(body.getPosition().x - (BODY_SIZE + 0.2f), body.getPosition().y, 0);
                break;
        }

        //Gdx.app.log("Enemy", "State: " + state + "; Sees hero: " + seesHero + "; Body: " + body.getPosition() + "; Sensor: " + visibleArea.getBody().getPosition());

        if (seesHero && state == BehaviourStates.ATTACK) {
            Vector2 pos = body.getPosition().sub(hero.getPosition());
            float angle = (float) Math.atan2(pos.y, pos.x);

            Vector2 sensorCenter = new Vector2((float) (VISIBLE_DISTANCE / 2.0 * Math.cos(angle)), (float) (VISIBLE_DISTANCE / 2.0 * Math.sin(angle))).scl(1.8f);
            pos = body.getPosition().sub(sensorCenter);

            visibleArea.getBody().setTransform(pos.x, pos.y, angle);
        }
        else {
            switch (facing) {
                case RIGHT:
                    visibleArea.getBody().setTransform(body.getPosition().x + (2*BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y, 0);
                    break;
                case LEFT:
                    visibleArea.getBody().setTransform(body.getPosition().x - (2*BODY_SIZE + VISIBLE_DISTANCE/2.0f), body.getPosition().y, 0);
                    break;
            }
        }
    }

    public void seeHero(Hero hero) {
        this.hero = hero;
        seesHero = true;
        warnedTime = 0;

        switch (state) {
            case STAND_STILL:
            case PATROL:
                if (hp > FLEEING_HP_THRESHOLD)
                    state = BehaviourStates.ATTACK;
                else
                    state = BehaviourStates.FLEE;
                break;
        }
    }

    public void unseeHero() {
        seesHero = false;

        if (state == BehaviourStates.ATTACK) {
            warnedTime = WARNED_TIME;
        }
    }
}
