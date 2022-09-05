package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import io.ebean.annotation.NotNull;
import javax.persistence.*;
import java.security.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "URL")
@NoArgsConstructor
public class Url extends Model {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @WhenCreated
    private Timestamp createdAt;

    public Url(String name){
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
