package log.charter.gui.handlers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import log.charter.gui.ChartEventsHandler;

public class CharterFrameWindowListener implements WindowListener {

	private final ChartEventsHandler eventsHandler;

	public CharterFrameWindowListener(final ChartEventsHandler eventsHandler) {
		this.eventsHandler = eventsHandler;
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		eventsHandler.exit();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

}
