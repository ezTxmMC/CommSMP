package de.commsmp.smp.config.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MutedPlayer {
    private String uniqueId;
    private long duration;
    private String timestamp;
    private String reason;
}
