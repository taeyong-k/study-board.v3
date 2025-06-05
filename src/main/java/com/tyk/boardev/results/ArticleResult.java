package com.tyk.boardev.results;

public enum ArticleResult {
    FAILURE,            // 정규화 실패, INSERT 결과 반환된 int 값이 0
    FAILURE_PASSWORD,   // 입력된 비밀번호가 올바르지 않은 경우
    SUCCESS             // 성공
}
