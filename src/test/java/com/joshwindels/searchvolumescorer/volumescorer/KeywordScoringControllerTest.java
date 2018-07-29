package com.joshwindels.searchvolumescorer.volumescorer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KeywordScoringControllerTest {

    private static final String KEYWORD = "linux";
    private static final String KEYWORD_KEY = "keyword";
    private static final String SCORE_KEY = "score";

    @Mock ScoreCalculatingService scoreCalculatingService;

    @InjectMocks KeywordScoringController keywordScoringController;

    @Test
    public void givenKeyword_whenCalculatingNetworkScoreForKeyword_parameterisedMapIsReturned() {
        when(scoreCalculatingService.calculateNetworkScoreForKeyword(KEYWORD)).thenReturn(54);

        Map<String, Object> actualResultsMap = keywordScoringController.getNetworkScoreForKeyword(KEYWORD);

        assertEquals(KEYWORD, actualResultsMap.get(KEYWORD_KEY));
        assertEquals(54, actualResultsMap.get(SCORE_KEY));
    }

    @Test
    public void givenKeyword_whenCalculatingSearchVolumeScoreForKeyword_parameterisedMapIsReturned() {
        when(scoreCalculatingService.calculateSearchVolumeScoreForKeyword(KEYWORD)).thenReturn(79);

        Map<String, Object> actualResultsMap = keywordScoringController.getSearchVolumeScoreForKeyword(KEYWORD);

        assertEquals(KEYWORD, actualResultsMap.get(KEYWORD_KEY));
        assertEquals(79, actualResultsMap.get(SCORE_KEY));
    }

}
