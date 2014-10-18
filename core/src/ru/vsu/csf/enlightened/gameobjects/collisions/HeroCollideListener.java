package ru.vsu.csf.enlightened.gameobjects.collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import ru.vsu.csf.enlightened.gameobjects.Hero;

public class HeroCollideListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() != null) {
            ((Hero) contact.getFixtureA().getBody().getUserData()).incNumContacts();
        }

        if (contact.getFixtureB().getBody().getUserData() != null) {
            ((Hero) contact.getFixtureB().getBody().getUserData()).incNumContacts();
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() != null) {
            ((Hero) contact.getFixtureA().getBody().getUserData()).decNumContacts();
        }

        if (contact.getFixtureB().getBody().getUserData() != null) {
            ((Hero) contact.getFixtureB().getBody().getUserData()).decNumContacts();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
