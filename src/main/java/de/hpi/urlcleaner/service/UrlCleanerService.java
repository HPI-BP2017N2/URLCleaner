package de.hpi.urlcleaner.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UrlCleanerService implements IUrlCleanerService {

    @Override
    public String cleanUrl(String dirtyUrl) {

        return null;
    }
}
