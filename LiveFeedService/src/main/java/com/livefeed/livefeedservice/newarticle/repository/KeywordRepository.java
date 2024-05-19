package com.livefeed.livefeedservice.newarticle.repository;

import java.util.List;
import java.util.Set;

public interface KeywordRepository {

    Set<String> getUserKeywords(String sseKey);

    int updateUserKeywords(String sseKey, List<String> newKeywords);

    void deleteUserKeywords(String sseKey);
}
