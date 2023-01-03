package com.university.libsys.backend.entities;

import com.university.libsys.backend.utils.RequestStatus;
import com.university.libsys.backend.utils.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LibsysRequest {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Request must have a sender ID")
    private Long requestSenderID;
    @NotBlank(message = "Request must have a type")
    private RequestType requestType;
    @NotBlank(message = "Request must have a status")
    private RequestStatus requestStatus;
}
