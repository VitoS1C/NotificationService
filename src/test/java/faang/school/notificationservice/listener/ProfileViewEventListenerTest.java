package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<ProfileViewEvent> messageBuilder;

    @Mock
    private Message message;

    @Mock
    TelegramService telegramService;

    @InjectMocks
    private ProfileViewEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = List.of(telegramService);
        listener = new ProfileViewEventListener(objectMapper, userServiceClient, messageBuilder, notificationServices);
        String timeToString = LocalDateTime.now().toString();
        String json = "{\"senderId\":1, \"receiverId\":2,\"dateTime\":" + timeToString + "}";
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testOnMessage_Success() throws IOException {
        // Mock data
        ProfileViewEvent event = ProfileViewEvent
                .builder()
                .senderId(1L)
                .receiverId(2L)
                .dateTime(LocalDateTime.now())
                .build();
        String expectedMessage = "Someone viewed your profile!";
        UserDto user = UserDto
                .builder()
                .id(2L)
                .username("John Doe")
                .email("john.doe@example.com")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
        when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, Locale.getDefault())).thenReturn(expectedMessage);
        when(userServiceClient.getUser(event.getReceiverId())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(user.getPreference());

        // Call the method
        listener.onMessage(message, null);

        // Verify interactions
        verify(objectMapper).readValue(message.getBody(), ProfileViewEvent.class);
        verify(messageBuilder).buildMessage(event, Locale.getDefault());
        verify(userServiceClient).getUser(event.getReceiverId());
        verify(telegramService).send(user, expectedMessage);
    }
}
