package com.smhrd.ta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// @JsonIgnoreProperties(ignoreUnknown = true)는 JSON에 있는 알 수 없는 필드를 무시하도록 합니다.
// Notion API 응답은 매우 방대하므로 필요한 필드만 매핑할 때 유용합니다.
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotionAnnouncement {

    private String id; // Notion 페이지의 고유 ID

    // Notion API 응답의 "properties" 필드를 직접 매핑합니다.
    // 이 Map은 Notion DB의 각 속성 이름(예: "Title", "Contents", "Date")을 키로 가집니다.
    private Map<String, PropertyValue> properties;

    // 클라이언트에 최종적으로 반환될 간소화된 데이터 필드
    // @Transient 어노테이션은 JPA에서 사용되지만, 여기서는 DB 엔티티가 아니므로 불필요합니다.
    // 하지만 이 필드들이 JSON 역직렬화 시 직접 채워지는 것이 아니라
    // mapToAnnouncement() 메서드를 통해 파싱되어 채워진다는 것을 명시하기 위해 개념적으로 유지합니다.
    private String title;
    private String contents;
    private String date; // ISO 8601 형식의 날짜 문자열 (예: "2025-06-20")

    // --- Getter와 Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Map<String, PropertyValue> getProperties() { return properties; }
    public void setProperties(Map<String, PropertyValue> properties) { this.properties = properties; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // Notion API 응답의 "properties"에서 실제 데이터를 추출하여 이 엔티티의 필드에 매핑하는 메서드
    public NotionAnnouncement mapToAnnouncement() {
        if (properties == null) {
            this.title = "[제목 없음]";
            this.contents = "[내용 없음]";
            this.date = "[날짜 없음]";
            return this;
        }

        // 1. Title 속성 추출
        PropertyValue titlePropValue = properties.get("Title");
        if (titlePropValue != null && titlePropValue.getTitle() != null && !titlePropValue.getTitle().isEmpty()) {
            this.title = titlePropValue.getTitle().get(0).getPlainText();
        } else {
            this.title = "[제목 없음]";
        }

        // 2. Contents 속성 추출
        PropertyValue contentsPropValue = properties.get("Contents");
        if (contentsPropValue != null && contentsPropValue.getRichText() != null && !contentsPropValue.getRichText().isEmpty()) {
            this.contents = contentsPropValue.getRichText().stream()
                    .map(TextElement::getPlainText) // TextElement의 plainText를 추출
                    .collect(Collectors.joining("\n")); // 줄바꿈으로 연결
        } else {
            this.contents = "[내용 없음]";
        }

        // 3. Date 속성 추출
        PropertyValue datePropValue = properties.get("Date");
        if (datePropValue != null && datePropValue.getDate() != null && datePropValue.getDate().getStart() != null) {
            this.date = datePropValue.getDate().getStart();
        } else {
            this.date = "[날짜 없음]";
        }

        return this; // 체이닝을 위해 현재 인스턴스 반환
    }

    // --- equals, hashCode, toString (선택 사항이지만 좋은 습관) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotionAnnouncement that = (NotionAnnouncement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NotionAnnouncement{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' + // 간소화된 필드 포함
               ", contents='" + contents + '\'' +
               ", date='" + date + '\'' +
               ", properties=" + properties + // 디버깅을 위해 properties도 포함
               '}';
    }

    // Notion API의 복잡한 속성 값들을 나타내는 내부 클래스들
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PropertyValue {
        private String id;
        private String type; // title, rich_text, date 등 속성의 타입

        @JsonProperty("title")
        private List<TextElement> title;

        @JsonProperty("rich_text")
        private List<TextElement> richText;

        @JsonProperty("date")
        private DateProperty date;

        // --- Getter와 Setter ---
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<TextElement> getTitle() { return title; }
        public void setTitle(List<TextElement> title) { this.title = title; }

        public List<TextElement> getRichText() { return richText; }
        public void setRichText(List<TextElement> richText) { this.richText = richText; }

        public DateProperty getDate() { return date; }
        public void setDate(DateProperty date) { this.date = date; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PropertyValue that = (PropertyValue) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "PropertyValue{" +
                   "id='" + id + '\'' +
                   ", type='" + type + '\'' +
                   ", title=" + title +
                   ", richText=" + richText +
                   ", date=" + date +
                   '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextElement {
        private String type;
        private TextContent text;
        @JsonProperty("plain_text")
        private String plainText; // 실제 텍스트 내용
        private String href;

        // --- Getter와 Setter ---
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public TextContent getText() { return text; }
        public void setText(TextContent text) { this.text = text; }

        public String getPlainText() { return plainText; }
        public void setPlainText(String plainText) { this.plainText = plainText; }

        public String getHref() { return href; }
        public void setHref(String href) { this.href = href; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TextElement that = (TextElement) o;
            return Objects.equals(plainText, that.plainText);
        }

        @Override
    public int hashCode() {
        return Objects.hash(plainText);
    }

        @Override
        public String toString() {
            return "TextElement{" +
                   "type='" + type + '\'' +
                   ", text=" + text +
                   ", plainText='" + plainText + '\'' +
                   ", href='" + href + '\'' +
                   '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextContent {
        private String content;
        private String link;

        // --- Getter와 Setter ---
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TextContent that = (TextContent) o;
            return Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(content);
        }

        @Override
        public String toString() {
            return "TextContent{" +
                   "content='" + content + '\'' +
                   ", link='" + link + '\'' +
                   '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DateProperty {
        private String start; // 시작 날짜 (ISO 8601 형식)
        private String end;   // 종료 날짜 (ISO 8601 형식, 선택 사항)
        @JsonProperty("time_zone")
        private String timeZone;

        // --- Getter와 Setter ---
        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }

        public String getEnd() { return end; }
        public void setEnd(String end) { this.end = end; }

        public String getTimeZone() { return timeZone; }
        public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateProperty that = (DateProperty) o;
            return Objects.equals(start, that.start);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start);
        }

        @Override
        public String toString() {
            return "DateProperty{" +
                   "start='" + start + '\'' +
                   ", end='" + end + '\'' +
                   ", timeZone='" + timeZone + '\'' +
                   '}';
        }
    }
}