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

    private SearchQueryParam(List<String> type, List<String> keywords, int size, Sort sort, Long lastId, String pit, boolean isRelatedQuery) {
        this.type = type;
        this.keywords = keywords;
        this.size = size;
        this.sort = sort;
        this.lastId = lastId;
        this.pit = pit;
        this.isRelatedQuery = isRelatedQuery;
    }

    public static SearchQueryParam makeParam(List<String> type, List<String> keywords, int size,
                                             List<String> sorts, Long lastId, String pit, boolean isRelatedQuery) {
        Sort targetSort  = makeSort(sorts);
        return new SearchQueryParam(type, keywords, size, targetSort, lastId, pit, isRelatedQuery);
    }

    public static SearchQueryParam makeParam(List<String> type, List<String> keywords, int size, Double lastScore,
                                             Long lastId, String pit, boolean isRelatedQuery) {
        Sort targetSort  = makeSort(isRelatedQuery);
        return new SearchQueryParam(type, keywords, size, targetSort, lastScore, lastId, pit, isRelatedQuery);
    }

    private static Sort makeSort(boolean isRelatedQuery) {
        if (isRelatedQuery) {
            return Sort.by(Sort.Direction.DESC, "_score", "id");
        }
        return Sort.by(Sort.Direction.DESC, "id");
    }

    private static Sort makeSort(List<String> sorts) {

        List<Sort.Order> orders = sorts.stream().map(sort -> {
            String[] sortStringArray = sort.split("-");
            if (sortStringArray.length <= 1) throw new IllegalArgumentException("올바르지 않은 정렬 정보입니다.");

            String conditionValue = sortStringArray[0];
            String conditionOrder = sortStringArray[1];

            if (conditionOrder.equals(ASC)) {
                return Sort.Order.asc(conditionValue);
            } else if (conditionOrder.equals(DESC)) {
                return Sort.Order.desc(conditionValue);
            } else {
                throw new IllegalArgumentException("올바르지 않은 정렬 정보입니다.");
            }
        }).toList();

        return Sort.by(orders);
    }

    public void setFirstRequestPit(String pit) {
        if (pit == null || pit.isBlank()) {
            throw new IllegalArgumentException("정확한 pit 값이 입력되지 않았습니다.");
        }
        this.pit = pit;
    }
}
