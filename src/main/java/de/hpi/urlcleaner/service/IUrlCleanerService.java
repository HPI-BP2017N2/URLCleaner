package de.hpi.urlcleaner.service;

public interface IUrlCleanerService {

    String cleanTrackers(String dirtyUrl);

    String cleanRedirects(long shopID, String url);
}
