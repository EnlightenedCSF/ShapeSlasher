package ru.vsu.csf.enlightened.gameobjects.collisions;

import com.badlogic.gdx.physics.box2d.*;
import ru.vsu.csf.enlightened.controlling.attacking.Attacks;
import ru.vsu.csf.enlightened.controlling.attacking.CurrentAttack;
import ru.vsu.csf.enlightened.gameobjects.Map;
import ru.vsu.csf.enlightened.gameobjects.enemies.Dummy;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

import java.util.concurrent.atomic.AtomicReference;

public class HeroCollideListener implements ContactListener{

    private boolean ifHeroTouchesGround(Contact contact, AtomicReference<Hero> hero) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object first  = fixtureA.getBody().getUserData();
        Object second = fixtureB.getBody().getUserData();

        if (first == null || second == null)
            return false;

        if (first.getClass().equals(Hero.class) && second.getClass().equals(Map.class)) {
            hero.set((Hero) first);
            return true;
        }
        else if (second.getClass().equals(Hero.class) && first.getClass().equals(Map.class)) {
            hero.set((Hero) second);
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

    @Override
    public void beginContact(Contact contact) {
        AtomicReference<Hero> hero = new AtomicReference<Hero>(null);
        AtomicReference<Dummy> enemy = new AtomicReference<Dummy>(null);
        AtomicReference<CurrentAttack> attack = new AtomicReference<CurrentAttack>(null);


        if (ifHeroTouchesGround(contact, hero)) {
            hero.get().setIsGrounded(true);
        }

        if (ifEnemyWasHit(contact, enemy, attack)) {
            enemy.get().beAttacked(attack);
        }
    }

    @Override
    public void endContact(Contact contact) {
        AtomicReference<Hero> hero = new AtomicReference<Hero>(null);
        if (ifHeroTouchesGround(contact, hero)) {
            hero.get().setIsGrounded(false);
        }
        else {
            AtomicReference<Dummy> enemy = new AtomicReference<Dummy>(null);
            AtomicReference<CurrentAttack> attack = new AtomicReference<CurrentAttack>(null);

            if (ifEnemyWasHit(contact, enemy, attack)) {

            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
