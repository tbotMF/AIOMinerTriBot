package scripts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.tribot.api2007.types.RSTile;

import scripts.actions.Deposit;
import scripts.actions.Equip;
import scripts.actions.OpenBank;
import scripts.actions.WalkToBank;
import scripts.actions.WalkToMine;
import scripts.actions.Withdraw;
import scripts.actions.AxeFix.FindPickAxeHead;
import scripts.actions.AxeFix.FixAxe;
import scripts.actions.AxeFix.FreeInventorySpace;
import scripts.actions.Mine.ClickRock;
import scripts.actions.Mine.DropRocks;
import scripts.actions.Mine.LoadRocks;
import scripts.actions.Mine.LogOut;
import scripts.actions.Mine.Mine;
import scripts.actions.Mine.WalkToRock;
import scripts.sbf.graphics.AbstractGUI;
import scripts.sbf.state.State;
import scripts.sbf.util.worldhop.GoToWorldScreen;
import scripts.sbf.util.worldhop.Hop;
import scripts.sbf.util.worldhop.LogIn;
import scripts.states.Banking;
import scripts.states.FixingAxe;
import scripts.states.Hopping;
import scripts.states.Mining;

public class mfAIOMinerGUI extends AbstractGUI {

	/**
	 * Creates new form mfAIOMinerGUI
	 */
	public mfAIOMinerGUI() {
		initComponents();
	}

	@Override
	public List<State> createStateAssociations() {
		List<State> associations = new ArrayList<State>();

		State mining = new Mining();
		State banking = new Banking();
		State worldHopping = new Hopping();
		State fixingAxe = new FixingAxe();
		mining.addActions(new Equip(), new WalkToMine(), new LoadRocks(),
				new WalkToRock(), new ClickRock(), new Mine(), new DropRocks());
		fixingAxe.addActions(new FreeInventorySpace(), new FindPickAxeHead(),
				new FixAxe());
		banking.addActions(new WalkToBank(), new OpenBank(), new Deposit(),
				new Withdraw());
		worldHopping.addActions(new LogOut(), new GoToWorldScreen(), new Hop(),
				new LogIn());
		Collections.addAll(associations, worldHopping, mining, fixingAxe,
				banking);
		return associations;
	}

	/**
	 * mineLocationsMap.put("Varrock East", westVarrock);
	 * mineLocationsMap.put("Varrock West", eastVarrock);
	 * mineLocationsMap.put("Lumbridge Swamp East", eastLumb);
	 * mineLocationsMap.put("Lumbridge Swamp West", westLumb);
	 * mineLocationsMap.put("Al-Kharid", new RSTile(3298, 3298, 0));
	 * mineLocationsMap.put("Wilderness - Hobgoblins' Mine", new RSTile(3082,
	 * 3760, 0)); mineLocationsMap.put("Wilderness - Runite Mine", new
	 * RSTile(3059, 3884, 0));
	 * mineLocationsMap.put("Wilderness - Pirates' Mine", new RSTile(3058, 3945,
	 * 0)); mineLocationsMap.put("Wilderness - Skeletons' Mine", new
	 * RSTile(3017, 3590, 0)); mineLocationsMap.put("Wilderness - Steel Mine",
	 * new RSTile(3104, 3566, 0)); mineLocationsMap.put("East Ardougne", new
	 * RSTile(2710, 3300, 0)); mineLocationsMap.put("South Ardougne", new
	 * RSTile(2604, 3235, 0)); mineLocationsMap.put("TzHaar", new RSTile(2454,
	 * 5168, 0)); mineLocationsMap.put("Yanille", new RSTile(2628, 3145, 0));
	 * mineLocationsMap.put("Barbarian village", new RSTile(3081, 3421, 0));
	 * mineLocationsMap.put("Rimmington", new RSTile(2979, 3238, 0));
	 * skillManager.loadSkillAreas(mineLocationsMap);
	 * skillManager.loadSkillResourcesByModifiedColour("Clay", (short) 6705,
	 * (short) 6589); skillManager.loadSkillResourcesByModifiedColour("Copper",
	 * (short) 4645, (short) 3776, (short) 4510);
	 * skillManager.loadSkillResourcesByModifiedColour("Tin", (short) 53,
	 * (short) 57); skillManager.loadSkillResourcesByModifiedColour("Iron",
	 * (short) 2576); skillManager .loadSkillResourcesByModifiedColour("Silver",
	 * (short) 73663); skillManager.loadSkillResourcesByModifiedColour("Coal",
	 * (short) 10508); skillManager.loadSkillResourcesByModifiedColour("Gold",
	 * (short) 8885, (short) 8128);
	 * skillManager.loadSkillResourcesByModifiedColour("Mithril", (short)
	 * -22239); skillManager.loadSkillResourcesByModifiedColour("Adamant",
	 * (short) 21662); skillManager.loadSkillResourcesByModifiedColour("Runite",
	 * (short) -31437); skillManager.loadCurrentLevel(startMiningLevel);
	 * skillManager.loadRareResources("Gold", "Mithril", "Adamant", "Runite");
	 */
	@Override
	public void processUserSelections() {
		processMiningLocations();
		processOresTable();
		processDroppingMethod();
		processMining();

		selections.put("Player hop amount", hopWhenPlayerNumCheckBox.getText());
	}

