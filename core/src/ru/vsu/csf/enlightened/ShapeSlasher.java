package ru.vsu.csf.enlightened;

import com.badlogic.gdx.Game;
import ru.vsu.csf.enlightened.screens.MainMenuScreen;

public class ShapeSlasher extends Game {

    public static int WIDTH   = 840;
    public static int HEIGHT  = 500;
	
	@Override
	public void create () {
        setScreen(new MainMenuScreen(this));
	}
}
