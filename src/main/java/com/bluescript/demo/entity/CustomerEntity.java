package com.bluescript.demo.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
@Data
@RequiredArgsConstructor
// Schema : CUSTOMER
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMERNUMBER")
    private String customerNumber;
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "DATEOFBIRTH")
    private Date dateOfBirth;
    @Column(name = "HOUSENAME")
    private String houseName;
    @Column(name = "HOUSENUMBER")
    private String houseNumber;
    @Column(name = "POSTCODE")
    private String postCode;
    @Column(name = "PHONEMOBILE")
    private String phoneMobile;
    @Column(name = "PHONEHOME")
    private String phoneHome;
    @Column(name = "EMAILADDRESS")
    private String emailAddress;
}
