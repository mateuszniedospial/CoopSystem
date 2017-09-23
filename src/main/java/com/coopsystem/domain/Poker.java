package com.coopsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Poker.
 */
@Entity
@Table(name = "poker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Poker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_started")
    private Boolean isStarted;

    @Column(name = "is_stoped")
    private Boolean isStoped;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn()
    private Sprint sprint;

    @OneToOne
    @JoinColumn()
    private JGroup jGroup;

    @OneToMany(mappedBy = "poker")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PokerVotes> pokerVotes = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsStarted() {
        return isStarted;
    }

    public Poker isStarted(Boolean isStarted) {
        this.isStarted = isStarted;
        return this;
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }

    public Boolean isIsStoped() {
        return isStoped;
    }

    public Poker isStoped(Boolean isStoped) {
        this.isStoped = isStoped;
        return this;
    }

    public void setIsStoped(Boolean isStoped) {
        this.isStoped = isStoped;
    }

    public String getTitle() {
        return title;
    }

    public Poker title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Poker sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public JGroup getJGroup() {
        return jGroup;
    }

    public Poker jGroup(JGroup jGroup) {
        this.jGroup = jGroup;
        return this;
    }

    public void setJGroup(JGroup jGroup) {
        this.jGroup = jGroup;
    }

    public Set<PokerVotes> getPokerVotes() {
        return pokerVotes;
    }

    public Poker pokerVotes(Set<PokerVotes> pokerVotes) {
        this.pokerVotes = pokerVotes;
        return this;
    }

    public Poker addPokerVotes(PokerVotes pokerVotes) {
        this.pokerVotes.add(pokerVotes);
        pokerVotes.setPoker(this);
        return this;
    }

    public Poker removePokerVotes(PokerVotes pokerVotes) {
        this.pokerVotes.remove(pokerVotes);
        pokerVotes.setPoker(null);
        return this;
    }

    public void setPokerVotes(Set<PokerVotes> pokerVotes) {
        this.pokerVotes = pokerVotes;
    }

    public Task getTask() {
        return task;
    }

    public Poker task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poker poker = (Poker) o;
        if (poker.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, poker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Poker{" +
            "id=" + id +
            ", isStarted='" + isStarted + "'" +
            ", isStoped='" + isStoped + "'" +
            ", title='" + title + "'" +
            '}';
    }
}
