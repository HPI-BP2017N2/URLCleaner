package de.hpi.urlcleaner.services;

import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class RedirectCleanStrategyTest {

    @Getter(AccessLevel.PRIVATE) private static final long EXAMPLE_SHOP_ID = 20740;
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_SHOP_ROOTURL = "https://esprit.de";


    private RedirectCleanStrategy strategy;

    @Before
    public void setup() throws CouldNotCleanURLException {
        setStrategy(new RedirectCleanStrategy(getEXAMPLE_SHOP_ROOTURL()));
    }

    @Test(expected = CouldNotCleanURLException.class)
    public void dirtyUrlDoesNotContainRootUrl() throws CouldNotCleanURLException {
        getStrategy().clean("http://www.cptracker.de/blumblum");
    }

    @Test
    public void dirtyUrlContainsRootUrl() throws CouldNotCleanURLException {
        String dirtyUrl = "http://www.cptracker.de/redir?www.esprit" +
                ".de/testproduct";
        String expectedUrl = "https://esprit.de/testproduct";
        assertEquals(expectedUrl, getStrategy().clean(dirtyUrl));
    }

}