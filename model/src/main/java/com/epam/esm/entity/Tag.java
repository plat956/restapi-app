package com.epam.esm.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Tag extends BaseEntity<Long> {

    @Size(max = 20, message = "Tag name must be less than or equal to 20 chars")
    @NotBlank(message = "Please provide a tag name")
    private String name;

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return name != null ? name.equals(tag.name) : tag.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
