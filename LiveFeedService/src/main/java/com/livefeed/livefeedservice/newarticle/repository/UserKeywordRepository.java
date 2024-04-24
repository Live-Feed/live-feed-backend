package com.livefeed.livefeedservice.newarticle.repository;

import java.util.Set;

public interface UserKeywordRepository {

    Set<String> getUserKeywords(String sseKey);
}
