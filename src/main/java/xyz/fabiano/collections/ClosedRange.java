package xyz.fabiano.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ClosedRange<T extends Comparable> implements Comparable {

    private T start;
    private T end;

    public ClosedRange(T start, T end) {
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
        ClosedRange range = (ClosedRange) o;
        return this.start.compareTo(range.start);
    }

    @Override
    public String toString() {
        return "ClosedRange{start=" + start + ", end=" + end + '}';
    }

    public Collection<ClosedRange<T>> minus(ClosedRange<T> range) {
        Collection<ClosedRange<T>> newRanges = new ArrayList<>();

        if (this.overlaps(range)) {
            if (this.end.compareTo(range.end) > 0) newRanges.add(new ClosedRange<>(range.end, this.end));
            if (this.start.compareTo(range.getStart()) < 0) newRanges.add(new ClosedRange<>(this.start, range.start));
        }

        return newRanges;
    }

    public boolean containsTotally(ClosedRange<T> range) {
        return start.compareTo(range.start) <= 0 && end.compareTo(range.end) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClosedRange<?> that = (ClosedRange<?>) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    public boolean doesNotOverlap(ClosedRange<T> range) {
        return this.start.compareTo(range.end) >= 0 || range.start.compareTo(this.end) >= 0;
    }

    public boolean overlaps(ClosedRange<T> range) {
        return !doesNotOverlap(range);
    }
}