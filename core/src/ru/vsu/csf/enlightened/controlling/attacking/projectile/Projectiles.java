package ru.vsu.csf.enlightened.controlling.attacking.projectile;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

import java.util.ArrayList;

public class Projectiles {

    protected World world;
    protected ArrayList<Body> projectiles;

    public Projectiles(World world) {
        this.world = world;
        this.projectiles = new ArrayList<Body>();
    }

    public void addNew(BodyDef projectileBody, FixtureDef projectileFixtureDef, float angle) {
        Body anotherVolley = world.createBody(projectileBody);

        projectiles.add(0, anotherVolley);

        anotherVolley.createFixture(projectileFixtureDef);
        anotherVolley.setGravityScale(0);
        anotherVolley.setBullet(true);

        anotherVolley.setTransform(projectiles.get(0).getPosition(), angle);
        anotherVolley.setUserData(this);
    }

    public void update() {
        for (Body b : projectiles) {
            b.setTransform( (float)(b.getPosition().x + Hero.PROJECTILE_SPEED * Math.cos(b.getAngle())),
                    (float)(b.getPosition().y + Hero.PROJECTILE_SPEED * Math.sin(b.getAngle())),
                    b.getAngle());
        }
    }
}
