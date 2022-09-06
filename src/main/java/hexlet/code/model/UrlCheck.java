package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.*;
import java.net.URL;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
public class UrlCheck extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int statusCode;
    private String title;
    private String h1;
    @Lob
    private String description;

    @OneToOne
    @JoinColumn(name = "url_id")
    private URL url;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public UrlCheck(URL u){
        this.url = u;
    }
    public long getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
