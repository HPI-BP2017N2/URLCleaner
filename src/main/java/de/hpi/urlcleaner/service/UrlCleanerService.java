package de.hpi.urlcleaner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hpi.urlcleaner.service.core.TrackerList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UrlCleanerService implements IUrlCleanerService {

    @Getter(AccessLevel.PRIVATE) private static final String ASSIGN_REGEX = "=";
    @Getter(AccessLevel.PRIVATE) private static final String TRACKERLIST_FILE = "tracker-list.json";
    @Getter(AccessLevel.PRIVATE) private static final String CASE_INSENSITIVE_FLAG = "(?i)";

    private List<String> delimiters;
    private List<Pattern> patterns;

    @Autowired
    public UrlCleanerService(ObjectMapper objectMapper) throws IOException {
        TrackerList trackerList = readTrackerList(objectMapper);
        buildRegexPatterns(trackerList);
        setDelimiters(trackerList.getDelimiters());
    }

    @Override
    public String cleanTrackers(String dirtyUrl) {
        String cleanedUrl = dirtyUrl;
        for (Pattern pattern : getPatterns()) {
            Matcher matcher = pattern.matcher(cleanedUrl);
            while (matcher.find()) {
                cleanedUrl = removeMatch(cleanedUrl, matcher);
                matcher = pattern.matcher(cleanedUrl);
            }
        }
        cleanedUrl = validateUrl(cleanedUrl);
        return cleanedUrl;
    }

    @Override
    public String cleanRedirects(long shopID, String url) {
        return null;
    }

    //action
    private String validateUrl(String cleanedUrl) {
        if (!cleanedUrl.contains("?") && cleanedUrl.contains("&")) {
            cleanedUrl = cleanedUrl.replaceFirst("&", "?");
        }
        return cleanedUrl;
    }

    private String removeMatch(String cleanedUrl, Matcher matcher) {
        return cleanedUrl.substring(0, matcher.start()) +
                cleanedUrl.substring(getNextDelimiter(cleanedUrl, matcher.end()), cleanedUrl.length());
    }

    private int getNextDelimiter(String cleanedUrl, int end) {
        for (String delimiter : getDelimiters()) {
            int index = cleanedUrl.indexOf(delimiter, end);
            if (isDelimiterFound(index)) {
                return index;
            }
        }
        return cleanedUrl.length();
    }


    private TrackerList readTrackerList(ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(
                getClass().getClassLoader().getResource(getTRACKERLIST_FILE()),
                TrackerList.class);
    }

    //initialization
    private void buildRegexPatterns(TrackerList trackerList) {
        setPatterns(new LinkedList<>());
        for (String delimiter : trackerList.getDelimiters()) {
            for (String tracker : trackerList.getTrackers()) {
                getPatterns().add(Pattern.compile(getCASE_INSENSITIVE_FLAG() +
                        delimiter + tracker + getASSIGN_REGEX()));
            }
        }
    }

    //conditionals
    private boolean isDelimiterFound(int index) {
        return index != -1;
    }
}
