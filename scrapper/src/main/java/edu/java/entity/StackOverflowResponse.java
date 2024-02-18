package edu.java.entity;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StackOverflowResponse {
    private List<StackOverflowItem> items;
}
