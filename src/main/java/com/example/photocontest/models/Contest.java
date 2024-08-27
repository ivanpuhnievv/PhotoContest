package com.example.photocontest.models;


import com.example.photocontest.models.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contests")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "contest_judges",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "judge_id"))
    private List<User> judges;


    @OneToOne
    @JoinColumn(name = "winner_id")
    private PhotoPost winner;

    @OneToMany
    private List<PhotoPost> photoPosts;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

//    @OneToOne
//    @JoinColumn(name = "contest_type_id")
//    private ContestType contestType;
//

}
