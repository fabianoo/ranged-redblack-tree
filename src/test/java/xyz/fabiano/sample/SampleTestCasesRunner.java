package xyz.fabiano.sample;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Simple Test Runner using the support classes for each time of data ranges.
 */
public class SampleTestCasesRunner {

    @Test
    public void test() throws IOException, URISyntaxException {
        new TestTimeIntervals().runAssertions();
        new TestIntegerIntervals().runAssertions();
    }
}
