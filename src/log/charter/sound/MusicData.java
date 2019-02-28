package log.charter.sound;

import static log.charter.sound.SoundPlayer.slow;
import static log.charter.sound.SoundPlayer.toBytes;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public class MusicData {

	public static MusicData readFile(final String path) {
		if (path.endsWith(".mp3")) {
			final File f = new File(path);
			if (f.exists()) {
				return Mp3Loader.load(f.getAbsolutePath());
			}
		} else if (path.endsWith(".ogg")) {
			final File f = new File(path);
			if (f.exists()) {
				return OggLoader.load(f.getAbsolutePath());
			}
		}
		return null;
	}

	public static MusicData readSongFile(final String dir) {
		File musicFile = new File(dir + "/guitar.mp3");
		if (musicFile.exists()) {
			return Mp3Loader.load(musicFile.getAbsolutePath());
		}
		musicFile = new File(dir + "/guitar.ogg");
		if (musicFile.exists()) {
			return OggLoader.load(musicFile.getAbsolutePath());
		}
		return null;
	}

	private static int[][] splitAudio(final byte[] b) {
		final int[][] d = new int[2][b.length / 4];
		for (int i = 0; i < b.length; i += 4) {
			d[0][i / 4] = b[i] + (b[i + 1] * 256);
			d[1][i / 4] = b[i + 2] + (b[i + 3] * 256);
		}

		return d;
	}

	public final int[][] data;
	public final AudioFormat outFormat;
	public byte[] preparedData;
	public int slow = 1;

	public MusicData(final byte[] b, final float rate) {
		preparedData = b;
		data = splitAudio(b);
		outFormat = new AudioFormat(Encoding.PCM_SIGNED, rate, 16, 2, 4, rate, false);
	}

	public void setSlow(final int newSlow) {
		if (newSlow == 0) {
			setSlow(1);
			return;
		}
		if (slow != newSlow) {
			slow = newSlow;
			preparedData = toBytes(slow(data, slow));
		}
	}
}