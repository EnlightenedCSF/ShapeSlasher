package ru.vsu.csf.enlightened.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.AttackTemplate;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;

import java.util.concurrent.atomic.AtomicReference;

public class Dummy {

    private Body body;
    private World world;

    private int hp;

    public Dummy(World world, float x, float y) {
        this.world = world;
        this.hp = 100;

        createBody(x, y);
    }

    public boolean decreaseHP(int damage) {
        this.hp -= damage;
        return this.hp <= 0;
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
            friction = 50f;
            restitution = 0.1f;
            density = 0.8f;
            filter.categoryBits = EntityTypes.ENEMY;
            filter.maskBits = EntityTypes.ENEMY_MASK;
        }});

        poly.dispose();

        body.setUserData(this);
        body.setFixedRotation(true);
    }

    public void beAttacked(AtomicReference<CurrentAttack> attack) {
        CurrentAttack currentAttack = attack.get();

        AttackTemplate template = Attacks.getAttacks().get(currentAttack.index).get(currentAttack.facing);

        int damage = template.damage;
        Vector2 direction = new Vector2(template.knockbackDirection).nor().scl(template.knockbackPower);
        Vector2 position = body.getPosition();

        this.decreaseHP(damage);

        //world.destroyBody(currentAttack.body);

        body.getPosition().set(position.x, position.y + 0.05f);
        body.applyLinearImpulse(direction.x, direction.y, position.x, position.y, true);

        //attack.set(null);
    }
}
