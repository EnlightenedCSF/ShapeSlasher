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

    protected ArrayList<Body> toDelete;
    protected ArrayList<Body> toFreeze;

    public Projectiles(World world) {
        this.world = world;
        this.projectiles = new ArrayList<Body>();
        this.toDelete = new ArrayList<Body>();
        this.toFreeze = new ArrayList<Body>();
    }

    public void addNew(BodyDef projectileBody, FixtureDef projectileFixtureDef, float angle) {
        Body anotherVolley = world.createBody(projectileBody);

        projectiles.add(anotherVolley);

        anotherVolley.createFixture(projectileFixtureDef);
        anotherVolley.setGravityScale(0);
        anotherVolley.setBullet(true);

        anotherVolley.setTransform(projectiles.get(projectiles.indexOf(anotherVolley)).getPosition(), angle);
        anotherVolley.setUserData(new ProjectileInfo(anotherVolley, this));
    }

    public void delete(Body projectile) {
        if (!toDelete.contains(projectile))
            toDelete.add(projectile);
    }

    public void freeze(Body projectile) {
        if (!toFreeze.contains(projectile))
            toFreeze.add(projectile);
    }

    public void clearCollided() {
        for (Body b : toDelete) {
            int index = projectiles.indexOf(b);
            world.destroyBody(projectiles.get(index));
            projectiles.remove(index);
        }
        toDelete.clear();

        for (Body b : toFreeze) {
            b.setActive(false);
        }
        toFreeze.clear();
    }

    public void update() {
        for (Body b : projectiles) {
            if (b.isActive()) {
                b.setTransform((float) (b.getPosition().x + Hero.PROJECTILE_SPEED * Math.cos(b.getAngle())),
                        (float) (b.getPosition().y + Hero.PROJECTILE_SPEED * Math.sin(b.getAngle())),
                        b.getAngle());
            }
        }
    }


}
