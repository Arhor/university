package by.bsu.uir.university.util;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Lazy<T> implements RichSupplier<T> {

  private Supplier<T> source;
  private boolean computed;
  private T value;

  private Lazy(final Supplier<T> source) {
    this.source = source;
  }

  public static <T> Lazy<T> eval(final Supplier<T> source) {
    Objects.requireNonNull(source, "Lazy evaluation source must not be null");
    return new Lazy<>(source);
  }

  public T get() {
    if (!computed) {
      synchronized (this) {
        compute();
      }
    }
    return value;
  }

  private void compute() {
    if (!computed) {
      value = source.get();
      // Nullifying source reference allows to release associated scope and prevent memory leaks
      source = null;
      computed = true;
    }
  }

  public boolean isComputed() {
    return computed;
  }

  @Override
  public <R> Lazy<R> map(final Function<T, R> f) {
    return eval(() -> f.apply(this.get()));
  }

}
