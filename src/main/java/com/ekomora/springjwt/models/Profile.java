package com.ekomora.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_profiles")

public class Profile {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "post")
    private String post;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "education")
    private String education;

    @Column(name = "educational_institution")
    private String educational_institution;

    @Column(name = "diploma_number")
    private String diploma_number;

    @OneToOne(optional = false)
    @MapsId
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    public Profile(String post, Date birthday, String nationality, String education,
                   String educational_institution, String diploma_number) {
        this.post = post;
        this.birthday = birthday;
        this.nationality = nationality;
        this.education = education;
        this.educational_institution = educational_institution;
        this.diploma_number = diploma_number;
    }
}
