package ru.vsu.csf.enlightened.controlling.attacking.projectile;

import com.badlogic.gdx.physics.box2d.Body;

public class ProjectileInfo {
    private Body body;
    private Projectiles projectiles;

    public ProjectileInfo(Body body, Projectiles projectiles) {
        this.body = body;
        this.projectiles = projectiles;
    }

    public Projectiles getProjectiles() {
        return projectiles;
    }

    public Body getBody() {
        return body;
    }
}
