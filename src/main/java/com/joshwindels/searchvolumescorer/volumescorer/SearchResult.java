package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.List;

public class SearchResult {

    private String searchTerm;
    private List<String> suggestedTerms;

    public SearchResult(String searchTerm, List<String> suggestedTerms) {
        this.searchTerm = searchTerm;
        this.suggestedTerms = suggestedTerms;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public List<String> getSuggestedTerms() {
        return suggestedTerms;
    }

}
