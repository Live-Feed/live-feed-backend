package com.livefeed.livefeedservice.articlelist.domain;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return document.text();
    }

    // 본문에서 100글자 씩 잘라서 텍스트만 보여주는 메서드
    public String cutTextFromBodyText(String text) {
        int index = text.indexOf("<b>");

        int leftBound = index - 200;
        int rightBound = index + 200;
        int textLength = text.length();

        if (index == -1) {
            return textLength > rightBound ? text.substring(0, rightBound) + " ..." : text;
        }

        int leftCond = Math.max(leftBound, 0);
        int rightCond = Math.min(rightBound, textLength);

        return text.substring(leftCond, rightCond) + " ...";
    }

    // explain 을 통해 어떤 항목에 어떤 단어가 검색되었는지 추출하는 메서드 pair<타입, 단어> 형태
    public String extractSearchedWord(String explain) {
        int firstIndex = explain.indexOf("(");
        int lastIndex = explain.indexOf(" ");

        return explain.substring(firstIndex + 1, lastIndex).split(":")[1];
    }

    // 찾은 단어를 볼드 형식으로 변경해주는 메서드
    public String boldingWord(String word, String text) {
        String regex = "\\b" + Pattern.quote(word) + "\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.replaceAll("<b>$0</b>");
    }
}














