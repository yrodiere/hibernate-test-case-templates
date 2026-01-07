package org.hibernate.bugs;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Struct;

@Embeddable
@Struct(name = "line")
public record Line(String character, String content) {
}
