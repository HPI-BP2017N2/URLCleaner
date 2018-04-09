package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import de.hpi.urlcleaner.exceptions.ShopBlacklistedException;

public interface IUrlCleanerService {

    String clean(String dirtyUrl, long shopID) throws CouldNotCleanURLException, ShopBlacklistedException;

}
