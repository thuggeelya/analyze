package ru.thuggeelya.analyze;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.thuggeelya.exceptions.AccessLogMatcherException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class AccessLog {
    private LocalDateTime startTime;
    private int statusCode;
    private double requestTime;

    private static final Pattern PATTERN =
            Pattern.compile("^.*\\[(\\d\\d/\\d\\d/\\d{4}:\\d\\d:\\d\\d:\\d\\d)\\s\\+\\d{4}].+\".+\"\\s(\\d{3})\\s\\d+\\s(\\d+\\.?\\d+).*$");

    public AccessLog(String log) throws AccessLogMatcherException {
        Matcher matcher = PATTERN.matcher(log);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss");

        try {
            matcher.find();
            startTime = LocalDateTime.parse(matcher.group(1), formatter);
            statusCode = Integer.parseInt(matcher.group(2));
            requestTime = Double.parseDouble(matcher.group(3));
        } catch (IllegalStateException e) {
            throw new AccessLogMatcherException("Failed parsing log: " + log);
        }
    }
}
