package de.hpi.urlcleaner.service.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TrackerList {

    private final List<String> delimiters;

    private final List<String> trackers;
}
