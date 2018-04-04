package de.hpi.urlcleaner.api;

import de.hpi.urlcleaner.service.IUrlCleanerService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UrlCleanerController {

    private final IUrlCleanerService urlCleanerService;

    @RequestMapping(value = "/clean/{shopID}", method = GET)
    public String clean(@PathVariable long shopID, @RequestParam String url) {
        return getUrlCleanerService().cleanRedirects(shopID, url);
    }
}
