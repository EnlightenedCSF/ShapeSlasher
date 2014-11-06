package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.physics.box2d.Body;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;

public class EnemyAttack extends CurrentAttack {
    public EnemyAttack(int index, Facing facing, Body body, float duration) {
        super(index, facing, body, duration);
    }

    public CurrentAttack transform() {
        try {
            return (CurrentAttack) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
