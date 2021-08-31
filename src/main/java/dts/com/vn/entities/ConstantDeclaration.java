package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "constant", schema = "public")
public class ConstantDeclaration {
    @Id
    @Column(name = "constant_key")
    private String constantKey;

    @Column(name = "constant_value")
    private String constantValue;
}
