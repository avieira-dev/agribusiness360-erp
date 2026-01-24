package com.agribusiness360.backend.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "property_id")
    private Integer propertyId;
}
