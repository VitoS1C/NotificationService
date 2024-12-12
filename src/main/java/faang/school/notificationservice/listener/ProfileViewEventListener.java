package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import java.util.List;
import java.util.Locale;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    public ProfileViewEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            MessageBuilder<ProfileViewEvent> messageBuilder,
            List<NotificationService> notificationServices) {
        super(
                objectMapper,
                userServiceClient,
                messageBuilder,
                notificationServices
        );
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = handleEvent(message, ProfileViewEvent.class);
        String text = getMessage(profileViewEvent, Locale.getDefault());
        sendNotification(profileViewEvent.getReceiverId(), text);
    }
}
