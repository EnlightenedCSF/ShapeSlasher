package ru.vsu.csf.enlightened.controlling;

public class ComboTreeNode {

    int key;

    float delay;
    float timeLeft;

    public Action action;
    public ComboTreeNode[] nodes;

    public ComboTreeNode(int key, float delay) {
        this.key = key;
        this.delay = delay;
        timeLeft = delay;
        action = null;
        nodes = null;
    }

    public boolean isComboOver(float delta) {
        this.timeLeft -= delta;
        if (timeLeft < 0) {
            timeLeft = delay;
            //Gdx.app.log("Child", "Combo is over!");
            return true;
        }
        return false;
    }

    public ComboTreeNode getChildWithKeycode(int keycode) {
        if (nodes == null)
            return null;

        for (ComboTreeNode node : nodes) {
            if (node.key == keycode)
                return node;
        }

        return null;
    }

    public void Do() {
        if (action != null)
            action.Do();
    }
}
