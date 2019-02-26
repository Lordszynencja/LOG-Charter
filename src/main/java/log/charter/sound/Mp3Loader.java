package log.charter.sound;

import static java.util.Arrays.copyOf;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static log.charter.io.Logger.error;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Mp3Loader {
	public static MusicData load(final String path) {
		try {
			final AudioInputStream in = getAudioInputStream(new BufferedInputStream(new FileInputStream(path)));

			final float rate = in.getFormat().getSampleRate();
			final AudioFormat outFormat = new AudioFormat(Encoding.PCM_SIGNED, rate, 16, 2, 4, rate, false);
			final AudioInputStream formattedIn = getAudioInputStream(outFormat, in);

			final List<byte[]> bytesList = new ArrayList<>();
			int last = 0;
			int length = 0;
			while (last >= 0) {
				final byte[] bytes = new byte[10000];
				last = formattedIn.read(bytes);
				if (last > 0) {
					bytesList.add(copyOf(bytes, last));
					length += last;
				}
			}

			final byte[] buffer = new byte[length];
			last = 0;
			for (final byte[] bytes : bytesList) {
				System.arraycopy(bytes, 0, buffer, last, bytes.length);
				last += bytes.length;
			}

			return new MusicData(buffer, rate);
		} catch (final IOException | UnsupportedAudioFileException e) {
			error("Couldnt load mp3 file " + path, e);
		}
		return null;
	}
}
