package com.university.libsys.backend.data;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.repositories.MessagesRepository;
import com.university.libsys.backend.utils.MessageStatus;
import com.university.libsys.web.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagesDataLoader implements ApplicationRunner {

    private final MessagesRepository messagesRepository;
    private final Logger log = LoggerFactory.getLogger(MessagesDataLoader.class);

    @Autowired
    public MessagesDataLoader(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (messagesRepository.count() == 0) {
            // saving test messages for functional users
            log.warn("Message repository is empty, saving some messages");
            List<Message> messages = List.of(
                    new Message(null, "Thank you for becoming our Administrator", 2L, MessageUtil.ADMINISTRATION_ID, MessageStatus.UNREAD),
                    new Message(null, "Thank you for becoming our Writer", 3L, MessageUtil.ADMINISTRATION_ID, MessageStatus.UNREAD),
                    new Message(null, "Thank you for becoming out Reader", 4L, MessageUtil.ADMINISTRATION_ID, MessageStatus.UNREAD)
            );
            messagesRepository.saveAll(messages);
        }
    }
}
