# search-volume-scorer

A small application that uses Amazon's autocomplete functionality to perform three separate functions relating to Amazon keywords. Each function has its own endpoint and their purpose and usage is described below.

## Search volume scoring

Returns a score from 0 to 100 for a given search term, with higher scores suggesting a higher search volume for the term. The endpoint can be accessed using `/estimate?keyword=[search term]`

## Network size scoring

Returns a score from 0 to 100 for the size of a search term's network of related terms, with higher scores representing a high number of terms relating to the provided term. The endpoint can be accessed using `/network?keyword=[search term]`

## Related terms

Returns a set of popularly searched terms related to the given search term. The endpoint can be accessed using `/terms?keyword=[search term]`
