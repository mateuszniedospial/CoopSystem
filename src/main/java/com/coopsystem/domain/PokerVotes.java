package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PokerVotes.
 */
@Entity
@Table(name = "poker_votes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PokerVotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "vote")
    private Float vote;

    @OneToOne
    @JoinColumn(name="j_user_id")
    private JUser juser;

    @OneToOne
    @JoinColumn()
    private Poker poker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getVote() {
        return vote;
    }

    public PokerVotes vote(Float vote) {
        this.vote = vote;
        return this;
    }

    public void setVote(Float vote) {
        this.vote = vote;
    }

    public JUser getJUser() {
        return juser;
    }

    public PokerVotes jUser(JUser jUser) {
        this.juser = jUser;
        return this;
    }

    public void setJUser(JUser jUser) {
        this.juser = jUser;
    }

    public Poker getPoker() {
        return poker;
    }

    public PokerVotes poker(Poker poker) {
        this.poker = poker;
        return this;
    }

    public void setPoker(Poker poker) {
        this.poker = poker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PokerVotes pokerVotes = (PokerVotes) o;
        if (pokerVotes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pokerVotes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PokerVotes{" +
            "id=" + id +
            ", vote='" + vote + "'" +
            '}';
    }
}
