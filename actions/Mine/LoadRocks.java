package scripts.actions.Mine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;

import scripts.GlobalVars;
import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;

public class LoadRocks extends Action {
	private final List<String> availableOres = selections.getContents("ores");
	private final Map<String, short[]> oresMap = skillManager
			.getSkillResourcesByModifiedColourMap();
	private LinkedList<short[]> rocks = new LinkedList<>();
	private short[] colourOfRockToMine;

	private final Filter<RSObject> colourFilter = new Filter<RSObject>() {

		@Override
		public boolean accept(RSObject rock) {
			if (rock != null) {
				RSObjectDefinition rockDefinition = rock.getDefinition();
				if (rockDefinition != null)
					for (short modColor : rockDefinition.getModifiedColors())
						for (short colorToMine : colourOfRockToMine)
							if (modColor == colorToMine)
								return true;

			}
			return false;
		}

	};
	private final Filter<RSObject> nonSmokeFilter = new Filter<RSObject>() {

		@Override
		public boolean accept(RSObject rock) {
			RSModel model = rock.getModel();
			return model != null && model.getVertexCount() < 150;
		}

	};

	@Override
	public void execute() {

		if (GlobalVars.getListOfRocks().isEmpty()) {
			for (String rockName : availableOres)
				rocks.add(oresMap.get(rockName));
			GlobalVars.loadListOfRocks(rocks);
		} else
			colourOfRockToMine = GlobalVars.getListOfRocks().get(0);

		if (colourOfRockToMine == null)
			return;

		final RSObject[] rocksToMine = Objects.findNearest(30,
				nonSmokeFilter.combine(colourFilter, true));

		if (rocksToMine.length < 1)
			if (selections.get("Is mining rare").equals("yes"))
				for (String rare : skillManager.getRareResourceList())
					if (SkillGlobals.HOPPING.setStatus(skillManager
							.getSkillResourceNameByModifiedColour(
									colourOfRockToMine).equalsIgnoreCase(rare)))
						return;

		if (rocksToMine[0] != null)
			GlobalVars.loadRockToMine(rocksToMine[0]);

	}

	@Override
	public boolean isValid() {
		return Globals.LOAD_ROCKS.getStatus();
	}

}
