package com.livefeed.livefeedservice.newarticle.repository;

import java.util.List;
import java.util.Set;

public interface UserKeywordRepository {

    Set<String> getUserKeywords(String sseKey);

    Long setUserKeywords(String sseKey, List<String> keywords);
}
