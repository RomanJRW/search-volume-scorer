package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchVolumeEstimatingService {

    @Autowired
    SearchResultService searchResultService;

    public int calculateScoreForKeyword(String keyword) {
        List<String> termsFound = searchResultService.getSearchResultForKeyword(keyword).getSuggestedTerms()
                .stream()
                .map(term -> searchResultService.getSearchResultForKeyword(term))
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return termsFound.size();
    }

}
