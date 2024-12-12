package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEventDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEventDto event, Locale locale) {
        return messageSource.getMessage("profile.view",
                new Object[]{event.getReceiverId(), event.getSenderId()}, locale);
    }
}
