package org.fastorm.api.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.forkjoin.ForkJoinPool;

public class FastOrmServices {
	public ForkJoinPool pool;
	public ExecutorService service;

	public FastOrmServices() {
		this.pool = new ForkJoinPool();
		this.service = new ThreadPoolExecutor(2, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
	}

	public void shutdown() {
		pool.shutdown();
		service.shutdown();
	}

}
