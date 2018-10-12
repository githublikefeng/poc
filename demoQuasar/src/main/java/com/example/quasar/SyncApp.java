package com.example.quasar;

import java.io.IOException;

import org.demoCommons.SpeedMetric;

public class SyncApp {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int i = 0;
		final int num = 10*10000;
		long t0 = System.currentTimeMillis();
		while(i++ < num) {
			SpeedMetric.handleRequest("request");
			String result = HttpHelper.post(String.valueOf(i));
		}
		long t1 = System.currentTimeMillis();
		long submitTimes = t1 - t0;
		System.out.println("submitTimes:"+submitTimes);
	}

}
