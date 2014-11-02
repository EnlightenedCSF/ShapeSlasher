package ru.vsu.csf.enlightened.gameobjects.collisions;

import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.controlling.attacking.projectile.ProjectileInfo;
import ru.vsu.csf.enlightened.gameobjects.Map;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.GroundSensor;
import ru.vsu.csf.enlightened.gameobjects.enemies.ai.ObstacleSensor;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

import java.util.concurrent.atomic.AtomicReference;

public class HeroCollideListener implements ContactListener{

    private boolean ifHeroTouchesGround(Contact contact, AtomicReference<Hero> hero) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        boolean isSensorA = fixtureA.isSensor();
        boolean isSensorB = fixtureB.isSensor();

        if (! (isSensorA ^ isSensorB)) {
            return false;
        }

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (isSensorA && first.getClass().equals(Hero.class) && (second.getClass().equals(Map.class) || second.getClass().equals(Dummy.class))) {
            hero.set((Hero) first);
            return true;
        }
        else if (second.getClass().equals(Hero.class)&& (first.getClass().equals(Map.class) || first.getClass().equals(Dummy.class))) {
            hero.set((Hero) second);
            return true;
        }
        return false;
    }

    private boolean ifEnemyTouchesGround(Contact contact, AtomicReference<Dummy> enemy) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        boolean isSensorA = fixtureA.isSensor();
        boolean isSensorB = fixtureB.isSensor();

        if (! (isSensorA ^ isSensorB)) {
            return false;
        }

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (isSensorA && first.getClass().equals(GroundSensor.class)) {
            enemy.set(((GroundSensor) first).getParent());
            return true;
        }
        else if (second.getClass().equals(GroundSensor.class)) {
            enemy.set(((GroundSensor) second).getParent());
            return true;
        }
        return false;
    }

    private boolean ifEnemySeesObstacle(Contact contact, AtomicReference<Dummy> enemy) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        boolean isSensorA = fixtureA.isSensor();
        boolean isSensorB = fixtureB.isSensor();

        if (! (isSensorA ^ isSensorB)) {
            return false;
        }

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (isSensorA && first.getClass().equals(ObstacleSensor.class) && second.getClass().equals(Map.class)) {
            enemy.set(((ObstacleSensor) first).getParent());
            return true;
        }
        else if (second.getClass().equals(ObstacleSensor.class) && first.getClass().equals(Map.class)) {
            enemy.set(((ObstacleSensor) second).getParent());
            return true;
        }
        return false;
    }

    private boolean ifKnifePiercesGround(Contact contact, AtomicReference<ProjectileInfo> projectile) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (first.getClass().equals(Map.class) && second.getClass().equals(ProjectileInfo.class)) {
            projectile.set((ProjectileInfo) second);
            return true;
        }
        else if (second.getClass().equals(Map.class) && first.getClass().equals(ProjectileInfo.class)) {
            projectile.set((ProjectileInfo) first);
            return true;
        }
        else
            return false;
    }

    private boolean ifEnemyWasPierced(Contact contact, AtomicReference<Dummy> enemy, AtomicReference<ProjectileInfo> projectile) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (first.getClass().equals(Dummy.class) && second.getClass().equals(ProjectileInfo.class)) {
            enemy.set((Dummy) first);
            projectile.set((ProjectileInfo) second);
            return true;
        }
        else if (second.getClass().equals(Dummy.class) && first.getClass().equals(ProjectileInfo.class)) {
            enemy.set((Dummy) second);
            projectile.set((ProjectileInfo) first);
            return true;
        }
        else
            return false;
    }

    private boolean ifEnemyWasHit(Contact contact, AtomicReference<Dummy> enemy, AtomicReference<CurrentAttack> attack) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (first.getClass().equals(Dummy.class) && second.getClass().equals(CurrentAttack.class)) {
            enemy.set((Dummy) first);
            attack.set((CurrentAttack) second);
            return true;
        }
        else if (second.getClass().equals(Dummy.class) && first.getClass().equals(CurrentAttack.class)) {
            enemy.set((Dummy) second);
            attack.set((CurrentAttack) first);
            return true;
        }
        else
            return false;
    }

    private boolean ifEnemySeesHero(Contact contact, AtomicReference<Dummy> enemy, AtomicReference<Hero> hero) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (first.getClass().equals(Dummy.class) && second.getClass().equals(Hero.class)) {
            enemy.set((Dummy) first);
            hero.set((Hero) second);
            return true;
        }
        else if (second.getClass().equals(Dummy.class) && first.getClass().equals(Hero.class)) {
            enemy.set((Dummy) second);
            hero.set((Hero) first);
            return true;
        }
        else
            return false;
    }

    @Override
    public void beginContact(Contact contact) {
        AtomicReference<Hero> hero = new AtomicReference<Hero>(null);
        AtomicReference<Dummy> enemy = new AtomicReference<Dummy>(null);
        AtomicReference<CurrentAttack> attack = new AtomicReference<CurrentAttack>(null);
        AtomicReference<ProjectileInfo> knife = new AtomicReference<ProjectileInfo>(null);

        if (ifHeroTouchesGround(contact, hero)) {
            hero.get().setIsGrounded(true);
        }
        else if (ifEnemyWasHit(contact, enemy, attack)) {
            enemy.get().beAttacked(attack.get());
        }
        else if (ifEnemyWasPierced(contact, enemy, knife)) {
            enemy.get().bePierced(knife.get().getBody());
            knife.get().getProjectiles().delete(knife.get().getBody());
        }
        else if (ifKnifePiercesGround(contact, knife)) {
            knife.get().getProjectiles().freeze(knife.get().getBody());
        }

        if (ifEnemyTouchesGround(contact, enemy)) {
            enemy.get().setGrounded(true);
        }
        if (ifEnemySeesObstacle(contact, enemy)) {
            enemy.get().setSeesObstacle(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        AtomicReference<Hero> hero = new AtomicReference<Hero>(null);
        AtomicReference<Dummy> enemy = new AtomicReference<Dummy>(null);

        if (ifHeroTouchesGround(contact, hero)) {
            hero.get().setIsGrounded(false);
        }

        if (ifEnemyTouchesGround(contact, enemy)) {
            enemy.get().setGrounded(false);
        }
        if (ifEnemySeesObstacle(contact, enemy)) {
            enemy.get().setSeesObstacle(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
