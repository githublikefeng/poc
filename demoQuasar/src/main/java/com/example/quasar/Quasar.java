package com.example.quasar;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

public class Quasar {
	public static void main(String[] args) throws ExecutionException, InterruptedException, SuspendExecution {
		int FiberNumber = 1_000_000;
		CountDownLatch latch = new CountDownLatch(1);
		AtomicInteger counter = new AtomicInteger(0);

		for (int i = 0; i < FiberNumber; i++) {
			new Fiber<Integer>(() -> {
				counter.incrementAndGet();
				System.out.println(counter.get());
				if (counter.get() == FiberNumber) {
					System.out.println("done");
				}
				Strand.sleep(1000000);
			}).start();
		}
		latch.await();
	}
}
