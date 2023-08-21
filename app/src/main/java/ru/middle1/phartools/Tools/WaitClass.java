package ru.middle1.phartools.Tools;

import java.util.concurrent.TimeUnit;

public class WaitClass {
	public static void MakeAwait(int duration){
		try {
			TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}