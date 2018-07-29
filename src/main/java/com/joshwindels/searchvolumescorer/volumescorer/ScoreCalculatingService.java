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
        double termLengthWeight = 100.00 / keyword.length();
        double searchVolumeScore = 0;

        String searchTerm = keyword;
        while (searchTerm.length() > 0) {
            List<String> termsFound = searchResultService.getSearchResultForKeyword(searchTerm).getSuggestedTerms();
            if (termsFound.contains(keyword)) {
                double indexMultiplier = (10 - termsFound.indexOf(keyword)) / 10;
                searchVolumeScore += termLengthWeight * indexMultiplier;
                searchTerm = searchTerm.substring(0, searchTerm.length() - 1);
            } else {
                break;
            }
        }
        return (int) Math.round(searchVolumeScore);
    }
}
