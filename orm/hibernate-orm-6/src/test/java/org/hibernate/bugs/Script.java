package org.hibernate.bugs;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Struct;

import java.util.List;

@Embeddable
@Struct(name = "script")
public class Script {
    public List<Scene> scenes;

    public Script() {
    }

    public Script(List<Scene> scenes) {
        this.scenes = scenes;
    }

    @Override
    public String toString() {
        return "Script{" +
                "scenes=" + scenes +
                '}';
    }
}