	private void processDroppingMethod() {
		if (powerMineRadioButton.isSelected())
			selections.put("Dropping Method",
					(String) dropMethodCheckBox.getSelectedItem());

	}

	private void processMining() {
		if (powerMineRadioButton.isSelected())
			selections.put("Mining method", "Powermine");
		else
			selections.put("Mining method", "Banking");

	}

	public void processMiningLocations() {
		selections.put("Location", (String) locationComboBox.getSelectedItem());
		switch (selections.get("Location")) {
		case "Varrock East":
			skillManager.loadSkillTile(new RSTile(3285, 3365));

			break;
		case "Varrock West":
			skillManager.loadSkillTile(new RSTile(3178, 3368));

			break;
		case "Lumbridge Swamp East":
			skillManager.loadSkillTile(new RSTile(3226, 3147));

			break;
		case "Lumbridge Swamp West":
			skillManager.loadSkillTile(new RSTile(3145, 3147));
			break;
		case "Al-Kharid":
			skillManager.loadSkillTile(new RSTile(3298, 3298));

			break;
		case "Wilderness - Hobgoblins' Mine":
			skillManager.loadSkillTile(new RSTile(3082, 3760));

			break;
		case "Wilderness - Runite Mine":
			skillManager.loadSkillTile(new RSTile(3059, 3884));

			break;
		case "Wilderness - Pirates' Mine":
			skillManager.loadSkillTile(new RSTile(3058, 3945));

			break;
		case "Wilderness - Skeletons' Mine":
			skillManager.loadSkillTile(new RSTile(3017, 3590));

			break;
		case "Wilderness - Steel Mine":
			skillManager.loadSkillTile(new RSTile(3104, 3566));

			break;
		case "East Ardougne":
			skillManager.loadSkillTile(new RSTile(2710, 3300));

			break;
		case "South Ardougne":
			skillManager.loadSkillTile(new RSTile(2604, 3235));

			break;
		case "TzHaar":
			skillManager.loadSkillTile(new RSTile(2454, 5168));

			break;
		case "Yanille":
			skillManager.loadSkillTile(new RSTile(2628, 3145));

			break;
		case "Barbarian village":
			skillManager.loadSkillTile(new RSTile(3081, 3421));

			break;
		case "Rimmington":
			skillManager.loadSkillTile(new RSTile(2979, 3238));

			break;
		}
		skillManager.loadSkillArea(30);
	}

	private List<String> loadOres() {

		List<String> customList = new ArrayList<>();
		for (int j = 0; j < oresTable.getRowCount(); j++) {
			String value = (String) oresTable.getValueAt(j, 0);
			if (value != null && value.length() > 0)
				customList.add(value);
		}

		return customList;

	}

	public void processOresTable() {
		List<String> oreValues = new ArrayList<>();
		for (String a : loadOres()) {
			if (a.equals("Gold") || a.equals("Mithril") || a.equals("Adamant")
					|| a.equals("Runite"))
				selections.put("Is mining rare", "yes");
			oreValues.add(a);
		}
		selections.put("ores", oreValues);
	}

}
