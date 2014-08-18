package scripts.sbf.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSTile;

import scripts.sbf.manager.Manager;

public class MFUtil {
	private MFUtil() {
	}

	private final static SKILLS[] skills = { SKILLS.ATTACK, SKILLS.STRENGTH,
			SKILLS.DEFENCE, SKILLS.RANGED, SKILLS.MAGIC, SKILLS.HITPOINTS,
			SKILLS.FLETCHING, SKILLS.MINING, SKILLS.CRAFTING };
	private final static int[] startXps = { Skills.getXP(skills[0]),
			Skills.getXP(skills[1]), Skills.getXP(skills[2]),
			Skills.getXP(skills[3]), Skills.getXP(skills[4]),
			Skills.getXP(skills[5]), Skills.getXP(skills[6]),
			Skills.getXP(skills[7]), Skills.getXP(skills[8]) };

	public static SKILLS determineXpToCheck() {
		LinkedHashMap<SKILLS, Integer> map = new LinkedHashMap<SKILLS, Integer>();
		int totalXp = 0;

		// Check what skills have been trained
		for (int i = 0; i < skills.length; i++) {
			int xpGained = Skills.getXP(skills[i]) - startXps[i];
			if (xpGained > 0) {
				totalXp += xpGained;
				map.put(skills[i], totalXp);
			}
		}

		// Determine what skill to hover
		if (map.size() > 0) {
			int random = General.random(0, totalXp);
			Set<SKILLS> skills = map.keySet();
			for (SKILLS skill : skills) {
				int xp = map.get(skill);
				if (xp >= random) {
					return skill;
				}
			}
		}

		return null;
	}
	
	public static boolean closeBank(){
		return Banking.isBankScreenOpen() ? (Banking.close() ? Timing
				.waitCondition(new Condition() {

					@Override
					public boolean active() {
						return !Banking.isBankScreenOpen();
					}

				}, General.random(5000, 6000)) : false) : true;
	}

	public static boolean enterAmountIsOpen() {
		return Screen.getColorAt(new Point(297, 403))
				.equals(new Color(0, 0, 0));
	}

	public static boolean useItemWithAnother(RSItem itemOne, RSItem itemTwo) {

		return itemOne.click("Use") && itemTwo.click("Use") ? Timing
				.waitMenuOpen(5000) : false;
	}

	public static Map<SKILLS, Integer> getAllTrainedSkills() {
		LinkedHashMap<SKILLS, Integer> map = new LinkedHashMap<SKILLS, Integer>();
		int totalXp = 0;

		// Check what skills have been trained
		for (int i = 0; i < skills.length; i++) {
			int xpGained = Skills.getXP(skills[i]) - startXps[i];
			if (xpGained > 0) {
				totalXp += xpGained;
				map.put(skills[i], totalXp);
			}
		}

		return map;
	}

	public static double getPercentUntilNextLevel(SKILLS skill) {
		return Math.round(getPercentAsFloat(skill, 100) * 100) / 100;
	}

	// get avg pt
	public static Point average(Point[] ps) {
		int tx = 0;
		int ty = 0;
		for (int i = 0; i <= ps.length - 1; i++) {
			tx += ps[i].x;
			ty += ps[i].y;
		}
		return new Point((tx / ps.length), (ty / ps.length));
	}

	public static boolean clickModel(RSModel model, String upText) {
		if (model != null) {
			Point[] ps = model.getAllVisiblePoints();
			if (ps.length > 0) {
				Point p = average(ps);
				Mouse.move(p.x + General.random(-3, 3),
						p.y + General.random(-3, 3));
				if (Game.getUptext().contains(upText)) {
					DynamicClicking.clickRSModel(model, 1);
					return true;
				} else {
					DynamicClicking.clickRSModel(model, 3);
					long t = System.currentTimeMillis();
					while (!ChooseOption.isOpen()) {
						General.sleep(100, 250);
						if (Timing.timeFromMark(t) >= General
								.random(5000, 7000))
							break;
					}
					return ChooseOption.select(upText);
				}
			}
		}

		return false;
	}

