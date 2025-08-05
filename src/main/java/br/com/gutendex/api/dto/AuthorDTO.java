package br.com.gutendex.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("birth_year")
    private Integer birthYear;

    @JsonProperty("death_year")
    private Integer deathYear;

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public AuthorDTO() {}

}