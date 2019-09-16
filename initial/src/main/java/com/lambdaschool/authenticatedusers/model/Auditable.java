package com.lambdaschool.authenticatedusers.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;


@MappedSuperclass // This is a super class. It will never be instantiated directly

// Listeners are services that run in the background that spring manages for us

//@EntityListeners - anytime something happens to an entity Spring will check this (Auditable) class to see if something needs to be updated. Every time an Entity happens the Listener gets invoked and Spring processes what needs to happen
@EntityListeners(AuditingEntityListener.class)
abstract class Auditable
{
    @CreatedBy
    protected String createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date createdDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date lastModifiedDate;



}