package com.livefeed.livefeedbatch.batch.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NewArticleBucket {

    private static final List<Long> newArticleIds = new CopyOnWriteArrayList<>();

    public static void add(Long articleId) {
        newArticleIds.add(articleId);
    }

    public static List<Long> getNewArticleIds() {
        return newArticleIds;
    }
}
