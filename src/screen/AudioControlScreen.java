package screen;

/**
 * Implements the audio settings screen.
 * Lets the player change the BGM volume, the SFX volume, and the mute state.
 */
public class AudioControlScreen extends Screen {

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
}
