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
        String html = replaceTabAndEnterToBlank(combineHeaderAndBodyHtml(header, body));

        return new ParseResultDto(articleTitle, publicationTime, pressCompanyName, journalistName, journalistEmail, originArticleUrl, html);
    }

    private static String combineHeaderAndBodyHtml(HeaderDto header, BodyDto body) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("<div class=\"article\">")
                .append(header.html())
                .append(body.html())
                .append("</div>")
                .toString();
    }

    private static String replaceTabAndEnterToBlank(String html) {
        return html.replaceAll("\t", "").replaceAll("\n", "");
    }
}
