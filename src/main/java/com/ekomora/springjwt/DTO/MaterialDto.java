package com.ekomora.springjwt.DTO;

import com.ekomora.springjwt.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
public class MaterialDto {
    private long id;
    private String title;
    private String inventoryNumber;
    private Date dateStart;
    private String type;
    private int amount;
    private int price;
    private long userId;

    public MaterialDto(long id, String title, String inventoryNumber,
                       Date dateStart, String type, int amount, int price, User user) {
        this.id = id;
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.dateStart = dateStart;
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.userId = user.getId();
    }
}
