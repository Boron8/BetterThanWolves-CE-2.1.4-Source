package btw.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Spliterators;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class RandomSelector<T> {
   private final T[] elements;
   private final ToIntFunction<Random> selection;

   public static <T> RandomSelector<T> uniform(Collection<T> elements) throws IllegalArgumentException {
      Objects.requireNonNull(elements, "collection must not be null");
      final int size = elements.size();
      T[] els = (T[])elements.toArray(new Object[size]);
      return new RandomSelector<>(els, new ToIntFunction<Random>() {
         public int applyAsInt(Random r) {
            return r.nextInt(size);
         }
      });
   }

   public static <T> RandomSelector<T> weighted(Collection<T> elements, ToDoubleFunction<? super T> weighter) throws IllegalArgumentException {
      Objects.requireNonNull(elements, "elements must not be null");
      Objects.requireNonNull(weighter, "weighter must not be null");
      int size = elements.size();
      T[] elementArray = (T[])elements.toArray(new Object[size]);
      double totalWeight = 0.0;
      double[] discreteProbabilities = new double[size];

      for (int i = 0; i < size; i++) {
         double weight = weighter.applyAsDouble(elementArray[i]);
         discreteProbabilities[i] = weight;
         totalWeight += weight;
      }

      for (int i = 0; i < size; i++) {
         discreteProbabilities[i] /= totalWeight;
      }

      return new RandomSelector<>(elementArray, new RandomSelector.RandomWeightedSelection(discreteProbabilities));
   }

   RandomSelector(T[] elements, ToIntFunction<Random> selection) {
      this.elements = elements;
      this.selection = selection;
   }

   public T next(Random random) {
      return this.elements[this.selection.applyAsInt(random)];
   }

   public Stream<T> stream(Random random) {
      Objects.requireNonNull(random, "random must not be null");
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new RandomSelector.BaseIterator(random), 1040), false);
   }

   private class BaseIterator implements Iterator<T> {
      private final Random random;

      BaseIterator(Random random) {
         this.random = random;
      }

      @Override
      public boolean hasNext() {
         return true;
      }

      @Override
      public T next() {
         return RandomSelector.this.next(this.random);
      }
   }

   private static class RandomWeightedSelection implements ToIntFunction<Random> {
      private final double[] probabilities;
      private final int[] alias;

      RandomWeightedSelection(double[] probabilities) {
         int size = probabilities.length;
         double average = 1.0 / size;
         int[] small = new int[size];
         int smallSize = 0;
         int[] large = new int[size];
         int largeSize = 0;

         for (int i = 0; i < size; i++) {
            if (probabilities[i] < average) {
               small[smallSize++] = i;
            } else {
               large[largeSize++] = i;
            }
         }

         double[] pr = new double[size];
         int[] al = new int[size];
         this.probabilities = pr;
         this.alias = al;

         while (largeSize != 0 && smallSize != 0) {
            int less = small[--smallSize];
            int more = large[--largeSize];
            pr[less] = probabilities[less] * size;
            al[less] = more;
            probabilities[more] += probabilities[less] - average;
            if (probabilities[more] < average) {
               small[smallSize++] = more;
            } else {
               large[largeSize++] = more;
            }
         }

         while (smallSize != 0) {
            pr[small[--smallSize]] = 1.0;
         }

         while (largeSize != 0) {
            pr[large[--largeSize]] = 1.0;
         }
      }

      public int applyAsInt(Random random) {
         int column = random.nextInt(this.probabilities.length);
         return random.nextDouble() < this.probabilities[column] ? column : this.alias[column];
      }
   }
}
