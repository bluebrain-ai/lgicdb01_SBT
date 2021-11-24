package com.bluescript.demo.jpa;

import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.bluescript.demo.dto.IGetCustomerJpaDto;
import com.bluescript.demo.entity.CustomerEntity;

public interface IGetCustomerJpa extends JpaRepository<CustomerEntity, String> {
    @QueryHints(value = { @QueryHint(name = org.hibernate.annotations.QueryHints.FETCH_SIZE, value = "100"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true") })
    @Query(value = "SELECT   FIRSTNAME as caFirstName,LASTNAME as caLastName,DATEOFBIRTH as caDob,HOUSENAME as caHouseName,HOUSENUMBER as caHouseNum,POSTCODE as caPostcode,PHONEMOBILE as caPhoneMobile,PHONEHOME as caPhoneHome,EMAILADDRESS as caEmailAddress FROM CUSTOMER WHERE CUSTOMERNUMBER = :db2CustomernumberInt", nativeQuery = true)
    IGetCustomerJpaDto getCustomerByDb2CustomernumberInt(@Param("db2CustomernumberInt") int db2CustomernumberInt);
}
