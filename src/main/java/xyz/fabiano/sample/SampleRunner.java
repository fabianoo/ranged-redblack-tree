package xyz.fabiano.sample;

import xyz.fabiano.collections.ClosedRange;
import xyz.fabiano.collections.RangedRedBlackTree;

import java.time.Instant;

public class SampleRunner {

    public static void main(String[] args) {
        RangedRedBlackTree<Instant> redBlackTree = new RangedRedBlackTree<>();
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2012-12-02T10:15:30Z"), Instant.parse("2012-12-03T10:15:30Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2011-12-05T10:15:30Z"), Instant.parse("2011-12-06T10:15:30Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2011-12-07T10:15:30Z"), Instant.parse("2011-12-08T10:15:30Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2011-12-06T10:15:30Z"), Instant.parse("2011-12-10T11:15:30Z")));
        redBlackTree.insert(new ClosedRange<>(Instant.parse("2011-12-10T12:15:30Z"), Instant.parse("2011-12-10T15:15:30Z")));

        redBlackTree.printOrderedNodes();

        redBlackTree.minus(new ClosedRange<>(Instant.parse("2011-12-07T10:25:30Z"), Instant.parse("2011-12-08T20:15:30Z")));

        System.out.println("\n\n\n");

        redBlackTree.printOrderedNodes();

    }
}
