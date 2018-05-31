package de.hpi.urlcleaner.api;

import de.hpi.urlcleaner.dto.CleanResponse;
import de.hpi.urlcleaner.dto.SuccessResponse;
import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import de.hpi.urlcleaner.exceptions.ShopBlacklistedException;
import de.hpi.urlcleaner.services.IUrlCleanerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UrlCleanerController {

    private final IUrlCleanerService urlCleanerService;

    @ApiOperation(value = "Remove all click trackers and redirects from given url", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed trackers and redirects."),
            @ApiResponse(code = 403, message = "The shop is blacklisted. The link won´t get cleaned."),
            @ApiResponse(code = 500, message = "The shop root url wasn´t found within the given link.")})

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @RequestMapping(value = "/clean/{shopID}", method = GET, produces = "application/json")
    public HttpEntity<Object> clean(@PathVariable long shopID, @RequestParam String url,
                                    @RequestParam(required = false) Optional<String> shopRootUrl)
            throws CouldNotCleanURLException, ShopBlacklistedException {
        String cleanUrl;
        if (shopRootUrl.isPresent()) cleanUrl = getUrlCleanerService().clean(url, shopID, shopRootUrl.get());
        else cleanUrl = getUrlCleanerService().clean(url, shopID);
        return new SuccessResponse<>(new CleanResponse(cleanUrl)).withMessage("Url cleaned successfully.").send();
    }
}
