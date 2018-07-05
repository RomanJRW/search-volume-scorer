package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    public int getLeafCountScoreFromResults(List<SearchResult> searchResults) {
        int leafLevel = getLeafLevelFromResults(searchResults);
        long leafCountScore = searchResults.stream()
                .filter(searchResult -> searchResult.getLevel() == leafLevel)
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .count();
        return standardiseScoreForLeafLevel(leafCountScore, leafLevel);
    }

    private int getLeafLevelFromResults(List<SearchResult> searchResults) {
        return Collections.max(searchResults.stream()
                .map(SearchResult::getLevel).collect(Collectors.toSet()));
    }

    private int standardiseScoreForLeafLevel(long score, int leafLevel) {
        double percentageScore = score / (10 * (Math.pow(10, leafLevel)));
        return (int) Math.round(percentageScore * 100);
    }

    // EXPERIMENTAL ALTERNATIVE SCORING SYSTEM - SEE NOTES
    public int getLeafArrayIndexScoreFromResults(List<SearchResult> searchResults) {
        int leafLevel = getLeafLevelFromResults(searchResults);
        Integer arrayIndexScore = searchResults.stream()
                .filter(searchResult -> searchResult.getLevel() == leafLevel)
                .map(searchResult -> searchResult.getSuggestedTerms().indexOf(searchResult.getSearchTerm()))
                .filter(indexPosition -> indexPosition != -1)
                .map(indexPosition -> 10 - indexPosition)
                .mapToInt(Integer::intValue)
                .sum();
        return standardiseScoreForLeafLevel(arrayIndexScore, leafLevel);
    }

}
