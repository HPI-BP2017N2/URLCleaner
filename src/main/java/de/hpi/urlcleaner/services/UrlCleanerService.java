package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import de.hpi.urlcleaner.exceptions.ShopBlacklistedException;
import de.hpi.urlcleaner.persistence.BlacklistEntry;
import de.hpi.urlcleaner.persistence.repositories.IBlacklistRepository;
import de.hpi.urlcleaner.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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

    private IBlacklistRepository blacklistRepository;

    private IdealoBridge idealoBridge;

    private TrackerCleanStrategy trackerCleanStrategy;

    @Autowired
    public UrlCleanerService(RestTemplate oAuthRestTemplate, IdealoBridgeProperties properties, TrackerCleanStrategy
            trackerCleanStrategy, IBlacklistRepository blacklistRepository) {
        setBlacklistRepository(blacklistRepository);
        setTrackerCleanStrategy(trackerCleanStrategy);
        setIdealoBridge(new IdealoBridge(oAuthRestTemplate, properties));
    }

    @Override
    public String clean(String dirtyUrl, long shopID) throws CouldNotCleanURLException, ShopBlacklistedException {
        if (isBlacklisted(shopID)) {
            throw new ShopBlacklistedException("Shop with id " + shopID + " is blacklisted!");
        }

        List<ICleanStrategy> strategies = Arrays.asList(
                new RedirectCleanStrategy(shopID, getIdealoBridge()),
                getTrackerCleanStrategy());
        return clean(dirtyUrl, strategies);
    }

    //actions
    private String clean(String dirtyUrl, List<ICleanStrategy> strategies) throws CouldNotCleanURLException {
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

    private String decodeUrl(String decodedUrl) throws UnsupportedEncodingException {
        String url = decodedUrl;
        while (getSTILL_ENCODED_REGEX().matcher(url).find()) {
            url = URLDecoder.decode(url, "UTF-8");
        }
        return url;
    }

    private boolean isBlacklisted(long shopID) {
        return getBlacklistRepository().findByShopID(shopID) != null;
    }

}
