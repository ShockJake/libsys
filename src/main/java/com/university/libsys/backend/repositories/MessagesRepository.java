package com.university.libsys.backend.repositories;

import com.university.libsys.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySenderID(Long id);

    List<Message> findMessagesByReceiverID(Long id);
}
