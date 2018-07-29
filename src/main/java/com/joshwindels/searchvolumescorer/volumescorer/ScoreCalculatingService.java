package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreCalculatingService {

    @Autowired
    SearchResultService searchResultService;

    public int calculateSearchVolumeScoreForKeyword(String keyword) {
        double keywordLengthWeighting = 100.00 / keyword.length();

        double searchVolumeScore = 0;
        String searchTerm = keyword;
        while (searchTerm.length() > 0) {
            List<String> termsFound = searchResultService.getSearchResultForKeyword(searchTerm).getSuggestedTerms();
            if (termsFound.contains(keyword)) {
                double indexMultiplier = (10.0 - termsFound.indexOf(keyword)) / 10;
                searchVolumeScore += keywordLengthWeighting * indexMultiplier;
                searchTerm = searchTerm.substring(0, searchTerm.length() - 1);
            } else {
                break;
            }
        }
        return (int) Math.round(searchVolumeScore);
    }

    public int calculateNetworkScoreForKeyword(String keyword) {
        List<String> termsFound = searchResultService.getSearchResultForKeyword(keyword).getSuggestedTerms()
                .stream()
                .map(term -> searchResultService.getSearchResultForKeyword(term))
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return termsFound.size();
    }

    public Set<String> getRelatedTermsForKeyword(String keyword) {
        Set<String> termsFound = new LinkedHashSet<>();

        SearchResult searchResult = searchResultService.getSearchResultForKeyword(keyword);
        termsFound.addAll(getRelatedTermsFromSearchResult(searchResult));

        searchResult.getSuggestedTerms()
                .stream()
                .map(term -> searchResultService.getSearchResultForKeyword(term))
                .forEach(result -> termsFound.addAll(getRelatedTermsFromSearchResult(result)));
        return termsFound;
    }

    private Set<String> getRelatedTermsFromSearchResult(SearchResult searchResult) {
        return searchResult.getSuggestedTerms()
                .stream()
                .map(term -> term.substring(searchResult.getSearchTerm().length()))
                .map(String::trim)
                .filter(relatedTerm -> !relatedTerm.isEmpty())
                .collect(Collectors.toSet());
    }
}
