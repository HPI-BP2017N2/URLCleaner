package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import de.hpi.urlcleaner.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UrlCleanerService implements IUrlCleanerService {

    @Getter(AccessLevel.PRIVATE) private static final Pattern STILL_ENCODED_REGEX = Pattern.compile("%[0-9a-f]{2}",
            Pattern.CASE_INSENSITIVE);

    private IdealoBridge idealoBridge;

    private TrackerCleanStrategy trackerCleanStrategy;

    public UrlCleanerService(RestTemplate oAuthRestTemplate, IdealoBridgeProperties properties, TrackerCleanStrategy
            trackerCleanStrategy) {
        setTrackerCleanStrategy(trackerCleanStrategy);
        setIdealoBridge(new IdealoBridge(oAuthRestTemplate, properties));
    }

    @Override
    public String clean(String dirtyUrl, long shopID) throws CouldNotCleanURLException {
        List<ICleanStrategy> strategies = Arrays.asList(
                new RedirectCleanStrategy(shopID, getIdealoBridge()),
                getTrackerCleanStrategy());
        try {
            String url = decodeUrl(dirtyUrl);
            for (ICleanStrategy strategy : strategies) {
                url = strategy.clean(url);
            }
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new CouldNotCleanURLException("Failed to clean url.\nCould not decode: " + dirtyUrl);
        }
    }

    //actions
    private String decodeUrl(String decodedUrl) throws UnsupportedEncodingException {
        String url = decodedUrl;
        while (getSTILL_ENCODED_REGEX().matcher(url).find()) {
            url = URLDecoder.decode(url, "UTF-8");
        }
        return url;
    }

}
