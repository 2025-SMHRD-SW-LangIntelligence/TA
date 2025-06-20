package com.smhrd.ta.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smhrd.ta.entity.NotionAnnouncement; // Entity 참조 변경

import jakarta.annotation.PostConstruct; // @PostConstruct는 jakarta.annotation-api 의존성 필요

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects; // Objects import 추가

@Repository
public class NotionRepository {

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.database.id}")
    private String notionDatabaseId;

    @Value("${notion.api.url}")
    private String notionApiBaseUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(notionApiBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + notionApiToken)
                .defaultHeader("Notion-Version", "2022-06-28")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Notion 데이터베이스에서 페이지 목록을 조회합니다.
     * Notion API의 쿼리 결과 전체를 나타내는 임시 내부 클래스를 사용합니다.
     * @return Notion API의 raw 쿼리 결과를 포함하는 Mono
     */
    public Mono<NotionQueryResultWrapper> findAllNotionPages() {
        String url = String.format("/databases/%s/query", notionDatabaseId);

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> dateSort = new HashMap<>();
        dateSort.put("property", "Date");
        dateSort.put("direction", "descending");

        requestBody.put("sorts", List.of(dateSort));

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NotionQueryResultWrapper.class); // 내부 Wrapper 클래스로 매핑
    }

    // Notion API 쿼리 응답의 루트를 나타내는 내부 클래스
    // DTO를 사용하지 않기로 했으므로, Entity가 아닌 여기에 필요한 최소한의 구조를 정의합니다.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NotionQueryResultWrapper {
        private String object;
        // Notion API 응답의 'results'는 NotionAnnouncement 엔티티 목록입니다.
        private List<NotionAnnouncement> results;
        @JsonProperty("next_cursor")
        private String nextCursor;
        @JsonProperty("has_more")
        private boolean hasMore;

        // --- Getter와 Setter ---
        public String getObject() { return object; }
        public void setObject(String object) { this.object = object; }

        public List<NotionAnnouncement> getResults() { return results; }
        public void setResults(List<NotionAnnouncement> results) { this.results = results; }

        public String getNextCursor() { return nextCursor; }
        public void setNextCursor(String nextCursor) { this.nextCursor = nextCursor; }

        public boolean isHasMore() { return hasMore; }
        public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NotionQueryResultWrapper that = (NotionQueryResultWrapper) o;
            return hasMore == that.hasMore && Objects.equals(object, that.object) && Objects.equals(results, that.results) && Objects.equals(nextCursor, that.nextCursor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object, results, nextCursor, hasMore);
        }

        @Override
        public String toString() {
            return "NotionQueryResultWrapper{" +
                   "object='" + object + '\'' +
                   ", results=" + results +
                   ", nextCursor='" + nextCursor + '\'' +
                   ", hasMore=" + hasMore +
                   '}';
        }
    }
}