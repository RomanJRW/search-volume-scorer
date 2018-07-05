package com.joshwindels.searchvolumescorer.volumescorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchVolumeScoringService {

    private final String AMAZON_AUTOCOMPLETE_URL
            = "http://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=";

    public int calculateScoreForKeyword(String keyword) {
        List<String> termsFound = getSearchResultForKeyword(keyword).getSuggestedTerms()
                .stream()
                .map(this::getSearchResultForKeyword)
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return termsFound.size();
    }

    private SearchResult getSearchResultForKeyword(String keyword) {
        ResponseEntity<String> response = getResponseFromKeywordRequest(keyword);
        return new SearchResult(getSearchTermFromResponse(response), getSuggestedTermsFromResponse(response));
    }

    private ResponseEntity<String> getResponseFromKeywordRequest(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(AMAZON_AUTOCOMPLETE_URL + keyword, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException();
        }
        return response;
    }

    private String getSearchTermFromResponse(ResponseEntity<String> response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(response.getBody()).get(0).asText();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private List<String> getSuggestedTermsFromResponse(ResponseEntity<String> response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode suggestedTerms;
        try {
            suggestedTerms = mapper.readTree(response.getBody()).get(1);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        List<String> searchResults = new ArrayList<>();
        for (int i = 0; suggestedTerms.size() > i; i++) {
            searchResults.add(suggestedTerms.get(i).asText());
        }
        return searchResults;
    }

}
