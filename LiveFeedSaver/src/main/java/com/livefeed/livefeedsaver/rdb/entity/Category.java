package com.livefeed.livefeedsaver.rdb.entity;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Service service;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    private Theme theme;

    @Builder
    private Category(Service service, Platform platform, Theme theme) {
        this.service = service;
        this.platform = platform;
        this.theme = theme;
    }
}
