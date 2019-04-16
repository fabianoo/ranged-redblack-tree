package xyz.fabiano.sample;

import xyz.fabiano.collections.ClosedRange;
import xyz.fabiano.collections.RangedRedBlackNode;
import xyz.fabiano.collections.RangedRedBlackTree;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class helps us to test cases without actually writing code. As a DSL, but not as powerful, it can read
 * the file of a given type and process their test cases.
 *
 * @param <T> the type of the data in the ranges.
 */
public abstract class AbstractTesterHelper<T extends Comparable<T>> {

    private static final Logger LOGGER = Logger.getLogger(AbstractTesterHelper.class.getName());

    private static final Pattern TEST_CASE_LINE_PATTERN = Pattern.compile("expect (\\[.+\\]) with (\\[.+\\]) minus (\\[.+\\])");
    private static final Pattern RANGE_GROUP_PATTERN = Pattern.compile("[\\w:]+\\.{2}[\\w:]+");
    private static final String RANGE_SPLIT = "\\.\\.";

    abstract String testCasesFileName();

    abstract T convertData(String rawValue);

    public void runAssertions() throws IOException, URISyntaxException {
        Collection<TestCase<T>> testCases = loadTestCases();

        testCases.forEach(testCase -> {
            LOGGER.info("Running: " + testCase);
            RangedRedBlackTree<T> tree = RangedRedBlackTree.createTree(testCase.minuend);

            testCase.subtrahend.forEach(tree::minus);

            Collection<RangedRedBlackNode<T>> nodes = tree.everyNodeOrdered();

            if (nodes.size() != testCase.difference.size()) ;

            testCase.difference.forEach(expected -> {
                if (nodes.stream().noneMatch(node -> node.getKey().equals(expected))) {
                    thowAssertionError(testCase.difference, nodes);
                }
            });
            LOGGER.info("Success");
        });
    }

    private void thowAssertionError(Collection<ClosedRange<T>> expected, Collection<RangedRedBlackNode<T>> actual) {
        List<ClosedRange<T>> actualRange = actual.stream().map(a -> a.getKey()).collect(Collectors.toList());
        throw new AssertionError(" Does not satisfy the expectation: " +
                "\nexpected: " + expected + "\n  actual: " + actualRange);
    }

    Collection<TestCase<T>> loadTestCases() throws URISyntaxException, IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(testCasesFileName()).toURI());
        Collection<TestCase<T>> testCases = new ArrayList<>();
        Files.readAllLines(path).forEach(line -> {
            Matcher matcher = TEST_CASE_LINE_PATTERN.matcher(line);
            if (matcher.matches()) {
                Collection<ClosedRange<T>> expected = loadGroups(matcher.group(1));
                Collection<ClosedRange<T>> minuend = loadGroups(matcher.group(2));
                Collection<ClosedRange<T>> subtrahend = loadGroups(matcher.group(3));
                testCases.add(new TestCase<>(expected, minuend, subtrahend));
            }
        });

        return testCases;
    }

    private Collection<ClosedRange<T>> loadGroups(String groupStr) {
        Collection<ClosedRange<T>> ranges = new ArrayList<>();

        Matcher matcher = RANGE_GROUP_PATTERN.matcher(groupStr);
        while (matcher.find()) {
            String group = matcher.group();
            String[] split = group.split(RANGE_SPLIT);
            ranges.add(new ClosedRange<>(convertData(split[0]), convertData(split[1])));
        }
        return ranges;
    }

    class TestCase<C extends Comparable> {
        private Collection<ClosedRange<C>> difference;
        private Collection<ClosedRange<C>> minuend;
        private Collection<ClosedRange<C>> subtrahend;

        public TestCase(Collection<ClosedRange<C>> difference,
                        Collection<ClosedRange<C>> minuend,
                        Collection<ClosedRange<C>> subtrahend) {

            this.difference = difference;
            this.minuend = minuend;
            this.subtrahend = subtrahend;
        }

        @Override
        public String toString() {
            return "TestCase{difference=" + difference + ", minuend=" + minuend + ", subtrahend=" + subtrahend + '}';
        }
    }
}