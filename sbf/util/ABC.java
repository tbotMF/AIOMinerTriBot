package scripts.sbf.util;

import java.awt.Polygon;
import java.awt.Rectangle;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;

import scripts.sbf.graphics.UserSelections;

/**
 * @author Starfox Utility class used for implementation of Anti-ban Compliance.
 */
public class ABC {

	private final ABCUtil abc;

	public ABC() {
		General.useAntiBanCompliance(true);
		this.abc = new ABCUtil();
	}

	/**
	 * Gets the ABCUtil object.
	 * 
	 * @return The ABCUtil object.
	 */
	public final ABCUtil getABCUtil() {
		return this.abc;
	}

	/**
	 * Checks to see whether or not the specified bool tracker is ready.
	 * 
	 * @param tracker
	 *            The tracker to check.
	 * @return True if it is ready, false otherwise.
	 */
	public final boolean isBoolTrackerReady(ABCUtil.BOOL_TRACKER tracker) {
		return tracker.next();
	}

	/**
	 * Checks to see whether or not the specified switch tracker is ready.
	 * 
	 * @param tracker
	 *            The tracker to check.
	 * @param playerCount
	 *            The player count.
	 * @return True if it is ready, false otherwise.
	 */
	public final boolean isSwitchTrackerReady(ABCUtil.SWITCH_TRACKER tracker,
			int playerCount) {
		return tracker.next(playerCount);
	}

	/**
	 * Gets the time (in milliseconds) until the next action will be performed
	 * for the specified delay tracker.
	 * 
	 * @param tracker
	 *            The tracker.
	 * @return The time (in milliseconds) until the next action will be
	 *         performed.
	 */
	public final long getTimeUntilNext(ABCUtil.DELAY_TRACKER tracker) {
		return tracker.next() - System.currentTimeMillis();
	}

	/**
	 * Gets the time (in milliseconds) until the next action will be performed
	 * for the specified int tracker.
	 * 
	 * @param tracker
	 *            The tracker.
	 * @return The time (in milliseconds) until the next action will be
	 *         performed.
	 */
	public final long getTimeUntilNext(ABCUtil.INT_TRACKER tracker) {
		return tracker.next() - System.currentTimeMillis();
	}

	/**
	 * Gets the time (in milliseconds) until the next action will be performed
	 * for the specified time tracker.
	 * 
	 * @param tracker
	 *            The tracker.
	 * @return The time (in milliseconds) until the next action will be
	 *         performed.
	 */
	public final long getTimeUntilNext(ABCUtil.TIME_TRACKER tracker) {
		return tracker.next() - System.currentTimeMillis();
	}

	/**
	 * Gets the next run at based off the int tracker.
	 * 
	 * @return The next run energy.
	 */
	public final int getNextRun() {
		return abc.INT_TRACKER.NEXT_RUN_AT.next();
	}

	/**
	 * Gets the next eat percent based off the int tracker.
	 * 
	 * @return The next eat percent.
	 */
	public final int getNextEatPercent() {
		return abc.INT_TRACKER.NEXT_EAT_AT.next();
	}

	/**
	 * Randomly moves the camera. Happens only if the time tracker for camera
	 * movement is ready.
	 */
	public final void moveCamera() {
		abc.performRotateCamera();
	}

	/**
	 * Checks the xp of the specified skill. Happens only if the time tracker
	 * for checking xp is ready.
	 * 
	 * @param skill
	 *            The skill to check.
	 */
	public void checkXp(SKILLS skill) {
		abc.performXPCheck(skill);
	}

	/**
	 * Activates run. Happens only when run is off & your run energy is >=
	 */
	public final void activateRun() {
		if (Game.getRunEnergy() >= getNextRun() && !Game.isRunOn()) {
			if (Options.setRunOn(true)) {
				abc.INT_TRACKER.NEXT_RUN_AT.reset();
			}
		}
	}

	/**
	 * Picks up the mouse. Happens only if the time tracker for picking up the
	 * mouse is ready.
	 */
	public final void pickUpMouse() {
		abc.performPickupMouse();
	}

