package com.tyk.boardev.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ArticleEntity {
    private int id;
    private String nickname;
    private String password;
    private String title;
    private String content;
    private int view;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}
