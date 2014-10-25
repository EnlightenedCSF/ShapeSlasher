package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ru.vsu.csf.enlightened.gameobjects.Map;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;

import java.util.HashMap;

public class Attacks {

    public final static HashMap<Integer, HashMap<Facing, AttackTemplate>> attacks = new HashMap<Integer, HashMap<Facing, AttackTemplate>>() {{
        //regular attack
        put(1, new HashMap<Facing, AttackTemplate>() {{
            put(Facing.RIGHT, new AttackTemplate(new BodyDef(){{
                type = BodyType.KinematicBody;
                position.set(new Vector2(0.6f, 0));
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] {new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(0.75f, 0)});
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));

            put(Facing.LEFT, new AttackTemplate(new BodyDef(){{
                type = BodyType.KinematicBody;
                position.set(new Vector2(-0.6f, 0));
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] {new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(-0.75f, 0)});
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));
        }
        });

        //swipe
        put(2, new HashMap<Facing, AttackTemplate>() {{
            put(Facing.RIGHT, new AttackTemplate(new BodyDef() {{
                type = BodyType.KinematicBody;
                position.set(0.6f, -0.1f);
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] {new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(0.6f, -0.1f)});
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));

            put(Facing.LEFT, new AttackTemplate(new BodyDef() {{
                type = BodyType.KinematicBody;
                position.set(-0.6f, -0.1f);
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] {new Vector2(0, -0.1f), new Vector2(0, 0.1f), new Vector2(-0.6f, -0.1f)});
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));
        }
        });

        //uppercut
        put(3, new HashMap<Facing, AttackTemplate>() {{
            put(Facing.RIGHT, new AttackTemplate(new BodyDef() {{
                type = BodyType.KinematicBody;
                position.set(0.6f, 0);
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] { new Vector2(0, 0), new Vector2(0.6f, 0.6f), new Vector2(0, 1.2f), new Vector2(0.4f, 0.6f) });
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));

            put(Facing.LEFT, new AttackTemplate(new BodyDef() {{
                type = BodyType.KinematicBody;
                position.set(-0.6f, 0);
            }
            }, new FixtureDef() {{
                shape = new PolygonShape() {{
                    set(new Vector2[] { new Vector2(0, 0), new Vector2(-0.6f, 0.6f), new Vector2(0, 1.2f), new Vector2(-0.4f, 0.6f) });
                }
                };
                filter.categoryBits = Map.SWORD_CATEGORY;
                filter.maskBits = Map.SWORD_MASK;
            }
            }));
        }
        });
    }};

    //public static final SparseArray

/*put(new AttackIndex(1, Facing.RIGHT), ));

        ));
    */
}
