package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record SkillAcquiredEvent(
        long userId,
        long skillId
) {
}
