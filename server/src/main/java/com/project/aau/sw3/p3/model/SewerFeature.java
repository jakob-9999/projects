package com.project.aau.sw3.p3.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class SewerFeature {

    @Id
    @GeneratedValue
    private Long id;

    // Store as PostgreSQL JSONB
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode feature;

    public SewerFeature() {
    }

    public SewerFeature(JsonNode feature) {
        this.feature = feature;
    }

}
