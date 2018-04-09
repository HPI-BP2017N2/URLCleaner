package de.hpi.urlcleaner.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blacklist")
@Getter
@RequiredArgsConstructor
public class BlacklistEntry {

    private final long shopID;

}
