package de.hpi.urlcleaner.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "blacklist")
@Getter
@RequiredArgsConstructor
public class BlacklistEntry {

    private static final int EXPIRES_AFTER_SECONDS = 60 * 60 * 24 * 30;

    @Field
    @Indexed(name = "createdTimestamp", expireAfterSeconds = EXPIRES_AFTER_SECONDS)
    private final Date created;

    private final long shopID;

}
