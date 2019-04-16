package xyz.fabiano.sample;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TestTimeIntervals extends AbstractTesterHelper<LocalTime> {

    private static final String TIME_PATTERN = "H:mm";
    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(TIME_PATTERN)
            .withZone(ZoneId.systemDefault());

    @Override
    LocalTime convertData(String rawValue) {
        return LocalTime.parse(rawValue, formatter);
    }

    @Override
    String testCasesFileName() {
        return "test-time-intervals.txt";
    }
}