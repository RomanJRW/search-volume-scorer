package com.joshwindels.searchvolumescorer.volumescorer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchVolumeEstimatingServiceTest {

    private static final String KEYWORD = "linux";

    @Mock
    SearchResultService searchResultService;

    @InjectMocks
    SearchVolumeEstimatingService searchVolumeEstimatingService;

    @Test
    public void givenKeyword_whenCalculatingScore_thenScoreIsReturned() {
        String SUGGESTED_TERM_A = "linux computer";
        String SUGGESTED_TERM_B = "linux book";
        SearchResult initialSearchResult = new SearchResult(KEYWORD, Arrays.asList(SUGGESTED_TERM_A, SUGGESTED_TERM_B));
        SearchResult secondarySearchResultA
                = new SearchResult(SUGGESTED_TERM_A, Arrays.asList(SUGGESTED_TERM_A + "mag", SUGGESTED_TERM_A + "guide"));
        SearchResult secondarySearchResultB
                = new SearchResult(SUGGESTED_TERM_B, Arrays.asList(SUGGESTED_TERM_B + "club", SUGGESTED_TERM_B + "shop"));

        when(searchResultService.getSearchResultForKeyword(KEYWORD)).thenReturn(initialSearchResult);
        when(searchResultService.getSearchResultForKeyword(SUGGESTED_TERM_A)).thenReturn(secondarySearchResultA);
        when(searchResultService.getSearchResultForKeyword(SUGGESTED_TERM_B)).thenReturn(secondarySearchResultB);

        int actualScore = searchVolumeEstimatingService.calculateScoreForKeyword(KEYWORD);

        assertEquals(4, actualScore);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullKeyword_whenCalculatingScore_thenNullPointerExceptionThrown() {
        searchVolumeEstimatingService.calculateScoreForKeyword(null);
    }

    @Test(expected = RuntimeException.class)
    public void givenKeyword_whenCalculatingScoreAndSearchResultServiceThrowsRuntimeException_thenRuntimeExceptionThrown() {
        when(searchResultService.getSearchResultForKeyword(KEYWORD)).thenThrow(RuntimeException.class);

        searchVolumeEstimatingService.calculateScoreForKeyword(KEYWORD);
    }

}