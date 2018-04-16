package de.hpi.urlcleaner.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CleanResponse {

    private final String url;
}
