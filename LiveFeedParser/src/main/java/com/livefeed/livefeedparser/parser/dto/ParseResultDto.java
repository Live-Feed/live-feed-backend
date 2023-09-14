package com.livefeed.livefeedparser.parser.dto;

public record ParseResultDto(
        String articleTitle,
        String publicationTime,
        String pressCompanyName,
        String journalistName,
        String journalistEmail,
        String originArticleUrl,
        String html
) {

    public static ParseResultDto from(HeaderDto header, BodyDto body) {

        String articleTitle = header.articleTitle();
        String publicationTime = header.publicationTime();
        String pressCompanyName = header.pressCompanyName();
        String originArticleUrl = header.originArticleUrl();
        String journalistName = body.journalistName();
        String journalistEmail = body.journalistEmail();
        String html = combineHeaderAndBodyHtml(header, body);

        return new ParseResultDto(articleTitle, publicationTime, pressCompanyName, journalistName, journalistEmail, originArticleUrl, html);
    }

    private static String combineHeaderAndBodyHtml(HeaderDto header, BodyDto body) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("<div class=\"article\">\n\t")
                .append(header.html())
                .append("\n\t")
                .append(body.html())
                .append("\n")
                .append("</div>")
                .toString();
    }
}
