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
public class ResultBuildingService {

    private final String AMAZON_AUTOCOMPLETE_URL
            = "http://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=";

    public List<SearchResult> getResultsTreeForKeywordWithDepth(String keyword, int depthToTraverse) {
        List<SearchResult> resultSet = createResultTreeForRootKeyword(keyword);
        for (int currentLevel = 1; currentLevel < depthToTraverse; currentLevel++) {
            resultSet.addAll(findNewSearchTermResults(resultSet, currentLevel));
        }
        return resultSet;
    }

    private List<SearchResult> findNewSearchTermResults(List<SearchResult> resultSet, int levelToSearch) {
        int previousLevel = levelToSearch - 1;
        return resultSet.stream()
                .filter(searchResult -> searchResult.getLevel() == previousLevel)
                .map(SearchResult::getSuggestedTerms)
                .flatMap(List::stream)
                .map(searchTerm -> getSearchResultForKeywordAtLevel(searchTerm, levelToSearch))
                .collect(Collectors.toList());
    }

    private List<SearchResult> createResultTreeForRootKeyword(String keyword) {
        List<SearchResult> resultSet = new ArrayList<>();
        resultSet.add(getSearchResultForKeywordAtLevel(keyword, 0));
        return resultSet;
    }

    private SearchResult getSearchResultForKeywordAtLevel(String keyword, int level) {
        ResponseEntity<String> response = getResponseFromKeywordRequest(keyword);
        return new SearchResult(level, getSearchTermFromResponse(response), getSuggestedTerms(response));
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

    private List<String> getSuggestedTerms(ResponseEntity<String> response) {
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
