package util;

public class GCThread extends Thread {
	private static int garbageCollectorFlag = 0;

	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			if(garbageCollectorFlag < 40) sleep();
			else { System.gc(); garbageCollectorFlag = 0; }
		}
	}
	
	public static void increaseGCFlag(int i) {
		garbageCollectorFlag += i;
	}
	
	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}