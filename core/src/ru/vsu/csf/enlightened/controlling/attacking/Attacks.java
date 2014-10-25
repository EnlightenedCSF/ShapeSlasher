package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ru.vsu.csf.enlightened.gameobjects.collisions.EntityTypes;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;

import java.util.HashMap;

public class Attacks {

    private static HashMap<Integer, HashMap<Facing, AttackTemplate>> attacks;

    private static void addAttack(int id, final Vector2 pos, final Vector2[] polyShape, final int damage, final float knockbackPower, final Vector2 knockBackDirection) {
        attacks.put(id, new HashMap<Facing, AttackTemplate>() {{
            put(Facing.RIGHT,
                new AttackTemplate(
                    new BodyDef() {
                        {
                            type = BodyType.KinematicBody;
                            position.set(pos);
                        }
                    },
                    new FixtureDef() {
                        {
                            shape = new PolygonShape() {
                                {
                                    set(polyShape);
                                }
                            };
                            filter.categoryBits = EntityTypes.SWORD_CATEGORY;
                            filter.maskBits = EntityTypes.SWORD_MASK;
                            isSensor = true;
                        }
                    }, damage, knockbackPower, knockBackDirection
                )
            );

            put(Facing.LEFT,
                new AttackTemplate(
                    new BodyDef() {
                        {
                            type = BodyType.KinematicBody;
                            position.set(-pos.x, pos.y);
                        }
                    },
                    new FixtureDef() {
                        {
                            shape = new PolygonShape() {
                                {
                                    Vector2[] newPShape = new Vector2[polyShape.length];
                                    for (int i = 0; i < polyShape.length; i++) {
                                        newPShape[i] = new Vector2(-polyShape[i].x, polyShape[i].y);
                                    }
                                    set(newPShape);
                                }
                            };
                            filter.categoryBits = EntityTypes.SWORD_CATEGORY;
                            filter.maskBits = EntityTypes.SWORD_MASK;
                            isSensor = true;
                        }
                    }, damage, knockbackPower, new Vector2(-knockBackDirection.x, knockBackDirection.y)
                )
            );
            }
        });
    }

    private static void init() {
        addAttack(1, new Vector2(0.6f, 0),
                new Vector2[]{new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(0.75f, 0)},
                20, 1, new Vector2(1, 0.2f));

        addAttack(2, new Vector2(0.6f, -0.1f),
                new Vector2[]{new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(0.6f, -0.1f)},
                15, 2, new Vector2(0.1f, 0.8f));

        addAttack(3, new Vector2(0.6f, 0),
                new Vector2[]{new Vector2(0, 0), new Vector2(0.6f, 0.6f), new Vector2(0, 1.2f), new Vector2(0.4f, 0.6f)},
                40, 4, new Vector2(0.6f, 1f));
    }

    public static HashMap<Integer, HashMap<Facing, AttackTemplate>> getAttacks() {
        if (attacks == null) {
            attacks = new HashMap<Integer, HashMap<Facing, AttackTemplate>>();
            init();
        }

        return attacks;
    }

    private Attacks() {}
}