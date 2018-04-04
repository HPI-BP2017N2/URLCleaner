package de.hpi.urlcleaner.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class TrackerCleanStrategyTest {

    private ICleanStrategy strategy;

    @Before
    public void setup() throws IOException {
        setStrategy(new TrackerCleanStrategy(new ObjectMapper()));
    }

    @Test
    public void testSingleTracker() {
        String dirtyUrl = "https://www.silvertrade" +
                ".de/tintenpatrone-schwarz-kompatibel-11ml-mit-chip-1426744a?sPartner=idealo";
        String expectedUrl = "https://www.silvertrade" +
                ".de/tintenpatrone-schwarz-kompatibel-11ml-mit-chip-1426744a";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

    @Test
    public void testRegexTracker() {
        String dirtyUrl = "https://www.aze-tuning.de/sitzbezug-sitzbezuege-mitsubishi-carisma" +
                ".html?utm_source=web&utm_medium=cpc&utm_campaign=idealo";
        String expectedUrl = "https://www.aze-tuning.de/sitzbezug-sitzbezuege-mitsubishi-carisma" +
                ".html";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

    @Test
    public void testLeaveOtherParams() {
        String dirtyUrl = "https://www.mega-bikes" +
                ".de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach:15144.html?ref=6&rabatt=true";
        String expectedUrl = "https://www.mega-bikes.de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach:15144.html?rabatt=true";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

    @Test
    public void testMultipleTracker() {
        String dirtyUrl = "https://www.mega-bikes" +
                ".de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach:15144.html?utm_medium=cpc&ref=6" +
                "&rabatt=true&utm_campaign=idealo";
        String expectedUrl = "https://www.mega-bikes.de/krg-shimano-ultegra-3953-zaehne-1725mm-fc-6800-hollowtech-ii-m-achse-11-fach:15144.html?rabatt=true";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

    @Test
    public void testMixedDelimiters() {
        String dirtyUrl = "http://www.amazon.de/dp/B00UI7XAJ8/?smid=A12ZUNPYXJYAM9";
        String expectedUrl = "http://www.amazon.de/dp/B00UI7XAJ8/";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

    @Test
    public void testUpperCaseUntouched() {
        String dirtyUrl = "http://www.amazon.de/dp/B00UI7XAJ8";
        String expectedUrl = "http://www.amazon.de/dp/B00UI7XAJ8";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }
}