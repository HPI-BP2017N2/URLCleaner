package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;


@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class RedirectCleanStrategy implements ICleanStrategy {

    @Getter(AccessLevel.PRIVATE) private static final String WWW = "www.";

    private String rootUrlWithProtocol;

    private String rootUrlWithoutProtocol;

    RedirectCleanStrategy(String shopRootUrl) throws CouldNotCleanURLException {
        try {
            setRootUrlWithProtocol(shopRootUrl);
            setRootUrlWithoutProtocol(removeProtocolFromURL(getRootUrlWithProtocol()));
        } catch (MalformedURLException e) {
            throw new CouldNotCleanURLException(e.getMessage());
        }
    }

    /**
     * This methods tries to remove all redirects from the given url. If the shop root url cannot be found, a
     * <code>CouldNotCleanURLException</code> is raised. This can happen, if the url doesn´t belong to the specified
     * shop, or if an advanced redirect is used, which uses a cryptic string and a database to redirect to the proper
     * url.
     * @param dirtyUrl A url which possibly contains a redirect, that should get removed.
     * @return A url which links to the same page, as the passed <code>dirtyUrl</code>, but without redirect.
     * @throws CouldNotCleanURLException Thrown, if the shop root url is not contained within dirty url.
     */
    @Override
    public String clean(String dirtyUrl) throws CouldNotCleanURLException{
        String cleanedUrl = dirtyUrl;
        if (!cleanedUrl.contains(getRootUrlWithoutProtocol())) {
            throw new CouldNotCleanURLException("Failed to clean " + dirtyUrl + "\n" +
                    "Could not find rootUrl: " + getRootUrlWithoutProtocol());
        }
        cleanedUrl = cleanedUrl.substring(cleanedUrl.indexOf(getRootUrlWithoutProtocol()) + getRootUrlWithoutProtocol().length());
        cleanedUrl = removeLeadingSlashIfRootUrlEndsWithSlash(cleanedUrl);
        cleanedUrl = getRootUrlWithProtocol() + cleanedUrl;
        return cleanedUrl;
    }

    private String removeLeadingSlashIfRootUrlEndsWithSlash(String cleanedUrl) {
        if (getRootUrlWithProtocol().isEmpty() || cleanedUrl.isEmpty()) return cleanedUrl;
        if (getRootUrlWithProtocol().charAt(getRootUrlWithProtocol().length() - 1) == '/'
                && cleanedUrl.charAt(0) == '/')
            return cleanedUrl.substring(1);
        return cleanedUrl;
    }

    //actions
    private String removeProtocolFromURL(String cleanedUrl) throws MalformedURLException {
        URL url = new URL(cleanedUrl);
        String urlWithoutProtocol = url.getHost();
        return urlWithoutProtocol.startsWith(getWWW()) ?
                urlWithoutProtocol.substring(getWWW().length()) : urlWithoutProtocol;
    }

}
