package com.example.gyak_beadando.model;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grand_prix_id")
    private GrandPrix grandPrix;

    @ManyToOne
    @JoinColumn(name = "pilot_id")
    private Pilot pilot;

    @Column(name = "place")
    private Integer place;

    @Column(name = "team")
    private String team;

    // getterek, setterek

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public GrandPrix getGrandPrix() { return grandPrix; }

    public void setGrandPrix(GrandPrix grandPrix) { this.grandPrix = grandPrix; }

    public Pilot getPilot() { return pilot; }

    public void setPilot(Pilot pilot) { this.pilot = pilot; }

    public Integer getPlace() { return place; }

    public void setPlace(Integer place) { this.place = place; }

    public String getTeam() { return team; }

    public void setTeam(String team) { this.team = team; }
}