	public static int getExpForTrainedSkills() {
		int totalXp = 0;

		for (int i = 0; i < skills.length; i++) {
			int xpGained = Skills.getXP(skills[i]) - startXps[i];
			if (xpGained > 0) {
				totalXp += xpGained;
			}
		}

		return totalXp;
	}

	public static int getInventoryCount(List<String> elements) {
		if (elements != null) {
			String[] items = elements.toArray(new String[elements.size()]);

			return Inventory.getCount(items);
		}
		return 0;
	}

	public static BufferedImage fetchImage(String IMAGE_URL) {
		try {
			return ImageIO.read(new URL(IMAGE_URL));
		} catch (IOException e) {
			Manager.getInstance().loadCurrentStatus("Image retrival failed.");
			return null;
		}
	}

	public static boolean inPossessionOfOnly(String... elements) {
		return elements != null
				&& Inventory.getAll().length == Inventory.find(elements).length;

	}


	private static Color completeColor = new Color(0, 155, 0, 75);
	private static Color incompleteColor = new Color(155, 0, 0, 75);
	private static Color fontColor = Color.BLACK;
	private static Font font = new Font("Arial", 12, 12);

	public static BufferedImage getProgressBar(SKILLS skill, int width,
			int height, boolean vertical) {
		final BufferedImage i = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		// Create graphics from the bufferedImage
		Graphics g = i.getGraphics();
		float complete = 0;
		if (vertical) {
			complete = getPercentAsFloat(skill, height);
		} else {
			complete = getPercentAsFloat(skill, width);
		}
		// Fill in the completed part of the bar
		g.setColor(completeColor);
		if (vertical) {
			g.fillRect(0, (int) (height - complete), width, (int) complete);
		} else {
			g.fillRect(0, 0, (int) complete, height);
		}
		// Fill in incomplete part of the bar
		g.setColor(incompleteColor);
		if (vertical) {
			g.fillRect(0, 0, width, (int) (height - complete));
		} else {
			g.fillRect((int) complete + 1, 0, (int) (width - complete), height);
		}
		// Surround in black bars
		/*
		 * g.setColor(Color.black); if(vertical){ g.drawLine(0,(int)
		 * (height-complete),width,(int) (height-complete)); }else{
		 * g.drawLine((int)complete, 0, (int)complete,height); }
		 */
		g.drawRect(0, 0, width - 1, height - 1);
		return i;
	}

	public static BufferedImage getProgressBar(SKILLS skill, int startXP,
			int width, int height, boolean vertical) {
		final BufferedImage i = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		// Create graphics from the bufferedImage
		Graphics g = i.getGraphics();
		float complete = 0;
		if (vertical) {
			complete = getPercentAsFloat(skill, height);
		} else {
			complete = getPercentAsFloat(skill, width);
		}
		// Fill in the completed part of the bar
		g.setColor(completeColor);
		if (vertical) {
			g.fillRect(0, (int) (height - complete), width, (int) complete);
		} else {
			g.fillRect(0, 0, (int) complete, height);
		}
		// Fill in incomplete part of the bar
		g.setColor(incompleteColor);
		if (vertical) {
			g.fillRect(0, 0, width, (int) (height - complete));
		} else {
			g.fillRect((int) complete + 1, 0, (int) (width - complete), height);
		}
		// Surround in black bars
		g.setColor(Color.black);
		if (vertical) {
			g.drawLine(0, (int) (height - complete), width,
					(int) (height - complete));
		} else {
			g.drawLine((int) complete, 0, (int) complete, height);
		}
		g.drawRect(0, 0, width - 1, height - 1);
		// Draw Text
		g.setFont(font);
		g.setColor(fontColor);
		String text = null;
		if (vertical) {
			text = getPercentAsInteger(skill) + "% to level "
					+ (Skills.getActualLevel(skill) + 1);
			int textSize = (g.getFontMetrics().getHeight()) * text.length();
			int y = (height / 2) - (textSize / 2);
			for (String s : text.split("")) {
				g.drawString(s, (width / 2)
						- (g.getFontMetrics().stringWidth(s) / 2), y);
				y += g.getFontMetrics().getHeight();
			}
		} else {
			text = getPercentAsInteger(skill)
					+ "% to level "
					+ (Skills.getActualLevel(skill) + 1)
					+ "(+"
					+ (Skills.getActualLevel(skill)
							- Skills.getLevelByXP(startXP) + ")");
			g.drawString(text,
					(width / 2) - (g.getFontMetrics().stringWidth(text) / 2),
					(height / 2) + (g.getFontMetrics().getHeight() / 3));
		}
		return i;
	}

