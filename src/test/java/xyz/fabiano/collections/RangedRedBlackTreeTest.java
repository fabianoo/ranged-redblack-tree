package xyz.fabiano.collections;

import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple test cases for different kinds of ranges.
 * Although this class does not cover every test scenario, we have helper classes inside the `xyz.fabiano.sample`
 *  package that runs test cases described inside the .txt files. To find more about, you can check the README.md
 */
public class RangedRedBlackTreeTest {

    @Test
    public void testWithTimeRanges() {

    }

    @Test
    public void testWithISODate() {
        RangedRedBlackTree<Instant> redBlackTree = new RangedRedBlackTree<>();
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2019-12-02T10:00:00Z"), Instant.parse("2019-12-03T10:15:00Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2019-12-05T11:00:00Z"), Instant.parse("2019-12-06T10:15:00Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2019-12-07T00:15:30Z"), Instant.parse("2019-12-20T10:00:00Z")));

        redBlackTree.minus(new ClosedRange<>(Instant.parse("2019-12-03T10:00:00Z"), Instant.parse("2019-12-05T12:00:00Z")));
        redBlackTree.minus(new ClosedRange<>(Instant.parse("2019-12-05T11:00:00Z"), Instant.parse("2019-12-07T12:00:00Z")));

        List<ClosedRange<Instant>> ranges = redBlackTree.everyNodeOrdered()
                .stream()
                .map(RangedRedBlackNode::getKey)
                .collect(Collectors.toList());

        ClosedRange<Instant> first = new ClosedRange<>(Instant.parse("2019-12-02T10:00:00Z"), Instant.parse("2019-12-03T10:00:00Z"));
        ClosedRange<Instant> second = new ClosedRange<>(Instant.parse("2019-12-07T12:00:00Z"), Instant.parse("2019-12-20T10:00:00Z"));

        assertEquals(2, ranges.size());
        assertEquals(first, ranges.get(0));
        assertEquals(second, ranges.get(1));
    }

    @Test
    public void testWithInteger() {
        RangedRedBlackTree<Integer> redBlackTree = new RangedRedBlackTree<>();
        redBlackTree.insert(new ClosedRange<>(10, 20));
        redBlackTree.insert(new ClosedRange<>(15, 30));
        redBlackTree.insert(new ClosedRange<>(40, 60));

        redBlackTree.minus(new ClosedRange<>(17, 25));
        redBlackTree.minus(new ClosedRange<>(25, 50));

        List<ClosedRange<Integer>> ranges = redBlackTree.everyNodeOrdered()
                .stream()
                .map(RangedRedBlackNode::getKey)
                .collect(Collectors.toList());

        ClosedRange<Integer> first = new ClosedRange<>(10,17);
        ClosedRange<Integer> second = new ClosedRange<>(15,17);
        ClosedRange<Integer> third = new ClosedRange<>(50,60);

        assertEquals(3, ranges.size());
        assertEquals(first, ranges.get(0));
        assertEquals(second, ranges.get(1));
        assertEquals(third, ranges.get(2));

    }

}