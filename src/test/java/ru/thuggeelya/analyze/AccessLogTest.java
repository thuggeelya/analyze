package ru.thuggeelya.analyze;

import org.junit.jupiter.api.Test;
import ru.thuggeelya.exceptions.AccessLogMatcherException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccessLogTest {

    @Test
    void tryParse() throws AccessLogMatcherException {
        String expectedLog = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT " +
                "/rest/v1.4/documents?zone=default&_rid=6076537c " +
                "HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0";
        AccessLog expectedAccessLog = new AccessLog(
                LocalDateTime.of(2017, 6, 14, 16, 47, 2),
                200,
                44.510983
        );
        assertEquals(expectedAccessLog, new AccessLog(expectedLog));

        String incorrectLog = "/rest/v1.4/documents?zone=default&_rid=6076537c";
        assertThrows(AccessLogMatcherException.class, () -> new AccessLog(incorrectLog));
    }
}