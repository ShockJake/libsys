package com.university.libsys.backend.services.Message;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.exceptions.MessageNotFoundException;
import com.university.libsys.backend.repositories.MessagesRepository;
import com.university.libsys.backend.utils.MessageStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessagesRepository messagesRepository;
    private final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    public MessageServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public Message getMessageById(@NotNull Long id) throws MessageNotFoundException {
        return messagesRepository.findById(id).orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public List<Message> getAllMessages() {
        return messagesRepository.findAll();
    }

    @Override
    public List<Message> getMessagesBySenderID(@NotNull Long id) {
        return messagesRepository.findMessagesBySenderID(id);
    }

    @Override
    public List<Message> getMessagesByReceiverID(@NotNull Long id) {
        return messagesRepository.findMessagesByReceiverID(id);
    }

    @Override
    public Message saveNewMessage(@NotNull Message message) {
        log.debug(String.format("Saving new message with id = %s", message.getId()));
        return messagesRepository.save(message);
    }

    @Override
    public Message deleteMessage(@NotNull Long id) throws MessageNotFoundException {
        final Message messageToDelete = messagesRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));

        log.debug(String.format("Deleting message id = %s", messageToDelete.getId()));

        messagesRepository.delete(messageToDelete);
        return messageToDelete;
    }

    @Override
    public Message updateMessageStatus(@NotNull Long id, @NotNull MessageStatus messageStatus) throws MessageNotFoundException {
        final Message messageToUpdate = messagesRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));

        log.debug(String.format("Updating message status id = %s, old value = %s, new value = %s",
                id, messageToUpdate.getStatus().name(), messageStatus.name()));

        messageToUpdate.setStatus(messageStatus);
        messagesRepository.save(messageToUpdate);
        return messageToUpdate;
    }
}
