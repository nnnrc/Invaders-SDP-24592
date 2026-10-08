package screen;

import java.awt.event.KeyEvent;

import audio.AudioManager;
import engine.Cooldown;
import engine.Core;

/**
 * Implements the audio settings screen.
 * Lets the player change the BGM volume, the SFX volume, and the mute state.
 */
public class AudioControlScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Menu option for the background music volume. */
	private static final int BGM_VOLUME_OPTION = 0;

	/** Menu option for the sound effect volume. */
	private static final int SFX_VOLUME_OPTION = 1;

	/** Menu option for the global mute state. */
	private static final int MUTE_OPTION = 2;

	/** Number of menu options. */
	private static final int OPTION_COUNT = 3;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	/** Currently selected menu option. */
	private int selectedOption;

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

		// Defaults to the BGM volume.
		this.selectedOption = BGM_VOLUME_OPTION;
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

		draw();
		if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousOption();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextOption();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
				this.isRunning = false;
		}
	}

	/**
	 * Shifts the focus to the next menu option.
	 * Wraps around to the first option after the last one.
	 */
	private void nextOption() {
		this.selectedOption = (this.selectedOption + 1) % OPTION_COUNT;
	}

	/**
	 * Shifts the focus to the previous menu option.
	 * Wraps around to the last option before the first one.
	 */
	private void previousOption() {
		this.selectedOption = (this.selectedOption - 1 + OPTION_COUNT) % OPTION_COUNT;
	}

	/**
	 * Draws the elements associated with the screen.
	 * The current settings are read from the AudioManager.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawAudioSettings(this, this.selectedOption,
				AudioManager.getBGMVolume(), AudioManager.getSFXVolume(),
				AudioManager.isMuted());

		drawManager.completeDrawing(this);
	}
}
