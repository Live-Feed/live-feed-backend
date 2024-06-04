package com.livefeed.livefeedservice.articlelist.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@ToString
public class SearchQueryParam {
    private List<String> type;
    private List<String> keywords;
    private int size;
    private Sort sort;
    private Double lastScore;
    private Long lastId;
    private String pit;
    private boolean isRelatedQuery;

    private static final String ASC = "asc";
    private static final String DESC = "desc";

    private SearchQueryParam(List<String> type, List<String> keywords, int size, Sort sort, Double lastScore, Long lastId, String pit, boolean isRelatedQuery) {
        this.type = type;
        this.keywords = keywords;
        this.size = size;
        this.sort = sort;
        this.lastScore = lastScore;
        this.lastId = lastId;
        this.pit = pit;
        this.isRelatedQuery = isRelatedQuery;
    }

    public static SearchQueryParam makeParam(List<String> type, List<String> keywords, int size, Double lastScore,
                                             Long lastId, String pit, boolean isRelatedQuery) {
        Sort targetSort  = makeSort(isRelatedQuery);
        return new SearchQueryParam(type, keywords, size, targetSort, lastScore, lastId, pit, isRelatedQuery);
    }

    public static SearchQueryParam copyAndSetPit(SearchQueryParam origin, String pit) {
        return new SearchQueryParam(origin.type, origin.getKeywords(), origin.getSize(),
                origin.getSort(), origin.getLastScore(), origin.getLastId(), pit, origin.isRelatedQuery());
    }

    private static Sort makeSort(boolean isRelatedQuery) {
        if (isRelatedQuery) {
            return Sort.by(Sort.Direction.DESC, "_score", "id");
        }
        return Sort.by(Sort.Direction.DESC, "id");
    }
}
