package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.dto.ShopIDToRootUrlResponse;
import de.hpi.urlcleaner.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;


    @Retryable(
      value = { HttpClientErrorException.class },
      backoff = @Backoff(delay = 3000))
    String resolveShopIDToRootUrl(long shopID) {
        return getOAuthRestTemplate().getForObject(getShopIDToRootUrlURI(shopID), ShopIDToRootUrlResponse
                .class).getShopUrl();
    }

    private URI getShopIDToRootUrlURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getShopIDToRootUrlRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }
}
