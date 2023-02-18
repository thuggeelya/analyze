package ru.thuggeelya.analyze;

import java.util.*;

public class Analyzer {

    private final double maxRequestTime;
    private final double minAccessibilityLevel;
    private final Set<AccessibilityInterval> intervals = new TreeSet<>();
    private AccessibilityInterval interval;

    public Analyzer(double maxRequestTime, double minAccessibilityLevel) {
        this.maxRequestTime = maxRequestTime;
        this.minAccessibilityLevel = minAccessibilityLevel;
    }

    private boolean isFailed(AccessLog accessLog) {
        return (accessLog.getStatusCode() / 100 == 5) || (accessLog.getRequestTime() > maxRequestTime);
    }

    public void receive(AccessLog log) {
        AccessibilityInterval tempInterval = new AccessibilityInterval();
        tempInterval.setStart(log.getStartTime());
        tempInterval.setEnd(log.getStartTime());
        tempInterval.incCount(isFailed(log));
        interval = Optional.ofNullable(interval).orElse(new AccessibilityInterval()).concat(tempInterval);

        if (interval.getAccessibilityLevel() < minAccessibilityLevel) {
            intervals.add(interval);
            interval = null;
        }
    }

    public List<AccessibilityInterval> getFailIntervals() {
        return new ArrayList<>(intervals);
    }
}
