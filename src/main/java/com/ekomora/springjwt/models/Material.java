package com.ekomora.springjwt.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "inventory_number")
    private String inventoryNumber;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private int price;

    @JsonProperty
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //    public Material(String title, String inventoryNumber, Date dateStart,
//                    String type, int amount, int price, User user) {
//        this.title = title;
//        this.inventoryNumber = inventoryNumber;
//        this.dateStart = dateStart;
//        this.type = type;
//        this.amount = amount;
//        this.price = price;
//        this.user = user;
//    }
}
