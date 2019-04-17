package xyz.fabiano.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class HalfClosedRange<T extends Comparable> implements Comparable {

    private T start;
    private T end;

    public HalfClosedRange(T start, T end) {
        if (start.compareTo(end) < 0) {
            this.start = start;
            this.end = end;
        } else {
            throw new IllegalArgumentException(String
                    .format("The value of the start [%s] should be smaller than the end [%s]", start, end));
        }
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    @Override
    public int compareTo(Object o) {
        HalfClosedRange range = (HalfClosedRange) o;
        return this.start.compareTo(range.start);
    }

    @Override
    public String toString() {
        return "HalfClosedRange{start=" + start + ", end=" + end + '}';
    }

    public Collection<HalfClosedRange<T>> minus(HalfClosedRange<T> range) {
        Collection<HalfClosedRange<T>> newRanges = new ArrayList<>();

        if (this.overlaps(range)) {
            if (this.end.compareTo(range.end) > 0) newRanges.add(new HalfClosedRange<>(range.end, this.end));
            if (this.start.compareTo(range.getStart()) < 0) newRanges.add(new HalfClosedRange<>(this.start, range.start));
        }

        return newRanges;
    }

    public boolean containsTotally(HalfClosedRange<T> range) {
        return start.compareTo(range.start) <= 0 && end.compareTo(range.end) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HalfClosedRange<?> that = (HalfClosedRange<?>) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    public boolean doesNotOverlap(HalfClosedRange<T> range) {
        return this.start.compareTo(range.end) >= 0 || range.start.compareTo(this.end) >= 0;
    }

    public boolean overlaps(HalfClosedRange<T> range) {
        return !doesNotOverlap(range);
    }
}