package com.tyk.boardev.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CommentEntity {
    private int id;
    private int articleId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}
