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
public class ScoreCalculatingServiceTest {

    private static final String KEYWORD = "linux";

    @Mock
    SearchResultService searchResultService;

    @InjectMocks ScoreCalculatingService scoreCalculatingService;

    @Test
    public void givenKeyword_whenCalculatingNetworkScore_thenScoreIsReturned() {
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

        int actualScore = scoreCalculatingService.calculateNetworkScoreForKeyword(KEYWORD);

        assertEquals(4, actualScore);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullKeyword_whenCalculatingNetworkScore_thenNullPointerExceptionThrown() {
        scoreCalculatingService.calculateNetworkScoreForKeyword(null);
    }

    @Test(expected = RuntimeException.class)
    public void givenKeyword_whenCalculatingNetworkScoreAndSearchResultServiceThrowsRuntimeException_thenRuntimeExceptionThrown() {
        when(searchResultService.getSearchResultForKeyword(KEYWORD)).thenThrow(RuntimeException.class);

        scoreCalculatingService.calculateNetworkScoreForKeyword(KEYWORD);
    }

    @Test
    public void givenKeyword_whenCalculatingSearchVolumeScore_thenScoreIsReturned() {
        when(searchResultService.getSearchResultForKeyword(KEYWORD))
                .thenReturn(new SearchResult(KEYWORD, Arrays.asList(KEYWORD, "linux computer", "linux book")));
        String shortenedTerm = "linu";
        when(searchResultService.getSearchResultForKeyword(shortenedTerm))
                .thenReturn(new SearchResult(shortenedTerm, Arrays.asList()));

        int actualScore = scoreCalculatingService.calculateSearchVolumeScoreForKeyword(KEYWORD);

        assertEquals(20, actualScore);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullKeyword_whenCalculatingSearchVolumeScore_thenNullPointerExceptionThrown() {
        scoreCalculatingService.calculateSearchVolumeScoreForKeyword(null);
    }

    @Test(expected = RuntimeException.class)
    public void givenKeyword_whenCalculatingSearchVolumekScoreAndSearchResultServiceThrowsRuntimeException_thenRuntimeExceptionThrown() {
        when(searchResultService.getSearchResultForKeyword(KEYWORD)).thenThrow(RuntimeException.class);

        scoreCalculatingService.calculateSearchVolumeScoreForKeyword(KEYWORD);
    }

}
