package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;

/**
 * Implements the audio settings screen.
 * Lets the player change the BGM volume, the SFX volume, and the mute state.
 */
public class AudioControlScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public AudioControlScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
	}

	/**
	 * Opens the audio settings screen and waits until it is closed.
	 * Other screens can call this method to show the audio settings.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public static void open(final int width, final int height, final int fps) {
		AudioControlScreen screen = new AudioControlScreen(width, height, fps);
		screen.initialize();
		screen.run();
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
				this.isRunning = false;
		}
	}
}
