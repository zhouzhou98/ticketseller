### 多线程实现火车票贩卖  
使用三种方法实现：  
（1）基于runnable atomicInteger  
（2）基于runnable synchronized  
（3）基于runnable reentrantlock  

具体代码：  
```java
package com.high.ticket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TickerSeller01 implements Runnable{
    public static AtomicInteger ticketSum = new AtomicInteger(Constant.ticketSum);

    @Override
    public void run() {
        int count = 0;
        while ((count=ticketSum.decrementAndGet()) >= 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "卖出了第" + ++count + "张票");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new TickerSeller01(), "一号窗口");
        Thread t2 = new Thread(new TickerSeller01(), "二号窗口");
        Thread t3 = new Thread(new TickerSeller01(), "三号窗口");
        Thread t4 = new Thread(new TickerSeller01(), "四号窗口");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
```


```java
package com.high.ticket;

import java.util.concurrent.TimeUnit;

public class TickerSeller02 implements Runnable{
    private int index = 1;
    private boolean change = true;

    private final static Object object = new Object();
    @Override
    public void run() {
        synchronized (object) {
            while (index <= Constant.ticketSum && change) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第： " + (index++) + "张票");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                change = false;
            }
            change = true;
        }
    }
    public static void main(String[] args) {
        final TickerSeller02 task = new TickerSeller02();
        for (int i = 0; i < Constant.ticketSum / 4 + 1; i++) {
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
```

```java
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
```
