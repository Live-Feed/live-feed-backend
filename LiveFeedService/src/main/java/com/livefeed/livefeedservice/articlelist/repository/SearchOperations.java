package com.livefeed.livefeedservice.articlelist.repository;

import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface SearchOperations {

    String getPit(String indexName);

    SearchHits<ElasticsearchArticle> getSearchResult(SearchQueryParam searchQueryParam);
}
