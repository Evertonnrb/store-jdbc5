package model.entities;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"baseSalary"})
public class Seller {
    private Integer id;
    private String name;
    private String email;
    private Date birthDay;
    private @Setter(AccessLevel.NONE) BigDecimal baseSalary;
    private Department department;
}
