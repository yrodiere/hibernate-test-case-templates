package org.hibernate.bugs;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Struct;

import java.util.List;

@Embeddable
@Struct(name = "scene")
public class Scene {
    public List<Line> lines;

    public Scene() {
    }

    public Scene(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "lines=" + lines +
                '}';
    }
}
