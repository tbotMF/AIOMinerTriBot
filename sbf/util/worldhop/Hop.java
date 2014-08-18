package scripts.sbf.util.worldhop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Login;

import scripts.sbf.action.Action;

public class Hop extends Action {
	private Random rand = new Random();
	private List<Integer> blackList_arbit = new ArrayList<Integer>();
	private List<Integer> allowedWorlds_arbit = new ArrayList<Integer>();
	private boolean[] worldHopOps = manager.getWorldHopOps();
	private int worldNumber;

	enum Worlds {
		FREE_WORLDS(8, 16, 81, 82, 83, 84, 93, 94), PVP_WORLDS(25, 37), POPULAR_WORLDS(
				1, 2, 30), UNLISTED(7, 15, 23, 24, 31, 32, 39, 40, 47, 48, 55,
				56, 63, 64, 71, 72, 79, 80, 85, 86, 87, 88, 89, 90, 91, 92, 93), ALL_WORLDS(
				1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
				21, 22, 25, 26, 27, 28, 29, 30, 33, 34, 35, 36, 37, 38, 41, 42,
				43, 44, 45, 46, 49, 50, 51, 52, 53, 54, 57, 58, 59, 60, 61, 62,
				65, 66, 67, 68, 69, 70, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84,
				93, 94);

		int[] worlds;

		Worlds(int... worlds) {
			this.worlds = worlds;
		}

		int[] getWorlds() {
			return this.worlds;
		}

		int getCount() {
			return this.worlds.length;
		}

		int getAtIndex(int i) {
			if (i < getCount())
				return worlds[i];

			return 0;
		}
	}

	private int getIndex(int wN) {
		for (int i = 0; i < Worlds.ALL_WORLDS.getCount(); i++)
			if (wN == Worlds.ALL_WORLDS.getAtIndex(i))
				return i;
		return 0;
	}

	private int selectNumber(boolean excludeFree, boolean excludePvP,
			boolean excludePopular) {

		if (allowedWorlds_arbit.isEmpty()) {
			if (excludeFree)
				for (int a : Worlds.FREE_WORLDS.getWorlds())
					blackList_arbit.add(a);

			if (excludePvP)
				for (int a : Worlds.PVP_WORLDS.getWorlds())
					blackList_arbit.add(a);

			if (excludePopular)
				for (int a : Worlds.POPULAR_WORLDS.getWorlds())
					blackList_arbit.add(a);

			for (int a : Worlds.UNLISTED.getWorlds())
				blackList_arbit.add(a);

			for (int i = 1; i <= 68; i++)
				if (!blackList_arbit.contains(i))
					allowedWorlds_arbit.add(i);
		}

		return allowedWorlds_arbit
				.get(rand.nextInt(allowedWorlds_arbit.size()));
	}

	public void hop(boolean excludeFree, boolean excludePvP,
			boolean excludePopular) {
	//	int randomWorld = WorldHopper.getRandomWorld(true);
		worldNumber = selectNumber(excludeFree, excludePvP, excludePopular);
		World world = new World(getIndex(worldNumber));
		manager.loadWorldNumber(worldNumber + 300);
		world.click();
	//	WorldHopper.changeWorld(randomWorld);
		
	}

	@Override
	public void execute() {
		if (worldHopOps != null)
			hop(worldHopOps[0], worldHopOps[1], worldHopOps[2]);

		if (Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 150);
				return Login.getLoginState() == Login.STATE.LOGINSCREEN;
			}

		}, General.random(3000, 3500))) {
			HopperGlobals.ON_WORLD_SCREEN.setStatus(false);
			HopperGlobals.READY_TO_LOGIN.setStatus(true);
		}
	}

	@Override
	public boolean isValid() {

		return HopperGlobals.ON_WORLD_SCREEN.getStatus();
	}

}
