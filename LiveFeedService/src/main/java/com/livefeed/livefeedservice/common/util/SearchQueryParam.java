package com.livefeed.livefeedservice.common.util;

import org.springframework.data.domain.Sort;

import java.util.List;

public record SearchQueryParam(
        List<String> type,
        List<String> keywords,
        int size,
        Sort sort,
        Long lastId,
        String pit
) {

    public static SearchQueryParam makeParam(List<String> type, List<String> keywords, int size,
                                             List<String> sorts, Long lastId, String pit) {
        Sort targetSort  = makeSort(sorts);

        return new SearchQueryParam(
                type, keywords,
                size,
                targetSort,
                lastId,
                pit
        );
    }

    private static Sort makeSort(List<String> sorts) {

        List<Sort.Order> orders = sorts.stream().map(sort -> {
            String[] sortStringArray = sort.split("-");
            String conditionValue = sortStringArray[0];
            String conditionOrder = sortStringArray[1];

            if (conditionOrder.equals("asc")) {
                return Sort.Order.asc(conditionValue);
            } else if (conditionOrder.equals("desc")) {
                return Sort.Order.desc(conditionValue);
            } else {
                throw new IllegalArgumentException("올바르지 않은 정렬 정보입니다.");
            }
        }).toList();

        return Sort.by(orders);
    }
}