	private static float getPercentAsFloat(SKILLS skill, int size) {
		float low, mid, high;
		if (Skills.getActualLevel(skill) >= 99) {
			return size;
		}
		low = Skills.getXPByLevel(Skills.getActualLevel(skill));
		mid = Skills.getXP(skill);
		high = Skills.getXPByLevel(Skills.getActualLevel(skill) + 1);
		return size * ((mid - low) / (high - low));
	}

	private static int getPercentAsInteger(SKILLS skill) {
		float low, mid, high;
		low = Skills.getXPByLevel(Skills.getActualLevel(skill));
		mid = Skills.getXP(skill);
		high = Skills.getXPByLevel(Skills.getActualLevel(skill) + 1);
		return (int) (100 * ((mid - low) / (high - low)));
	}

	public void setCompleteColor(Color col) {
		completeColor = col;
	}

	public void setIncompleteColor(Color col) {
		incompleteColor = col;
	}

	public void setFontColor(Color col) {
		fontColor = col;
	}

	public void setFont(Font f) {
		font = f;
	}

	public static boolean switchTab(final GameTab.TABS tab) {
		return (GameTab.getOpen() != tab) ? (GameTab.open(tab) ? Timing
				.waitCondition(new Condition() {

					@Override
					public boolean active() {
						General.sleep(100, 200);
						return GameTab.getOpen() == tab;
					}

				}, General.random(3000, 4000)) : false) : true;
	}

	public static int[] convertIntegerList(List<Integer> items) {
		if (items != null) {
			int[] ret = new int[items.size()];
			for (int i = 0; i < ret.length; i++)
				ret[i] = items.get(i);
			return ret;
		}
		return null;
	}

	public static int getNumOfFreeInvSlots() {
		return 28 - Inventory.getAll().length;
	}

	public static void dropAllExceptID(List<Integer> elementOne,
			List<String> elementTwo, String... elementThree) {
		final List<RSItem[]> dontDrop = new ArrayList<RSItem[]>();

		if (elementOne != null && elementOne.size() > 0) {
			RSItem[] a = Inventory.find(convertIntegerList(elementOne));

			if (a.length > 0)
				dontDrop.add(a);
		}
		if (elementTwo != null && elementOne.size() > 0) {
			RSItem[] b = Inventory.find(toArrayString(elementTwo));
			if (b.length > 0)
				dontDrop.add(b);
		}

		if (elementThree != null) {
			RSItem[] c = Inventory.find(elementThree);
			if (c.length > 0)
				dontDrop.add(c);
		}
		if (dontDrop.size() > 0) {

			List<Integer> id = new ArrayList<Integer>();
			for (int i = 0; i < dontDrop.size(); i++)
				if (dontDrop.get(i).length > 0) {
					for (RSItem item : dontDrop.get(i)) {
						id.add(item.getID());
					}
				}

			Inventory.dropAllExcept(convertIntegerList(id));

		}
	}

	public static String[] toArrayString(List<String> items) {
		return items != null ? items.toArray(new String[items.size()]) : null;
	}

	public static RSItem[] toArrayRSItem(List<RSItem> items) {
		return items != null ? items.toArray(new RSItem[items.size()]) : null;

	}

	public static int getCountOfFreeInvSpace() {
		return 28 - Inventory.getAll().length;
	}

	public static boolean isOnTile(RSTile pos) {
		RSArea boundingBox = null;
		if (pos != null)
			boundingBox = new RSArea(new RSTile(pos.getX() + 4, pos.getY() + 4,
					pos.getPlane()), new RSTile(pos.getX() - 4, pos.getY() - 4,
					pos.getPlane()));

		return boundingBox != null
				&& boundingBox.contains(Player.getPosition());

	}

}
