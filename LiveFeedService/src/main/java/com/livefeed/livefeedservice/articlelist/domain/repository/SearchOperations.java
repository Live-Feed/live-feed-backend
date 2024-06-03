package com.livefeed.livefeedservice.articlelist.domain.repository;

import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;

public interface SearchOperations {

    SearchResultDto getSearchResult(SearchQueryParam searchQueryParam);
}
