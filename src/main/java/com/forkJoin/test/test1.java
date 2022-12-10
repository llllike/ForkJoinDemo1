package com.forkJoin.test;

import com.forkJoin.task.ForkJoinAction;
import com.forkJoin.task.ForkJoinCalculate;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * @author yzy
 * @create 2022-12-10-13:19
 */
public class test1 {
    @Test
    public void test1(){
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCalculate(0L, 100000000000L);
        long sum = pool.invoke(task);
        long end = System.currentTimeMillis();
        long sum1=0;
        for (long i = 0; i < 100000000000L; i++) {
            sum1+=i;
        }
        long end2=System.currentTimeMillis();
        System.out.println(sum1);
        System.out.println(sum);
        //耗费的时间为: 8840
        //耗费的时间为: 28091
        System.out.println("耗费的时间为: " + (end - start));
        System.out.println("耗费的时间为: " + (end2 - end));
    }
    @Test
    public void test5(){
        ForkJoinAction forkJoinAction = new ForkJoinAction(new File("D:/"), "java");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //execute方法是异步的
        forkJoinPool.execute(forkJoinAction);
        //阻塞，等待ForkJoin执行完，主线程才往下执行
        forkJoinAction.join();
    }
}
