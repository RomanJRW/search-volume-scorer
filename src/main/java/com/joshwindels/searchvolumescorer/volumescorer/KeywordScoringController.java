package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeywordScoringController {

    private final String KEYWORD_KEY = "keyword";
    private final String SCORE_KEY = "score";

    @Autowired ScoreCalculatingService scoreCalculatingService;

    @GetMapping("/estimate")
    @ResponseBody
    public Map<String, Object> getSearchVolumeScoreForKeyword(@RequestParam String keyword) {
        int estimatedScore = scoreCalculatingService.calculateSearchVolumeScoreForKeyword(keyword);
        return createKeywordAndScoreMap(keyword, estimatedScore);
    }

    @GetMapping("/network")
    @ResponseBody
    public Map<String, Object> getNetworkScoreForKeyword(@RequestParam String keyword) {
        int networkScore = scoreCalculatingService.calculateNetworkScoreForKeyword(keyword);
        return createKeywordAndScoreMap(keyword, networkScore);
    }

    private Map<String, Object> createKeywordAndScoreMap(String keyword, int estimatedSearchScore) {
        Map<String, Object> results = new LinkedHashMap<>();
        results.put(KEYWORD_KEY, keyword);
        results.put(SCORE_KEY, estimatedSearchScore);
        return results;
    }

    @ExceptionHandler({ RuntimeException.class })
    public Map<String, Object> handleException() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "something went wrong, try again later");
        return errorResponse;
    }

}
