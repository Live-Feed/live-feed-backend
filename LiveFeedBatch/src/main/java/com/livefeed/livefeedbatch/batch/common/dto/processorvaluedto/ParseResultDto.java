package com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDtoInterface;

public record ParseResultDto(
        String articleTitle,
        String publicationTime,
        String pressCompanyName,
        String journalistName,
        String journalistEmail,
        String originArticleUrl,
        String headerHtml,
        String bodyHtml
) implements ItemDtoInterface<ParseResultDto> {

    public static ParseResultDto from(HeaderDto header, BodyDto body) {
        String articleTitle = header.articleTitle();
        String publicationTime = header.publicationTime();
        String pressCompanyName = header.pressCompanyName();
        String originArticleUrl = header.originArticleUrl();
        String journalistName = body.journalistName();
        String journalistEmail = body.journalistEmail();
        String headerHtml = replaceTabAndEnterToBlank(header.html());
        String bodyHtml = replaceTabAndEnterToBlank(body.html());

        return new ParseResultDto(articleTitle, publicationTime, pressCompanyName, journalistName, journalistEmail, originArticleUrl, headerHtml, bodyHtml);
    }

    private static String replaceTabAndEnterToBlank(String html) {
        return html.replaceAll("\t", "").replaceAll("\n", "").replaceAll("\"", "");
    }

    @Override
    public ParseResultDto getValue() {
        return this;
    }
}
