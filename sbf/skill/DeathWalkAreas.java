package scripts.sbf.skill;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public enum DeathWalkAreas {
	LUMBRIDGE(new RSArea(new RSTile(3222, 3219, 0), 10)), CAMELOT(new RSArea(
			new RSTile(2757, 3478), 10)), FALADOR(new RSArea(new RSTile(2969,
			3339), 10));

	private RSArea area;

	DeathWalkAreas(RSArea area) {
		this.area = area;
	}

	public RSArea getArea() {
		return this.area;
	}

}
