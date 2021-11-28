package com.high.ticket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TickerSeller03 implements Runnable{
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (Constant.ticketSum > 0) {
            try {
                lock.lock();
                sellTicket();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }

    private void sellTicket() {
        if (Constant.ticketSum > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 正在出售第  " + (Constant.ticketSum--)+ "张票");
        }
    }

    public static void main(String[] args) {
        // 创建资源对象
        TickerSeller03 task = new TickerSeller03();

        for(int i = 0; i < Constant.ticketSum/4 + 1; i++) {
            Thread windowThread1 = new Thread(task, "一号窗口");
            Thread windowThread2 = new Thread(task, "二号窗口");
            Thread windowThread3 = new Thread(task, "三号窗口");
            Thread windowThread4 = new Thread(task, "四号窗口");

            windowThread1.start();
            windowThread2.start();
            windowThread3.start();
            windowThread4.start();
        }

    }
}
