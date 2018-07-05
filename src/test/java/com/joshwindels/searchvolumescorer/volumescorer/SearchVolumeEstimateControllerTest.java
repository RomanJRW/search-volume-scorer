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
public class SearchVolumeEstimateControllerTest {

    private static final String KEYWORD = "linux";
    private static final String KEYWORD_KEY = "keyword";
    private static final String SCORE_ESTIMATE_KEY = "score";

    @Mock
    SearchVolumeEstimatingService searchVolumeEstimatingService;

    @InjectMocks
    SearchVolumeEstimateController searchVolumeEstimateController;

    @Test
    public void givenKeyword_whenEstimatingScoreForKeyword_parameterisedMapIsReturned() {
        when(searchVolumeEstimatingService.calculateScoreForKeyword(KEYWORD)).thenReturn(54);

        Map<String, Object> actualResultsMap = searchVolumeEstimateController.estimateSearchVolumeScoreForKeyword(KEYWORD);

        assertEquals(KEYWORD, actualResultsMap.get(KEYWORD_KEY));
        assertEquals(54, actualResultsMap.get(SCORE_ESTIMATE_KEY));
    }

}