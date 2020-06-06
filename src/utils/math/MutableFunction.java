package utils.math;

import java.util.function.*;

public interface MutableFunction extends Function<Integer,Double> {
    public boolean hasChanged();
}