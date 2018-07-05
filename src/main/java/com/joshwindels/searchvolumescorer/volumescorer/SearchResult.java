package com.joshwindels.searchvolumescorer.volumescorer;

import java.util.List;

public class SearchResult {

    private int level;
    private String searchTerm;
    private List<String> suggestedTerms;

    public SearchResult(int level, String searchTerm, List<String> suggestedTerms) {
        this.level = level;
        this.searchTerm = searchTerm;
        this.suggestedTerms = suggestedTerms;
    }

    public int getLevel() {
        return level;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public List<String> getSuggestedTerms() {
        return suggestedTerms;
    }

}
