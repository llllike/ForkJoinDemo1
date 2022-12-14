package com.forkJoin.task;

import java.util.concurrent.RecursiveTask;

public class ForkJoinCalculate extends RecursiveTask<Long>{
	private long start;
	private long end;
	private static final long THRESHOLD = 100000L;
	//临界值
	public ForkJoinCalculate(long start, long end) {
		this.start = start;
		this.end = end;
	}
	@Override
	protected Long compute() {
		long length = end - start;
		if(length <= THRESHOLD){
			long sum = 0;
			for (long i = start; i <= end; i++) {
				sum += i;
			}
			return sum;
		}else{
			long middle = (start + end) / 2;
			ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
			//拆分，并将该子任务压入线程队列
			left.fork();
			ForkJoinCalculate right = new ForkJoinCalculate(middle+1, end);
			right.fork();
			return left.join() + right.join();
		}
	}
}
