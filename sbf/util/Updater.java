package scripts.sbf.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scripts.sbf.manager.Manager;

public class Updater {

	private static final ExecutorService executor = Executors
			.newCachedThreadPool();

	private Updater() {

	}

	public static void zybeyzUpdateProfit(final int profitable) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				Manager.getInstance().updateProfitCounter(profitable);
			}

		});

	}

	public static void zybeyzUpdateProfit(final String profitable) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				Manager.getInstance().updateProfitCounter(profitable);
			}

		});

	}

}
