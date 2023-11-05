package com.livefeed.livefeedservice.articlelist.repository;

import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface SearchOperations<T> {

    String getPit(String indexName);

    SearchHits<? extends T> getSearchResult(SearchQueryParam searchQueryParam);
}
