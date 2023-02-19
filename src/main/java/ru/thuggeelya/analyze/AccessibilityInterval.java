package ru.thuggeelya.analyze;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor
public class AccessibilityInterval implements Comparable<AccessibilityInterval> {

    @Setter
    private LocalDateTime start;
    @Setter
    private LocalDateTime end;
    private int okCount;
    private int failCount;
    @Getter
    private double accessibilityLevel;

    public AccessibilityInterval(LocalDateTime start, LocalDateTime end, double accessibilityLevel) {
        this.start = start;
        this.end = end;
        this.accessibilityLevel = accessibilityLevel;
    }

    // accepting 0% if okCount == 0
    private void calculateAccessibilityLevel() {
        accessibilityLevel = 100d * okCount / (okCount + failCount);
    }

    public void incCount(boolean isFailed) {
        if (isFailed) {
            failCount++;
        } else {
            okCount++;
        }
    }

    AccessibilityInterval concat(AccessibilityInterval interval) {
        if (interval != null) {
            setStart(Stream.of(start, interval.start).filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(start));
            setEnd(Stream.of(end, interval.end).filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(end));
            okCount += interval.okCount;
            failCount += interval.failCount;
            calculateAccessibilityLevel();
        }

        return this;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        NumberFormat decimalFormat = new DecimalFormat("#0.0");
        return start.format(dateTimeFormatter) + " " +
                end.format(dateTimeFormatter) + " " +
                decimalFormat.format(getAccessibilityLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, accessibilityLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        AccessibilityInterval interval = (AccessibilityInterval) o;
        return Objects.equals(start, interval.start)
                && Objects.equals(end, interval.end)
                && Objects.equals(accessibilityLevel, interval.accessibilityLevel);
    }

    @Override
    public int compareTo(AccessibilityInterval o) {
        return start.compareTo(o.start);
    }
}
