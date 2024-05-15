package com.livefeed.livefeedservice.newarticle.service;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${cookie.secure}")
    private boolean secure;

    @Value("${cookie.domain}")
    private String domain;

    private static final String COOKIE_KEY = "sseKey";
    private static final int MAX_AGE = 24 * 60 * 60;
    private static final String PATH = "/";

    public Cookie makeSseKeyCookie(String emitterKey) {
        Cookie cookie = new Cookie(COOKIE_KEY, emitterKey);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setDomain(domain);
        cookie.setPath(PATH);
        cookie.setMaxAge(MAX_AGE);
        return cookie;
    }
}
