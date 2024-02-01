package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import com.livefeed.livefeedbatch.batch.common.driver.ChromeDriverProvider;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.BodyDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.HeaderDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.processor.parser.exception.ParsingException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public abstract class Parser {

    protected final String OUTER_HTML = "outerHTML";

    public ParseResultDto parseWebPage(String url) {
        WebDriver driver = ChromeDriverProvider.getDriver();

        try {
            return parse(url, driver);
        } catch (Exception e) {
            log.error("파싱 도중 에러가 발생했습니다. url = {}", url, e);
            throw new ParsingException(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private ParseResultDto parse(String url, WebDriver driver) {
        driver.get(url);
        HeaderDto headerDto = parseHeader(driver);
        BodyDto bodyDto = parseBody(driver);
        return ParseResultDto.from(url, headerDto, bodyDto);
    }

    abstract protected HeaderDto parseHeader(WebDriver driver);

    abstract protected BodyDto parseBody(WebDriver driver);

    abstract public boolean support(UrlInfo key);
}
