package com.livefeed.livefeedservice.newarticle.repository;

import java.util.Set;

public interface TermVectorRepository {

    Set<String> findTerms(Set<String> ids);

}
