package scripts;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.WebWalking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.sbf.manager.Manager;
import scripts.sbf.skill.SkillManager;
import scripts.sbf.util.ABC;
import scripts.sbf.util.MFUtil;

@ScriptManifest(authors = { "modulusfrank12" }, category = "Mining", name = "mfAIOMiner")
public class mfAIOMiner extends Script implements Painting {

	private final Manager manager = Manager.getInstance();
	private final SkillManager skillManager = SkillManager.getInstance();
	private final ABC abc = new ABC();
	private int startMiningLevel = Skills.getActualLevel(SKILLS.MINING);
	private final mfAIOMinerGUI minerGUI = new mfAIOMinerGUI();
	private long startTime;
	private final Font paintFont = new Font("Miriam Fixed", Font.BOLD, 14);

	@Override
	public void run() {
		println("Thank you very much for choosing this script, "
				+ General.getTRiBotUsername() + "!");
		loadSkillEssentials();
		manager.loadScript(this);
		manager.loadABC(abc);
		WebWalking.setUseAStar(true);
		manager.loadWorldHopOps(true, true, true);
		setMouseSpeed(105);
		start();
	}

	public void start() {
		minerGUI.setTitle("mfAIOMiner v1.0");
		minerGUI.setVisible(true);
		startTime = Timing.currentTimeMillis();
		manager.initialize(minerGUI, 20, false);
	}

	private void setMouseSpeed(int mouseSpeed) {
		manager.setMouseSpeed(mouseSpeed);
		Mouse.setSpeed(mouseSpeed);
	}

	@Override
	public void onPaint(Graphics gr) {
		Graphics2D g2d = (Graphics2D) gr;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);
		String runtime = Timing.msToString(System.currentTimeMillis()
				- startTime);
		g2d.setColor(Color.WHITE);
		g2d.setFont(paintFont);

		g2d.drawString(
				"Exp : "
						+ manager.getExpPerHrForFxnTime(
								MFUtil.getExpForTrainedSkills(),
								System.currentTimeMillis() - startTime)
						+ "\\hr", 10,

				50);
		g2d.drawString("Runtime : " + runtime, 10, 80);
		g2d.drawString("mfMiner v1.0", 10, 110);
		g2d.setColor(Color.gray);
		g2d.drawRect(0, 30, 240, 27);
		g2d.drawRect(0, 60, 240, 27);
		g2d.drawRect(0, 90, 150, 27);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2d.setColor(Color.cyan);
		g2d.fillRect(0, 30, 240, 27);
		g2d.setColor(Color.orange);
		g2d.fillRect(0, 58, 240, 27);
		g2d.setColor(Color.green);
		g2d.fillRect(0, 88, 150, 27);
	}

	private void loadSkillEssentials() {
		skillManager.loadSkillResourcesByModifiedColour("Clay", (short) 6705,
				(short) 6589);
		skillManager.loadSkillResourcesByModifiedColour("Copper", (short) 4645,
				(short) 3776, (short) 4510);
		skillManager.loadSkillResourcesByModifiedColour("Tin", (short) 53,
				(short) 57);
		skillManager.loadSkillResourcesByModifiedColour("Iron", (short) 2576);
		skillManager
				.loadSkillResourcesByModifiedColour("Silver", (short) 73663);
		skillManager.loadSkillResourcesByModifiedColour("Coal", (short) 10508);
		skillManager.loadSkillResourcesByModifiedColour("Gold", (short) 8885,
				(short) 8128);
		skillManager.loadSkillResourcesByModifiedColour("Mithril",
				(short) -22239);
		skillManager.loadSkillResourcesByModifiedColour("Adamant",
				(short) 21662);
		skillManager.loadSkillResourcesByModifiedColour("Runite",
				(short) -31437);
		skillManager.loadCurrentLevel(startMiningLevel);
		skillManager.loadRareResources("Gold", "Mithril", "Adamant", "Runite");
	}
}
