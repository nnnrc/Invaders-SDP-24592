package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Core;
import engine.Score;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores;
	/** Sound played when leaving the high score screen. */
	private static Clip menuBackClip;
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
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;

		try {
			this.highScores = Core.getFileManager().loadHighScores();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}

		if (menuBackClip == null)
			menuBackClip = loadSound("sound/menu_back.wav");
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
		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished()) {
			playSound(menuBackClip);
			this.isRunning = false;
		}

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawHighScoreMenu(this);
		drawManager.drawHighScores(this, this.highScores);

		drawManager.completeDrawing(this);
	}

	// TODO: AudioManager 완성 후 교체

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
