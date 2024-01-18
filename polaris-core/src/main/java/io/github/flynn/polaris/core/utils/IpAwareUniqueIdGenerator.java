package io.github.flynn.polaris.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class IpAwareUniqueIdGenerator implements UniqueIdGenerator {

  private final AtomicInteger counter;
  private final long ipAddressBits;

  public IpAwareUniqueIdGenerator() throws UnknownHostException {
    InetAddress localHost = InetAddress.getLocalHost();
    byte[] address = localHost.getAddress();
    this.counter = new AtomicInteger(0);
    ipAddressBits = ((address[2] & 0xFF) << 8) | (address[3] & 0xFF);
  }

  public long nextId() {
    return System.currentTimeMillis() << 22
        | (this.ipAddressBits << 6)
        | (long) (this.counter.getAndIncrement() & 0x3F);
  }

  public String nextId(String prefix) {
    return prefix + this.nextId();
  }
}
