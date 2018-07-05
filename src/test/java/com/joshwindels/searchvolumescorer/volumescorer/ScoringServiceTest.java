package com.joshwindels.searchvolumescorer.volumescorer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScoringServiceTest {

    private ScoringService scoringService;

    @Before
    public void setUp() {
        scoringService = new ScoringService();
    }

    @Test
    public void givenSearchResultsSet_whenLeafCounting_thenStandardisedScoreIsReturned() {
        SearchResult levelOneResult = new SearchResult(0, "linux", Arrays.asList("Linux book", "Linux computer"));
        SearchResult levelTwoResultA = new SearchResult(1, "linux book", Arrays.asList("Linux book", "Linux book for dummies"));
        SearchResult levelTwoResultB = new SearchResult(1, "linux computer", Arrays.asList("Linux computer mag", "Linux computer game"));

        List<SearchResult> resultSet = Arrays.asList(levelOneResult, levelTwoResultA, levelTwoResultB);

        assertEquals(4, scoringService.getLeafCountScoreFromResults(resultSet));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullResultsSet_whenLeafCounting_thenNullPointerExceptionIsThrown() {
        scoringService.getLeafCountScoreFromResults(null);
    }

}