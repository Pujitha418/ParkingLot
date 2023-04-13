package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.Id;

@Getter
@Setter
public class BaseModel {
    @Id
    private Long id;
    private Date createdDate;
    private Date lastUpdateDate;
}
