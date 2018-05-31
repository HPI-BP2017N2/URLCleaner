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
import java.util.Date;
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

    /**
     * Applies all cleaning strategies to a given url. Currently their are only two strategies:
     * <code>{@link RedirectCleanStrategy}</code> and <code>{@link TrackerCleanStrategy}</code>
     * @param dirtyUrl The url that should get cleaned from tracking information.
     * @param shopID The id that identifies the shop, the url belongs to.
     * @return A url which points to the same web page, but does not contain any click trackers.
     * @throws CouldNotCleanURLException Thrown, if the url could not get cleaned.
     * @throws ShopBlacklistedException Thrown, if shop is blacklisted.
     */
    @Override
    public String clean(String dirtyUrl, long shopID) throws CouldNotCleanURLException, ShopBlacklistedException {
        return clean(dirtyUrl, shopID, getIdealoBridge().resolveShopIDToRootUrl(shopID));
    }


    /**
     * Does the same as {@link #clean(String, long)} but uses a passed shop root url instead of calling the idealo
     * bridge.
     * @param dirtyUrl The url that should get cleaned from tracking information.
     * @param shopID The id which is required to check if shop is blacklisted.
     * @param shopRootUrl The root url of the shop, the dirty url belongs to.
     * @return A url which points to the same web page, but does not contain any click trackers.
     * @throws CouldNotCleanURLException Thrown, if the url could not get cleaned.
     * @throws ShopBlacklistedException Thrown, if shop is blacklisted.
     */
    @Override
    public String clean(String dirtyUrl, long shopID, String shopRootUrl) throws CouldNotCleanURLException,
            ShopBlacklistedException {
        if (isBlacklisted(shopID)) {
            throw new ShopBlacklistedException("Shop with id " + shopID + " is blacklisted!");
        }

        List<ICleanStrategy> strategies = Arrays.asList(
                new RedirectCleanStrategy(getTrackerCleanStrategy().clean(shopRootUrl)),
                getTrackerCleanStrategy());
        try {
            return clean(dirtyUrl, strategies);
        } catch (CouldNotCleanURLException e) {
            getBlacklistRepository().save(new BlacklistEntry(new Date(), shopID));
            throw e;
        }
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
