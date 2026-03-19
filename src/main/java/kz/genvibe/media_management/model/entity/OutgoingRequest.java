package kz.genvibe.media_management.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import lombok.*;

@Entity
@Table(name = "outgoing_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutgoingRequest extends CreateEntity {

    private String url;
    private String method;
    private String requestBody;
    private short responseStatusCode;
    private String responseBody;

}
