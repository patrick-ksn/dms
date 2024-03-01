package de.krieger.management.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;
/**
 * Configures caching for the application.
 */
@Component
@EnableCaching
public class CacheConfig
        implements CacheManagerCustomizer<ConcurrentMapCacheManager> {
    /**
     * Customize the cache manager with specific cache names.
     *
     * @param cacheManager the cache manager to customize
     */
    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(asList("authors", "documents"));
    }
}