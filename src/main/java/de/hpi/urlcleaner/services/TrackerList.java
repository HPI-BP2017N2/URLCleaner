package de.hpi.urlcleaner.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
class TrackerList {

    private List<String> delimiters;

    private List<String> trackers;
}
