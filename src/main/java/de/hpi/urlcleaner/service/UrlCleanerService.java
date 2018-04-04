package de.hpi.urlcleaner.service;

import de.hpi.urlcleaner.service.core.ICleanStrategy;
import de.hpi.urlcleaner.service.core.TrackerCleanStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UrlCleanerService implements IUrlCleanerService {

    private List<ICleanStrategy> cleanStrategies;

    @Autowired
    public UrlCleanerService(TrackerCleanStrategy trackerCleanStrategy)  {
        setCleanStrategies(new LinkedList<>());
        getCleanStrategies().add(trackerCleanStrategy);
    }

    @Override
    public String clean(String dirtyUrl, long shopID) {
        String cleanUrl = dirtyUrl;
        for (ICleanStrategy strategy : getCleanStrategies()) {
            cleanUrl = strategy.clean(cleanUrl);
        }
        return cleanUrl;
    }

}
