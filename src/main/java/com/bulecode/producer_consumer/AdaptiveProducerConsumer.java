package com.bulecode.producer_consumer;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 使用ConcurrentLinkedQueue实现一个高效自适应生产者消费者模式
 * 简单demo 正式使用时线程请使用类
 * Author: buleCode
 * Date: 2017/12/24
 */
public class AdaptiveProducerConsumer {

    public static void main(String[] args) {
        AdaptiveProducerConsumer adaptiveProducerConsumer = new AdaptiveProducerConsumer();
        adaptiveProducerConsumer.execute();
    }



    public void execute() {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();

        int producerNum = 1;
        int consumerNum = 2;

        ExecutorService producerPool = Executors.newFixedThreadPool(producerNum);
        ExecutorService consumerPool = Executors.newFixedThreadPool(consumerNum);

        for (int i = 0; i < producerNum; i++) {
            producerPool.submit(() -> {
                int ctr = 0;
                while (true) {
                    for (int j = 0; j < 10; j++) {
                        int e = ctr + j;
                        queue.offer(e);
                        System.out.println("生产 ---- " + e);
                    }
                    ctr += 10;
                    synchronized (queue) {
                        queue.notifyAll();
                    }
                    Thread.sleep(new Random().nextInt(10000));
                }
            });
        }


        for (int i = 0; i < consumerNum; i++) {
            consumerPool.submit(() -> {
                while (true) {
                    while (!queue.isEmpty()) {
                        Integer temp = queue.poll();
                        System.out.println("消费　---- " + temp);
                        Thread.sleep(500);
                    }
                    synchronized (queue){
                        queue.wait();
                    }
                }
            });
        }
    }



}
