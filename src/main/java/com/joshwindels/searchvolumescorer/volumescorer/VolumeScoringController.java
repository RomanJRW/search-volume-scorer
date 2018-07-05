package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VolumeScoringController {


    private final int SEARCH_DEPTH = 2;
    private final String KEYWORD_KEY = "keyword";
    private final String SCORE_KEY = "score";

    @Autowired
    ResultBuildingService resultBuildingService;

    @Autowired
    ScoringService scoringService;

    @GetMapping("/estimate")
    @ResponseBody
    public Map<String, Object> estimateScoreForKeyword(@RequestParam String keyword) {
        List<SearchResult> searchResults
                = resultBuildingService.getResultsTreeForKeywordWithDepth(keyword, SEARCH_DEPTH);
        int keywordScore = scoringService.getLeafCountScoreFromResults(searchResults);
        return createKeywordAndScoreMap(keyword, keywordScore);
    }

    private Map<String, Object> createKeywordAndScoreMap(String keyword, int keywordScore) {
        Map<String, Object> results = new LinkedHashMap<>();
        results.put(KEYWORD_KEY, keyword);
        results.put(SCORE_KEY, keywordScore);
        return results;
    }

    @ExceptionHandler({ RuntimeException.class })
    public Map<String, Object> handleException() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "something went wrong, try again later");
        return errorResponse;
    }

}
