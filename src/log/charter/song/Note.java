package log.charter.song;

import static java.lang.System.arraycopy;
import static log.charter.util.ByteUtils.bytesToDouble;
import static log.charter.util.ByteUtils.doubleToBytes;
import static log.charter.util.ByteUtils.getBit;
import static log.charter.util.ByteUtils.getBitByte;

import java.util.Arrays;

public class Note extends Event {
	private static final int BIT_HOPO = 0;
	private static final int BIT_CRAZY = 1;
	private static final int BIT_YELLOW_TOM = 2;
	private static final int BIT_BLUE_TOM = 3;
	private static final int BIT_GREEN_TOM = 4;
	private static final int BIT_EXPERT_PLUS = 5;

	public static Note fromBytes(final byte[] bytes, final double offset) {
		final double pos = offset + bytesToDouble(Arrays.copyOfRange(bytes, 2, 10));
		final double length = bytesToDouble(Arrays.copyOfRange(bytes, 10, 18));
		return new Note(pos, length, bytes[0], //
				getBit(bytes[1], BIT_HOPO), //
				getBit(bytes[1], BIT_CRAZY), //
				getBit(bytes[1], BIT_YELLOW_TOM), //
				getBit(bytes[1], BIT_BLUE_TOM), //
				getBit(bytes[1], BIT_GREEN_TOM), //
				getBit(bytes[1], BIT_EXPERT_PLUS));
	}

	/**
	 * GUITAR: 0 -> open note<br/>
	 * notes & 1 -> Green<br/>
	 * notes & 2 -> Red<br/>
	 * notes & 4 -> Yellow<br/>
	 * notes & 8 -> Blue<br/>
	 * notes & 16 -> Orange<br/>
	 *
	 * DRUMS: notes & 1 -> Bass<br/>
	 * notes & 2 -> Red<br/>
	 * notes & 4 -> Yellow<br/>
	 * notes & 8 -> Blue<br/>
	 * notes & 16 -> Green
	 */
	public int notes;

	public boolean forced;
	public boolean hopo;
	public boolean tap;
	public boolean crazy;

	public boolean yellowTom;
	public boolean blueTom;
	public boolean greenTom;
	public boolean expertPlus;

	public Note(final double pos) {
		super(pos);
	}

	public Note(final double pos, final int notes) {
		super(pos);
		this.notes = notes;
	}

	public Note(final double pos, final double length, final int notes, final boolean hopo, final boolean crazy,
			final boolean yellowTom, final boolean blueTom, final boolean greenTom, final boolean expertPlus) {
		super(pos, length);
		this.notes = notes;
		this.hopo = hopo;
		this.crazy = crazy;
		this.yellowTom = yellowTom;
		this.blueTom = blueTom;
		this.greenTom = greenTom;
		this.expertPlus = expertPlus;
	}

	public Note(final Note n) {
		super(n);
		notes = n.notes;
		tap = n.tap;
		hopo = n.hopo;
		crazy = n.crazy;
		forced = n.forced;
		yellowTom = n.yellowTom;
		blueTom = n.blueTom;
		greenTom = n.greenTom;
		expertPlus = n.expertPlus;
	}

	public byte[] toBytes(final double offset) {
		final byte[] noteBytes = new byte[18];
		noteBytes[0] = (byte) notes;
		noteBytes[1] = 0;
		noteBytes[1] |= (hopo ? getBitByte(BIT_HOPO) : 0);
		noteBytes[1] |= (crazy ? getBitByte(BIT_CRAZY) : 0);
		noteBytes[1] |= (yellowTom ? getBitByte(BIT_YELLOW_TOM) : 0);
		noteBytes[1] |= (blueTom ? getBitByte(BIT_BLUE_TOM) : 0);
		noteBytes[1] |= (greenTom ? getBitByte(BIT_GREEN_TOM) : 0);
		noteBytes[1] |= (expertPlus ? getBitByte(BIT_EXPERT_PLUS) : 0);

		arraycopy(doubleToBytes(pos - offset), 0, noteBytes, 2, 8);
		arraycopy(doubleToBytes(getLength()), 0, noteBytes, 10, 8);

		return noteBytes;
	}

	@Override
	public String toString() {
		return "Note{notes: "//
				+ (notes & 1) + ((notes >> 1) & 1) + ((notes >> 2) & 1) + ((notes >> 3) & 1) + ((notes >> 4) & 1)//
				+ ", pos: " + pos//
				+ ", length: " + getLength()//
				+ ", hopo: " + (hopo ? "T" : "F")//
				+ ", tap: " + (tap ? "T" : "F")//
				+ ", crazy: " + (crazy ? "T" : "F") //
				+ ", yellowTom: " + (yellowTom ? "T" : "F") //
				+ ", blueTom: " + (blueTom ? "T" : "F") //
				+ ", greenTom: " + (greenTom ? "T" : "F") //
				+ ", expertPlus: " + (expertPlus ? "T" : "F") + "}";
	}
}
