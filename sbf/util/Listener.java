package scripts.sbf.util;

import org.tribot.api2007.MessageListener;
import org.tribot.script.interfaces.MessageListening07;

public class Listener implements MessageListening07 {

	private static String messageReceived;
	private boolean teleBlocked;
	private boolean dead;

	public Listener() {
		MessageListener.addListener(this);
	}

	public String poll() {
		return messageReceived;
	}

	public boolean pollTeleBlocked() {
		if (teleBlocked) {
			teleBlocked = false;
			return true;
		}
		return false;
	}

	public boolean pollDead() {
		if (dead) {
			dead = false;
			return true;
		}
		return false;
	}

	

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverMessageReceived(String m) {
		switch (m) {
		case "A magical force stops you from teleporting.":
			teleBlocked = true;
			break;
		case "Oh dear, you are dead!":
			dead = true;
			break;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub

	}

}
