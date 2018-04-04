package de.hpi.urlcleaner.service.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrackerList {

    private List<String> delimiters;

    private List<String> trackers;
}
