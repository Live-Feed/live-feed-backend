package com.livefeed.livefeedservice.newarticle.service;

import com.livefeed.livefeedservice.newarticle.domain.Emitters;
import com.livefeed.livefeedservice.newarticle.repository.TermVectorRepository;
import com.livefeed.livefeedservice.newarticle.repository.UserKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermAnalyzeService {

    private final TermVectorRepository termVectorRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final Emitters emitters;

    public void noticeNewArticles(Set<String> newArticleIDs) {

        Set<String> terms = termVectorRepository.findTerms(newArticleIDs);
        List<String> sseKeyList = emitters.getSseKeyList();

        Map<String, Set<String>> userKeywordMap = findConnectedUserKeywords(sseKeyList);

        emitters.sendAlertMessage(terms, userKeywordMap);
    }

    private Map<String, Set<String>> findConnectedUserKeywords(List<String> sseKeyList) {
        return sseKeyList.stream().collect(Collectors.toMap(
                sseKey -> sseKey,
                userKeywordRepository::getUserKeywords
        ));
    }
}
