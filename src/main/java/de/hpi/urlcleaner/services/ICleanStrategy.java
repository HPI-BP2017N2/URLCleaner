package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;

public interface ICleanStrategy {

    String clean(String dirtyUrl) throws CouldNotCleanURLException;
}
