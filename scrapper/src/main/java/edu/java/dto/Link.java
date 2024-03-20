package edu.java.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "link")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private long id;
    @Column(name = "link_name")
    private String name;
    @Column(name = "last_check")
    private OffsetDateTime lastCheck;
    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;
    @Column(name = "last_commit")
    private OffsetDateTime lastCommit;
    @Column(name = "amount_issues")
    private int amountOfIssues;
    @Column(name = "type")
    private String type;
}
