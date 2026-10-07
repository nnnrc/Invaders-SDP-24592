package audio;

import engine.Core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AudioCache {

    private final Logger logger = Core.getLogger();

    private final Map<String, AudioResource> cacheMap = new HashMap<>();

    public synchronized AudioResource getAudioData(String path) {
        if (path == null) return null;
        if (cacheMap.containsKey(path)) return cacheMap.get(path);

        AudioResource data = readAudioData(path);
        if (data != null) cacheMap.put(path, data);

        return data;
    }

    private AudioResource readAudioData(String path) {
        URL resource = AudioCache.class.getClassLoader().getResource(path);

        if (resource == null) {
            logger.warning("Audio resource not found: " + path);
            return null;
        }

        try (AudioInputStream stream = AudioSystem.getAudioInputStream(resource)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int frameSize = stream.getFormat().getFrameSize();
            byte[] buffer = new byte[frameSize * 1024];
            int count;

            while ((count = stream.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }

            return new AudioResource(stream.getFormat(), output.toByteArray());
        } catch (Exception e) {
            logger.warning("Failed to read audio file: " + path + " / " + e);
        }

        return null;
    }

    public static final class AudioResource {
        final AudioFormat format;
        final byte[] data;

        AudioResource(AudioFormat format, byte[] data) {
            this.format = format;
            this.data = data;
        }
    }

}
