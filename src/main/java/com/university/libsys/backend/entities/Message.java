package com.university.libsys.backend.entities;

import com.university.libsys.backend.utils.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Message should have a text in it")
    private String text;
    @NotBlank(message = "Message should have a receiver")
    private Long receiverID;
    @NotBlank(message = "Message should have a sender")
    private Long senderID;
    @NotBlank(message = "Message should have its status (read/unread)")
    private MessageStatus status;
}
