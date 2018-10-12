package com.example.quasar;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.demoCommons.SpeedMetric;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
//		final Channel<String> naturals = Channels.newChannel(5000);
		CountDownLatch latch = new CountDownLatch(1);
		System.out.println("dev-1测试2");
		long t0 = System.currentTimeMillis();
		System.out.println("dev-1测试3");
		final int num = 100*10000;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				long t0 = System.currentTimeMillis();
				while(i++ < num) {
//					System.out.println(i);
					final int pa = i;
					new Fiber<String>(new SuspendableRunnable() {
						@Override
						public void run() throws SuspendExecution, InterruptedException {
							try {
								SpeedMetric.handleRequest("request");
								String result = HttpHelper.post(String.valueOf(pa));
//								System.out.println(result);
//								naturals.send(result);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
//								naturals.send(e.getMessage());
							}
						}
					}).start();
//					naturals.close();
				}
				long t1 = System.currentTimeMillis();
				long submitTimes = t1 - t0;
				System.out.println("submitTimes:"+submitTimes);
			}
		}).start();
		
		AtomicInteger counter = new AtomicInteger(0);
		long t1 = System.currentTimeMillis();
		try {
//			int total = 0;
//			while(counter.getAndIncrement() < num) {
//				String result = naturals.receive();
////				System.out.println(result);
//				if (result!=null) {
//					SpeedMetric.handleRequest("resp");
//					total++;
//				}
//			}
//			naturals.close();
//			long t2 = System.currentTimeMillis();
//			long receiveTimes = t2 - t1;
//			System.out.println("receiveTimes:"+receiveTimes);
//			System.out.println("totalTimes:"+(t2 - t0));
//			System.out.println("total:"+total);
			latch.await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
