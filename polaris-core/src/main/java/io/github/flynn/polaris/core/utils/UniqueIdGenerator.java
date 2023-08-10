package io.github.flynn.polaris.core.utils;

public interface UniqueIdGenerator {
  long nextId();
  String nextId(String prefix);
}
