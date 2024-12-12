package faang.school.notificationservice.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

@Data
@Builder
public class ProfileViewEvent {
    private Long senderId;
    private Long receiverId;
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime dateTime;

}
