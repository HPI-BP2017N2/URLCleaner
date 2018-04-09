package de.hpi.urlcleaner.persistence.repositories;

import de.hpi.urlcleaner.persistence.BlacklistEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlacklistRepository extends MongoRepository<BlacklistEntry, Long> {

    BlacklistEntry findByShopID(long shopID);
}
