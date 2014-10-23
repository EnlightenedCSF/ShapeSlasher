package ru.vsu.csf.enlightened;

import com.badlogic.gdx.Game;
import ru.vsu.csf.enlightened.screens.GameScreen;

public class ShapeSlasher extends Game {

    public static final float DEGREES_TO_RADIANS = 0.0174532925f;

    public static int WIDTH   = 840;
    public static int HEIGHT  = 490;
	
	@Override
	public void create () {
        setScreen(new GameScreen(this));
	}
}