	/**
	 * Leaves the game. Happens only if the time tracker for leaving the game is
	 * ready.
	 */
	public void leaveGame() {
		abc.performLeaveGame();
	}

	/**
	 * Examines a random object. Happens only if the time tracker for examining
	 * a random object is ready.
	 */
	public final void examineObject() {
		abc.performExamineObject();
	}

	/**
	 * Right clicks a random spot. Happens only if the time tracker for right
	 * clicking a random spot is ready.
	 */
	public final void rightClick() {
		abc.performRandomRightClick();
	}

	/**
	 * Randomly moves the mouse. Happens only if the time tracker for randomly
	 * moving the mouse is ready.
	 */
	public final void mouseMovement() {
		abc.performRandomMouseMovement();
	}

	/**
	 * Eats/drinks an item in your inventory with the specified name if your
	 * current hp percent is equal to or lower than the next eat percent.
	 * 
	 * @param name
	 *            The name of the food or drink.
	 * @return True if the food/drink was successfully eaten/drank.
	 */
	public final boolean eat() {
		final RSItem[] food = Inventory.find(UserSelections.getInstance().get(
				"Food"));
		if (food.length < 1) {
			return false;
		}
		final int hp = getHpPercent();
		String option = "";
		if (hp <= getNextEatPercent()) {
			final RSItem to_eat = food[0];
			if (to_eat != null) {
				final RSItemDefinition to_eat_definition = to_eat
						.getDefinition();
				if (to_eat_definition != null) {
					final String[] to_eat_actions = to_eat_definition
							.getActions();
					if (to_eat_actions != null && to_eat_actions.length > 0) {
						if (arrayContains(to_eat_actions, "Eat")) {
							option = "Eat";
						} else if (arrayContains(to_eat_actions, "Drink")) {
							option = "Drink";
						}
					}
				}
			}
			if (!option.isEmpty() && to_eat != null
					&& Clicking.click(option, to_eat)) {
				if (Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(50, 100);
						return getHpPercent() > hp;
					}
				}, General.random(1500, 2000))) {
					abc.INT_TRACKER.NEXT_EAT_AT.reset();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Eats/drinks an item in your inventory with the specified id if your
	 * current hp percent is equal to or lower than the next eat percent.
	 * 
	 * @param id
	 *            The id of the food or drink.
	 * @return True if the food/drink was successfully eaten/drank.
	 */
	public final boolean eat(final int id) {
		final RSItem[] food = Inventory.find(id);
		if (food.length < 1) {
			return false;
		}
		final int hp = getHpPercent();
		String option = "";
		if (hp <= getNextEatPercent()) {
			final RSItem to_eat = food[0];
			if (to_eat != null) {
				final RSItemDefinition to_eat_definition = to_eat
						.getDefinition();
				if (to_eat_definition != null) {
					final String[] to_eat_actions = to_eat_definition
							.getActions();
					if (to_eat_actions != null && to_eat_actions.length > 0) {
						if (arrayContains(to_eat_actions, "Eat")) {
							option = "Eat";
						} else if (arrayContains(to_eat_actions, "Drink")) {
							option = "Drink";
						}
					}
				}
			}
			if (!option.isEmpty() && to_eat != null
					&& Clicking.click(option, to_eat)) {
				if (Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return getHpPercent() > hp;
					}
				}, General.random(500, 600))) {
					abc.INT_TRACKER.NEXT_EAT_AT.reset();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Sleeps for the switch object delay time.
	 */
	public final void waitSwitchObjectDelay() {
		General.sleep(abc.DELAY_TRACKER.SWITCH_OBJECT.next());
		abc.DELAY_TRACKER.SWITCH_OBJECT.reset();
	}

	/**
	 * Sleeps for the switch object combat delay time.
	 */
	public final void waitSwitchObjectCombatDelay() {
		General.sleep(abc.DELAY_TRACKER.SWITCH_OBJECT_COMBAT.next());
		abc.DELAY_TRACKER.SWITCH_OBJECT_COMBAT.reset();
	}

	/**
	 * Sleeps for the new object delay time.
	 */
	public final void waitNewObjectDelay() {
		General.sleep(abc.DELAY_TRACKER.NEW_OBJECT.next());
		abc.DELAY_TRACKER.NEW_OBJECT.reset();
	}

	/**
	 * Sleeps for the new object combat delay time.
	 */
	public final void waitNewObjectCombatDelay() {
		General.sleep(abc.DELAY_TRACKER.NEW_OBJECT_COMBAT.next());
		abc.DELAY_TRACKER.NEW_OBJECT_COMBAT.reset();
	}

	/**
	 * Sleeps for the interaction delay time.
	 */
	public final void waitItemInteractionDelay() {
		General.sleep(abc.DELAY_TRACKER.ITEM_INTERACTION.next());
	}

	/**
	 * Executes the NEW_OBJECT/SWITCH_OBJECT delay, to be executed right before
	 * clicking objects/NPCs/players/tiles/ground items/minimap tiles/items. Not
	 * to be used between item clicks; only if we have to wait for an item to
	 * popup in the inventory before clicking on it, and if we do not know the
	 * exact time at which the item will appear in the inventory. In other
	 * words, this is the reaction delay when responding to a changing
	 * environment where we do not know the exact time at which the environment
	 * will change to the way we want.
	 * 
	 * @param last_busy_time
	 *            The timestamp at which the player was last
	 *            working/mining/woodcutting/fighting/fishing/crafting/etc. The
	 *            timestamp beings when we have to move on to the next resource.
	 * 
	 * @param combat
	 *            True if the player is in combat, or the script is one which
	 *            the player is constantly performing actions, and requires the
	 *            player to have very fast actions (such as sorceress's garden).
	 */
	public void waitNewOrSwitchDelay(final long last_busy_time,
			final boolean combat) {
		if (Timing.timeFromMark(last_busy_time) >= General.random(8000, 12000)) {
			if (combat) {
				waitNewObjectCombatDelay();
				return;
			}
			waitNewObjectDelay();
		} else {
			if (combat) {
				waitSwitchObjectCombatDelay();
				return;
			}
			waitSwitchObjectDelay();
		}
	}

	/**
	 * Hovers the next available object if applicable. YOU MUST RESET THE
	 * TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
	 * 
	 * @param currentlyInteracting
	 *            The object you are currently interacting with.
	 * @param objectName
	 *            The name of the object you wish to hover.
	 * @return True if the next object was hovered.
	 */
	public final boolean hoverNextObject(final RSObject currentlyInteracting,
			final String objectName) {
		if (currentlyInteracting == null || objectName == null) {
			return false;
		}
		final RSObject[] objects = Objects.findNearest(10,
				new Filter<RSObject>() {
					@Override
					public boolean accept(RSObject o) {
						if (o == null) {
							return false;
						}
						final RSObjectDefinition def = o.getDefinition();
						if (def != null) {
							final String name = def.getName();
							if (name != null) {
								return name.equalsIgnoreCase(objectName)
										&& !o.getPosition().equals(
												currentlyInteracting
														.getPosition())
										&& o.isOnScreen();
							}
						}
						return false;
					}
				});
		if (objects.length <= 0) {
			return false;
		}
		if (abc.BOOL_TRACKER.HOVER_NEXT.next()) {
			final RSObject next = objects[0];
			if (next != null) {
				if (!isHoveringObject(next) && next.hover()) {
					return Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return isHoveringObject(next);
						}
					}, 1000);
				}
			}
		}
		return false;
	}

	/**
	 * Hovers the next available RSCharacter if applicable. YOU MUST RESET THE
	 * TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
	 * 
	 * @param currentlyInteracting
	 *            The object you are currently interacting with.
	 * @param objectName
	 *            The name of the object you wish to hover.
	 * @return True if the next object was hovered.
	 */
	public final boolean hoverNextNPC(final RSCharacter currentlyInteracting,
			final String charName) {
		if (currentlyInteracting == null || charName == null) {
			return false;
		}
		final RSObject[] objects = Objects.findNearest(10,
				new Filter<RSObject>() {
					@Override
					public boolean accept(RSObject o) {
						if (o == null) {
							return false;
						}
						final RSObjectDefinition def = o.getDefinition();
						if (def != null) {
							final String name = def.getName();
							if (name != null) {
								return name.equalsIgnoreCase(charName)
										&& !o.getPosition().equals(
												currentlyInteracting
														.getPosition())
										&& o.isOnScreen();
							}
						}
						return false;
					}
				});
		if (objects.length <= 0) {
			return false;
		}
		if (abc.BOOL_TRACKER.HOVER_NEXT.next()) {
			final RSObject next = objects[0];
			if (next != null) {
				if (!isHoveringObject(next) && next.hover()) {
					return Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return isHoveringObject(next);
						}
					}, 100);
				}
			}
		}
		return false;
	}
	
	/**
	 * Hovers the next available object if applicable. YOU MUST RESET THE
	 * TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
	 * 
	 * @param currentlyInteracting
	 *            The object you are currently interacting with.
	 * @param objectName
	 *            The name of the object you wish to hover.
	 * @return True if the next object was hovered.
	 */
	public final boolean hoverNextObject(final String objectName) {
		if (objectName == null) {
			return false;
		}
		final RSObject[] objects = Objects.findNearest(10,
				new Filter<RSObject>() {
					@Override
					public boolean accept(RSObject o) {
						if (o == null) {
							return false;
						}
						final RSObjectDefinition def = o.getDefinition();
						if (def != null) {
							final String name = def.getName();
							if (name != null) {
								return name.contains(objectName)
										&& o.isOnScreen();
							}
						}
						return false;
					}
				});
		if (objects.length <= 0) {
			return false;
		}
		if (abc.BOOL_TRACKER.HOVER_NEXT.next()) {
			final RSObject next = objects[0];
			if (next != null) {
				if (!isHoveringObject(next) && next.hover()) {
					return Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return isHoveringObject(next);
						}
					}, 2000);
				}
			}
		}
		return false;
	}

	/**
	 * Hovers the next available object if applicable. YOU MUST RESET THE
	 * TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
	 * 
	 * @param currentlyInteracting
	 *            The object you are currently interacting with.
	 * @param objectName
	 *            The name of the object you wish to hover.
	 * @return True if the next object was hovered.
	 */
	public final boolean hoverNextItem(final String itemName) {
		if (itemName == null) {
			return false;
		}
		final RSItem[] items = Inventory.find(Filters.Items.nameContains(itemName));
		if (items.length <= 0) {
			return false;
		}
		if (abc.BOOL_TRACKER.HOVER_NEXT.next()) {
			final RSItem next = items[0];
			if (next != null) {
				if (!isHoveringItem(next) && next.hover()) {
					return Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return isHoveringItem(next);
						}
					}, 2000);
				}
			}
		}
		return false;
	}

	/**
	 * Gets an object near you with the specified id. This object will either be
	 * the nearest, or second nearest depending on the tracker.
	 * 
	 * @param id
	 *            The id of the object you are looking to find.
	 * @param distance
	 *            The distance range.
	 * @return An object with the specified id near you. Null if no objects were
	 *         found.
	 */
	public RSObject getUseClosestObject(final int id, final int distance) {
		final RSObject[] objects = Objects.findNearest(distance, id);
		if (objects.length < 1) {
			return null;
		}
		RSObject object_to_click = objects[0];
		if (objects.length > 1 && abc.BOOL_TRACKER.USE_CLOSEST.next()) {
			if (objects[1].getPosition().distanceToDouble(objects[0]) < 3.0) {
				object_to_click = objects[1];
			}
		}
		abc.BOOL_TRACKER.USE_CLOSEST.reset();
		return object_to_click;
	}

	/**
	 * Gets an object near you with the specified name. This object will either
	 * be the nearest, or second nearest depending on the tracker.
	 * 
	 * @param name
	 *            The name of the object you are looking to find.
	 * @param distance
	 *            The distance range.
	 * @return An object with the specified name near you. Null if no objects
	 *         were found.
	 */
	public RSObject getUseClosestObject(final String name, final int distance) {
		final RSObject[] objects = Objects.findNearest(distance, name);
		if (objects.length < 1) {
			return null;
		}
		RSObject object_to_click = objects[0];
		if (objects.length > 1 && abc.BOOL_TRACKER.USE_CLOSEST.next()) {
			if (objects[1].getPosition().distanceToDouble(objects[0]) < 3.0) {
				object_to_click = objects[1];
			}
		}
		abc.BOOL_TRACKER.USE_CLOSEST.reset();
		return object_to_click;
	}

	/**
	 * Gets an object near you with the specified id. This object will either be
	 * the nearest, or second nearest depending on the tracker.
	 * 
	 * @param id
	 *            The id of the object you are looking to find.
	 * @param distance
	 *            The distance range.
	 * @return An object with the specified id near you. Null if no objects were
	 *         found.
	 */
	public RSCharacter getUseClosestCharacter(final int id,
			final RSCharacter[] charArray) {
		if (charArray.length > 0) {
			final RSCharacter[] npcs = charArray;
			if (npcs.length < 1) {
				return null;
			}
			RSCharacter character_to_click = npcs[0];
			if (npcs.length > 1 && abc.BOOL_TRACKER.USE_CLOSEST.next()) {
				if (npcs[1].getPosition().distanceToDouble(npcs[0]) < 3.0) {
					character_to_click = npcs[1];
				}
			}
			abc.BOOL_TRACKER.USE_CLOSEST.reset();
			return character_to_click;
		}
		return null;

	}

	/**
	 * Gets an object near you with the specified name. This object will either
	 * be the nearest, or second nearest depending on the tracker.
	 * 
	 * @param name
	 *            The name of the object you are looking to find.
	 * @param distance
	 *            The distance range.
	 * @return An object with the specified name near you. Null if no objects
	 *         were found.
	 */
	public RSCharacter getUseClosestCharacter(final String name,
			final RSCharacter[] charArray) {
		if (charArray.length > 0) {
			final RSCharacter[] npcs = charArray;
			if (npcs.length < 1) {
				return null;
			}
			RSCharacter character_to_click = npcs[0];
			if (npcs.length > 1 && abc.BOOL_TRACKER.USE_CLOSEST.next()) {
				if (npcs[1].getPosition().distanceToDouble(npcs[0]) < 3.0) {
					character_to_click = npcs[1];
				}
			}
			abc.BOOL_TRACKER.USE_CLOSEST.reset();
			return character_to_click;
		}
		return null;
	}

	/**
	 * Gets an object near you with the specified id. This object will either be
	 * the nearest, or second nearest depending on the tracker. A distance range
	 * of 100 will be used as the default.
	 * 
	 * @param id
	 *            The id of the object you are looking to find.
	 * @return An object with the specified id near you. Null if no objects were
	 *         found.
	 */
	public RSObject getUseClosestObject(final int id) {
		return getUseClosestObject(id, 100);
	}

	/**
	 * Gets an object near you with the specified name. This object will either
	 * be the nearest, or second nearest depending on the tracker. A distance
	 * range of 100 will be used as the default.
	 * 
	 * @param name
	 *            The name of the object you are looking to find.
	 * @return An object with the specified name near you. Null if no objects
	 *         were found.
	 */
	public RSObject getUseClosestObject(final String name) {
		return getUseClosestObject(name, 100);
	}

	/**
	 * Gets an object near you with the specified id. This object will either be
	 * the nearest, or second nearest depending on the tracker. A distance range
	 * of 100 will be used as the default.
	 * 
	 * @param id
	 *            The id of the object you are looking to find.
	 * @return An object with the specified id near you. Null if no objects were
	 *         found.
	 */
	public RSCharacter getUseClosestCharacer(final int id,
			RSCharacter[] charArray) {
		return getUseClosestCharacter(id, charArray);
	}

	/**
	 * Gets an object near you with the specified name. This object will either
	 * be the nearest, or second nearest depending on the tracker. A distance
	 * range of 100 will be used as the default.
	 * 
	 * @param name
	 *            The name of the object you are looking to find.
	 * @return An object with the specified name near you. Null if no objects
	 *         were found.
	 */
	public RSCharacter getUseClosestCharacer(final String name,
			RSCharacter[] charArray) {
		return getUseClosestCharacter(name, charArray);
	}

	public long itemInteractionDelay() {
		return this.abc.DELAY_TRACKER.ITEM_INTERACTION.next();
	}

	public boolean goToAnticipatedLocation(final RSTile anticipatedLocation) {
		if (!Combat.isUnderAttack()
				&& getGoToAnticipatedLocation(anticipatedLocation)) {
			if (anticipatedLocation != null)
				while (!MFUtil.isOnTile(anticipatedLocation)
						&& !Combat.isUnderAttack()) {
					if (PathFinding.aStarWalk(anticipatedLocation))
						Timing.waitCondition(new Condition() {

							@Override
							public boolean active() {
								General.sleep(100, 200);
								return MFUtil.isOnTile(anticipatedLocation);
							}

						}, General.random(12000, 14000));
				}
			if (MFUtil.isOnTile(anticipatedLocation))
				this.abc.BOOL_TRACKER.GO_TO_ANTICIPATED.reset();

		}
		return false;
	}

	public boolean getGoToAnticipatedLocation(RSTile dest) {
		return this.abc.BOOL_TRACKER.GO_TO_ANTICIPATED.next();
	}

	/**
	 * Checks all of the actions that should be done when idle. If any are
	 * ready, it will perform them.
	 * 
	 * @param skill
	 *            The skill to check the xp of. Null if no xp is being gained.
	 */
	public final void doAllIdleActions(SKILLS skill, GameTab.TABS nextTab) {
		// activateRun();
		if (skill != null) {
			checkXp(skill);
			MFUtil.switchTab(nextTab);
		}
		examineObject();
		moveCamera();
		leaveGame();
		mouseMovement();
		pickUpMouse();
		rightClick();
	}

	// START HELPER METHODS

	/**
	 * Gets the local players hp as a percent value.
	 * 
	 * @return The local players hp as a percent value.
	 */
	private int getHpPercent() {
		return (int) (100.0 * ((double) SKILLS.HITPOINTS.getCurrentLevel() / (double) SKILLS.HITPOINTS
				.getActualLevel()));
	}

	/**
	 * Checks to see if the specified array contains the specified string.
	 * 
	 * @param array
	 *            The array.
	 * @param string
	 *            The string.
	 * @return True if the array contains the string, false otherwise.
	 */
	private boolean arrayContains(String[] array, String string) {
		for (String s : array) {
			if (s.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the polygon area of the specified object.
	 * 
	 * @param object
	 *            The object.
	 * @return The polygon of the object.
	 */
	private Polygon getObjectArea(final RSObject object) {
		if (object == null) {
			return null;
		}
		final RSModel model = object.getModel();
		if (model != null) {
			return model.getEnclosedArea();
		}
		return null;
	}
	
	

	/**
	 * Checks to see if the bot mouse is hovering the specified object.
	 * 
	 * @param object
	 *            The object.
	 * @return True if the bot mouse is hovering the object, false otherwise.
	 */
	private boolean isHoveringObject(final RSObject object) {
		final Polygon area = getObjectArea(object);
		return area != null && area.contains(Mouse.getPos());
	}
	
	private boolean isHoveringItem(final RSItem item) {
		final Rectangle area = item.getArea();
		return area != null && area.contains(Mouse.getPos());
	}
}