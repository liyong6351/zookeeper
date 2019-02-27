package com.liyong.example.zookeeper.utils;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>ClassName com.liyong.example.zookeeper.utils.ZkUtils              </p>
 * <p>Description                      </p>
 * <p>Author liyong                                  </p>
 * <p>Date 2019-02-27 22:34                           </p>
 **/
public class ZkCreateNode implements Watcher {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new ZkCreateNode());

        connectedSemaphore.await();
        String path1 = zooKeeper.create("/zk-test-1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success to create znode:" + path1);

        String path2 = zooKeeper.create("/zk-test-2","hello world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success to create znode:" + path2);
        Thread.sleep(50000L);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event:" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
