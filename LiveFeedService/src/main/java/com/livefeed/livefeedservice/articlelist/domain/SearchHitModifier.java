package com.livefeed.livefeedservice.articlelist.domain;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SearchHitModifier {

    // img 를 추출해주는 메서드
    public String extractFirstImgFromHtml(String html) {
        Document document = Jsoup.parse(html);
        Element imgElement = document.select("img").first();
        return imgElement != null ? imgElement.attr("src") : null;
    }

    // html 로 되어있는 텍스트에서 순수 text 만 추출하는 메서드
    public String extractTextFromHtml(String html) {
        Document document = Jsoup.parse(html);
        String bodyText = document.text();
        return bodyText.substring(0, bodyText.length() % 500);
    }

    public String extractTagExceptBoldTag(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        String textOnly = body.text();

        Elements elements = body.select("b");

        Set<String> keywords = elements.stream().map(Element::text).collect(Collectors.toSet());

        for (String keyword : keywords) {
            textOnly = textOnly.replace(keyword, "<b>" + keyword + "</b>");
        }

        return textOnly;
    }
}














