package ru.vsu.csf.enlightened.controlling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.vsu.csf.enlightened.gameobjects.hero.Hero;

public class ComboTree {

    ComboTreeNode root;
    ComboTreeNode current;

    Hero hero;

    public ComboTree(final Hero hero) {
        this.hero = hero;

        root = new ComboTreeNode(-1,  0) {{
            nodes = new ComboTreeNode[] {

                    new ComboTreeNode(Input.Keys.DOWN,  0.5f),
                    new ComboTreeNode(Input.Keys.UP, 0.5f),

                    new ComboTreeNode(Input.Keys.F, 2) {{
                         action = new Action() {
                             @Override
                             public void Do() {
                                 hero.attack(1);
                                 Gdx.app.log("Action", "Attack!");
                             }
                         };
                    }}
            };
        }};
        root.nodes[0].nodes = new ComboTreeNode[] {new ComboTreeNode(Input.Keys.F,  0.5f){{ action = new Action() {
            @Override
            public void Do() {
                hero.attack(2);
                Gdx.app.log("Action", "Sweep!");
            }
        }; }}
        };
        root.nodes[1].nodes = new ComboTreeNode[] {new ComboTreeNode(Input.Keys.F,  0.5f) {{ action = new Action() {
            @Override
            public void Do() {
                hero.attack(3);
                Gdx.app.log("Action", "Uppercut!");
            }
        }; }}
        };

        root.nodes[2].nodes = new ComboTreeNode[] {new ComboTreeNode(Input.Keys.F, 2) {{
            action = new Action() {
                @Override
                public void Do() {
                    hero.attack(1);
                    Gdx.app.log("Action", "Another attack!");
                }
            };
            nodes = new ComboTreeNode[] { new ComboTreeNode(Input.Keys.F, 2) {{
                action = new Action() {
                    @Override
                    public void Do() {
                        hero.attack(3);
                        Gdx.app.log("Action", "Finisher!");
                    }
                };
            }}
            };
        }}};

        current = root;
    }

    public void tick(float delta) {
        if (current != root && current.isComboOver(delta)) {
            current = root;
        }
    }


    public void keyDown(int keycode) {
        if (current == root) {
            ComboTreeNode node =  current.getChildWithKeycode(keycode);
            if (node != null) {
                current = node;
                current.Do();

                if (current.nodes.length == 0)
                    current = root;
                //Gdx.app.log("Key", "Pressed down");
            }
        }
        else {
            ComboTreeNode node = current.getChildWithKeycode(keycode);
            if (node == null) {
                current = root;
                Gdx.app.log("Key", "You have ended your combo");
            }
            else {
                current = node;
                current.Do();
                //Gdx.app.log("Key", "next one is pressed!");
            }
        }
    }

    public void keyUp(int keycode) {

    }
}
