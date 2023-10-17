package com.livefeed.livefeedsaver.rdb.entity;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @PrePersist
    public void setDeleteFalse() {
        this.isDeleted = Boolean.FALSE;
    }

    @Builder
    private Category(Service service, Platform platform, Theme theme) {
        this.service = service;
        this.platform = platform;
        this.theme = theme;
    }

    public static Category from(ConsumerKeyDto key) {
        return Category.builder()
                .service(key.service())
                .platform(key.platform())
                .theme(key.theme())
                .build();
    }
}
