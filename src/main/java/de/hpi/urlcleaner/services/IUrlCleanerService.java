package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;

public interface IUrlCleanerService {

    String clean(String dirtyUrl, long shopID) throws CouldNotCleanURLException;

}
