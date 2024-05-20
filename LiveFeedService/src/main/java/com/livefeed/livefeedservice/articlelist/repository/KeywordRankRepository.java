package com.livefeed.livefeedservice.articlelist.repository;

import java.util.List;
import java.util.Set;

public interface KeywordRankRepository {

    void updateKeywordScore(List<String> keywords);

    Set<String> getTopRankKeywords();

    Set<String> getExistedTopRankKeywords();

    void updateTopRankKeywords(Set<String> updatedTopRankKeywords);
}
