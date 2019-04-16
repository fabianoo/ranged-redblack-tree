package xyz.fabiano.sample;

public class TestIntegerIntervals extends AbstractTesterHelper<Integer> {

    @Override
    String testCasesFileName() {
        return "test-int-intervals.txt";
    }

    @Override
    Integer convertData(String rawValue) {
        return Integer.valueOf(rawValue);
    }
}