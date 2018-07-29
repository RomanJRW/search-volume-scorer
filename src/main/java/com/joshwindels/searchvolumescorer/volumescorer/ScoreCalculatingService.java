package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreCalculatingService {

    @Autowired
    SearchResultService searchResultService;

    public int calculateNetworkScoreForKeyword(String keyword) {
        List<String> termsFound = searchResultService.getSearchResultForKeyword(keyword).getSuggestedTerms()
                .stream()
                .map(term -> searchResultService.getSearchResultForKeyword(term))
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return termsFound.size();
    }

    public int calculateSearchVolumeScoreForKeyword(String keyword) {
        double lengthWeighting = 100.00 / keyword.length();
        double searchVolumeScore = 0;

        SearchResult initialResult = searchResultService.getSearchResultForKeyword(keyword);
        List<String> terms = initialResult.getSuggestedTerms();
        if (terms.contains(initialResult.getSearchTerm())) {
            double indexMultiplier = (10 - terms.indexOf(initialResult.getSearchTerm())) / 10;
            searchVolumeScore += lengthWeighting * indexMultiplier;
        }

        return (int) Math.round(searchVolumeScore);
    }
}
