package ru.vsu.csf.enlightened.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.vsu.csf.enlightened.ShapeSlasher;

public class DesktopLauncher {



    public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shape Slasher!";
        config.width = ShapeSlasher.WIDTH;
        config.height = ShapeSlasher.HEIGHT;
		new LwjglApplication(new ShapeSlasher(), config);
	}
}
