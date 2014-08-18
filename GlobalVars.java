package scripts;

import java.util.LinkedList;

import org.tribot.api2007.types.RSObject;

public class GlobalVars {
	private static RSObject rockToMine;
	private static LinkedList<short[]> rocks = new LinkedList<>();

	private GlobalVars() {

	}

	public static void loadRockToMine(RSObject rock) {
		rockToMine = rock;
	}

	public static RSObject getRockToMine() {
		return rockToMine;
	}

	public static void loadListOfRocks(LinkedList<short[]> rockList) {
		rocks = rockList;
	}

	public static LinkedList<short[]> getListOfRocks() {
		return rocks;
	}
}
