package edu.java.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StackOverflowItem {
    @JsonProperty("question_id")
    private int id;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("answer_count")
    private int answerCount;
    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;
    private String link;
    private String title;
}
