package ru.thuggeelya.analyze;

import org.junit.jupiter.api.Test;
import ru.thuggeelya.exceptions.AccessLogMatcherException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyzerTest {

    Analyzer analyzer = new Analyzer(70d, 99d);

    @Test
    void checkIntervals() {
        String fileName = "src/test/resources/access.log";

        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String log;

            while ((log = input.readLine()) != null) {
                analyzer.receive(new AccessLog(log));
            }
        } catch (IOException | AccessLogMatcherException e) {
            e.printStackTrace();
        }

        List<AccessibilityInterval> expectedList = new ArrayList<>();
        expectedList.add(new AccessibilityInterval(
                        LocalDateTime.of(2017, 6, 14, 16, 47, 2),
                        LocalDateTime.of(2017, 6, 14, 16, 47, 9),
                        50.0d));
        expectedList.add(new AccessibilityInterval(
                        LocalDateTime.of(2017, 6, 14, 16, 47, 12),
                        LocalDateTime.of(2017, 6, 14, 16, 48, 52),
                        95.0d));
        assertEquals(expectedList, analyzer.getFailIntervals());
    }
}