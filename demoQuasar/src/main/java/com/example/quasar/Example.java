package com.example.quasar;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

/**
 * 协程-范例
 * 
 * @author pc
 *
 */
public class Example {

	public static void printer(Channel<Integer> in) throws SuspendExecution, InterruptedException {
		Integer v;
		while ((v = in.receive()) != null) {
			System.out.println(v);
		}
	}

	public static void main(String[] args) {
		System.out.println("dev-2测试1");
		final Channel<Integer> naturals = Channels.newChannel(-1);
		final Channel<Integer> squares = Channels.newChannel(-1);
		System.out.println("dev-2测试2");
		new Fiber<Integer>(new SuspendableRunnable() {

			private static final long serialVersionUID = -5375615253044059317L;

			@Override
			public void run() throws SuspendExecution, InterruptedException {
				for (int i = 0; i < 10; i++) {
					System.out.println("send int >> " + i);
					try {
						HttpHelper.post("123456");
					} catch (IOException e) {
//						e.printStackTrace();
					}
					naturals.send(i);
				}
				naturals.close();
			}
		}).start();

		new Fiber<Integer>(new SuspendableRunnable() {
			private static final long serialVersionUID = 3719414488123567683L;

			@Override
			public void run() throws SuspendExecution, InterruptedException {
				Integer v;
				while ((v = naturals.receive()) != null) {
					System.out.println("send multi >> " + v);
					squares.send(v * v);
				}
				squares.close();
			}
		}).start();
		System.out.println("start over ");
		try {
			printer(squares);
		} catch (SuspendExecution e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}