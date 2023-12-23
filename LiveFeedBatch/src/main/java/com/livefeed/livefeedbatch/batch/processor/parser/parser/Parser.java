package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.BodyDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.HeaderDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.processor.parser.exception.ParsingException;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
public abstract class Parser {

    protected final String OUTER_HTML = "outerHTML";

    public ParseResultDto parseWebPage(String url) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(url);
            return parse(driver);
        } catch (Exception e) {
            log.error("파싱 도중 에러가 발생했습니다. ", e);
            throw new ParsingException(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private ParseResultDto parse(WebDriver driver) {
        HeaderDto headerDto = parseHeader(driver);
        BodyDto bodyDto = parseBody(driver);
        return ParseResultDto.from(headerDto, bodyDto);
    }

    abstract protected HeaderDto parseHeader(WebDriver driver);

    abstract protected BodyDto parseBody(WebDriver driver);

    abstract public boolean support(UrlInfo key);
}
