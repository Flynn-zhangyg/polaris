package io.github.flynn.polaris.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class IpAwareUniqueIdGenerator implements UniqueIdGenerator {

  private final byte[] address;
  private final AtomicInteger counter;

  public IpAwareUniqueIdGenerator() throws UnknownHostException {
    InetAddress localHost = InetAddress.getLocalHost();
    this.address = localHost.getAddress();
    this.counter = new AtomicInteger();
  }

  public long nextId() {
    return System.currentTimeMillis() << 22 | (long) ((this.address[2] & 255) << 14) |
        (long) ((this.address[3] & 255) << 6) | (long) (this.counter.getAndIncrement() & 63);
  }

  public String nextId(String prefix) {
    return prefix + this.nextId();
  }
}
