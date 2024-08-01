package bot;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class Common {
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int generateRandomNumber(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static boolean sleepUntil(BooleanSupplier condition, int sleep, int timeout) {
		long start = System.currentTimeMillis();
		long timeElapsed = 0;
		while(true) {
			if(condition.getAsBoolean()) {
				return true;
			}
			timeElapsed = System.currentTimeMillis() - start;
			if(timeElapsed >= timeout) {
				return false;
			}
			sleep(sleep);
		}
	}
}
