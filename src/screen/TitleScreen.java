package screen;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import engine.Cooldown;
import engine.Core;

/**
 * Implements the title screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class TitleScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;
	/** Sound played when the menu selection moves. */
	private static Clip menuMoveClip;

	/** Sound played when a menu item is selected. */
	private static Clip menuSelectClip;

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
	public TitleScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
		if (menuMoveClip == null)
			menuMoveClip = loadSound("sound/menu_move.wav");
		if (menuSelectClip == null)
			menuSelectClip = loadSound("sound/menu_select.wav");
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
				&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
				playSound(menuMoveClip);
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
				playSound(menuMoveClip);
			}
			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				playSound(menuSelectClip);
				this.isRunning = false;
			}
		}
	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	private void nextMenuItem() {
		if (this.returnCode == 3)
			this.returnCode = 0;
		else if (this.returnCode == 0)
			this.returnCode = 2;
		else
			this.returnCode++;
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		if (this.returnCode == 0)
			this.returnCode = 3;
		else if (this.returnCode == 2)
			this.returnCode = 0;
		else
			this.returnCode--;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitle(this);
		drawManager.drawMenu(this, this.returnCode);

		drawManager.completeDrawing(this);
	}

	/**
	 * Plays a sound effect file once.
	 *
	 * @param path
	 *            Path of the WAV file to play.
	 */
	/**
	 * Loads a sound effect file and prepares it for playback.
	 *
	 * @param path
	 *            Path of the WAV file to load.
	 * @return Prepared clip, or null if loading failed.
	 */
	private Clip loadSound(final String path) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			return clip;
		} catch (Exception e) {
			logger.warning("Couldn't load sound: " + path);
			return null;
		}
	}

	/**
	 * Plays a prepared sound effect from the beginning.
	 *
	 * @param clip
	 *            Clip to play.
	 */
	private void playSound(final Clip clip) {
		if (clip == null)
			return;
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}
}
