package edu.java.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GitHubResponse {
    private int id;
    private String name;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String description;
    @JsonProperty("created_at")
    private OffsetDateTime createTime;
    @JsonProperty("updated_at")
    private OffsetDateTime updateTime;
}
