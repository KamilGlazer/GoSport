package com.kamilglazer.gosport.model;

import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private CONNECTION_STATUS status;

}
