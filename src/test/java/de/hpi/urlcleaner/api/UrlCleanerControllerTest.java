package de.hpi.urlcleaner.api;

import de.hpi.urlcleaner.services.UrlCleanerService;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@Getter(AccessLevel.PRIVATE)
public class UrlCleanerControllerTest {

    @Getter(AccessLevel.PRIVATE) private static final String DIRTY_URL = "https://www.mega-bikes.de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach" +
            ":15144.html?ref=6&rabatt=true";
    @Getter(AccessLevel.PRIVATE) private static final String CLEAN_URL = "https://www.mega-bikes" +
            ".de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach:15144.html?rabatt=true";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlCleanerService urlCleanerService;

    @Test
    public void cleanHappyPath() throws Exception {
        doReturn(getCLEAN_URL()).when(getUrlCleanerService()).clean(getDIRTY_URL(), 0L);
        getMockMvc()
                .perform(get("/clean/0").param("url", getDIRTY_URL()))
                .andExpect(jsonPath("data.url").value(getCLEAN_URL()))
                .andExpect(status().isOk());
    }

}