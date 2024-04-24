package com.livefeed.livefeedservice.newarticle.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.MtermvectorsRequest;
import co.elastic.clients.elasticsearch.core.MtermvectorsResponse;
import co.elastic.clients.elasticsearch.core.mtermvectors.MultiTermVectorsResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NativeElasticsearchTermVectorRepository implements TermVectorRepository {

    private static final List<String> FIELDS = List.of("articleTitle", "bodyHtml");

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Set<String> findTerms(Set<String> ids) {
        MtermvectorsRequest mtermvectorsRequest = new MtermvectorsRequest.Builder()
                .index("articles")
                .ids(new ArrayList<>(ids))
                .fields(FIELDS)
                .termStatistics(false)
                .fieldStatistics(true)
                .offsets(false)
                .payloads(false)
                .positions(false)
                .build();

        MtermvectorsResponse mtermvectors;
        try {
            mtermvectors = elasticsearchClient.mtermvectors(mtermvectorsRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<MultiTermVectorsResult> docs = mtermvectors.docs();

        return docs.stream().flatMap(doc -> doc.termVectors().values().stream())
                .flatMap(termVector -> termVector.terms().keySet().stream())
                .collect(Collectors.toSet());
    }
}
