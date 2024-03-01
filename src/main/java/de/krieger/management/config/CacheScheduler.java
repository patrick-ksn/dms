package de.krieger.management.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * Scheduler for evicting cached data at regular intervals.
 */
@Component
@EnableScheduling
public class CacheScheduler {
    private static final Logger log = LoggerFactory.getLogger(CacheScheduler.class);

    private static final long EVICT_CACHE_FOR_AUTHORS_TIME = 1000L * 60L * 60L; // 1h
    private static final long EVICT_CACHE_FOR_DOCUMENT_TIME = 1000L * 60L * 60L; // 1h
    /**
     * Evicts cached data for authors at regular intervals.
     */
    @CacheEvict(cacheNames = "authors", allEntries = true)
    @Scheduled(fixedRate = EVICT_CACHE_FOR_AUTHORS_TIME) // Schedule cache eviction every 10 minutes
    public void evictCacheAuthors() {
        // Method annotated with @CacheEvict to evict cache entries for authors cache
        log.debug("evict cache authors");
    }
    /**
     * Evicts cached data for documents at regular intervals.
     */
    @CacheEvict(cacheNames = "documents", allEntries = true)
    @Scheduled(fixedRate = EVICT_CACHE_FOR_DOCUMENT_TIME) // Schedule cache eviction every 10 minutes
    public void evictCacheDocuments() {
        // Method annotated with @CacheEvict to evict cache entries for document cache
        log.debug("evict cache documents");
    }
}