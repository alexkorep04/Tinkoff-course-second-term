package edu.java.bot.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LinkUpdateRequest {
    @Min(1)
    private Long id;
    @NotNull
    private URI url;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    private List<Long> tgChatIds;
}
