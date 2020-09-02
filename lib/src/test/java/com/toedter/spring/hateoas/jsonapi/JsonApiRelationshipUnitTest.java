/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toedter.spring.hateoas.jsonapi;

import com.toedter.spring.hateoas.jsonapi.support.Movie;
import org.junit.jupiter.api.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("JsonApiRelationship Unit Test")
class JsonApiRelationshipUnitTest {

    private Movie movie;

    @BeforeEach
    void beforeEach() {
        movie = new Movie("1", "Star Wars");
    }

    @Test
    void should_add_data_to_empty_relation() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);
        JsonApiResource data = (JsonApiResource) jsonApiRelationship.getData();

        assertThat(data.getId()).isEqualTo("1");
        assertThat(data.getType()).isEqualTo("movies");
    }

    @Test
    void should_create_of_entity_model() {
        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(EntityModel.of(movie));
        JsonApiResource data = (JsonApiResource) jsonApiRelationship.getData();

        assertThat(data.getId()).isEqualTo("1");
        assertThat(data.getType()).isEqualTo("movies");
    }

    @Test
    void should_create_of_object() {
        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(movie);
        JsonApiResource data = (JsonApiResource) jsonApiRelationship.getData();

        assertThat(data.getId()).isEqualTo("1");
        assertThat(data.getType()).isEqualTo("movies");
    }

    @Test
    void should_create_of_object_as_collection() {
        JsonApiRelationship jsonApiRelationship =
                JsonApiRelationship.of(movie).isAlwaysSerializedWithDataArray();
        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.get(0).getId()).isEqualTo("1");
        assertThat(data.get(0).getType()).isEqualTo("movies");
    }

    @Test
    void should_create_of_collection() {
        List<Movie> collection = new ArrayList<>();
        collection.add(movie);

        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(collection);
        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.get(0).getId()).isEqualTo("1");
        assertThat(data.get(0).getType()).isEqualTo("movies");
    }

    @Test
    void should_create_of_empty_collection() {
        List<Test> collection = new ArrayList<>();

        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(collection);
        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.size()).isEqualTo(0);
    }

    @Test
    void should_create_of_links() {
        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(Links.NONE);
        final Links links = jsonApiRelationship.getLinks();
        assertThat(links).isEqualTo(Links.NONE);
    }

    @Test
    void should_create_of_meta() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("key", "value");

        JsonApiRelationship jsonApiRelationship = JsonApiRelationship.of(meta);

        assertThat(jsonApiRelationship.getMeta()).isEqualTo(meta);
    }

    @Test
    void should_create_with_data_as_collection_on_first_statement() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.isAlwaysSerializedWithDataArray();
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.get(0).getId()).isEqualTo("1");
        assertThat(data.get(0).getType()).isEqualTo("movies");
    }

    @Test
    void should_create_with_data_as_collection_on_second_statement() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);
        jsonApiRelationship = jsonApiRelationship.isAlwaysSerializedWithDataArray();

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.get(0).getId()).isEqualTo("1");
        assertThat(data.get(0).getType()).isEqualTo("movies");
    }

    @Test
    void should_create_with_data_as_collection_on_third_statement() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);
        jsonApiRelationship = jsonApiRelationship.addDataObject(new Movie("2", "Test"));
        jsonApiRelationship = jsonApiRelationship.isAlwaysSerializedWithDataArray();

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.get(0).getId()).isEqualTo("1");
        assertThat(data.get(0).getType()).isEqualTo("movies");
    }

    @Test
    void should_create_with_data_as_empty_collection() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.isAlwaysSerializedWithDataArray();

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.isEmpty()).isTrue();
    }

    @Test
    void should_add_empty_data_collection() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataCollection(Collections.EMPTY_LIST);

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.isEmpty()).isTrue();
    }

    @Test
    void should_add_data_collection_to_existing_single_data() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);
        jsonApiRelationship = jsonApiRelationship.addDataCollection(Collections.EMPTY_LIST);

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.size()).isEqualTo(1);
    }
    @Test
    void should_add_data_collection_to_existing_data_colection() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(movie);
        jsonApiRelationship = jsonApiRelationship.addDataObject(new Movie("2", "Movie 2"));

        Collection<Movie> movies = new ArrayList<>();
        movies.add(new Movie("3", "Movie 3"));
        movies.add(new Movie("4", "Movie 4"));

        jsonApiRelationship = jsonApiRelationship.addDataCollection(movies);

        List<JsonApiResource> data = (List<JsonApiResource>) jsonApiRelationship.getData();

        assertThat(data.size()).isEqualTo(4);
    }

    @Test
    void should_validate_relationship_with_constructor() {
        JsonApiRelationship jsonApiRelationship =
                new JsonApiRelationship(movie, Links.of(Link.of("x")), new HashMap<>());
        assertThat(jsonApiRelationship.isValid()).isTrue();
    }

    @Test
    void should_validate_relationship_with_wither() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(new JsonApiResource("1", "type"));
        jsonApiRelationship = jsonApiRelationship.withLinks(Links.of(Link.of("x")));
        jsonApiRelationship = jsonApiRelationship.withMeta(new HashMap<>());
        assertThat(jsonApiRelationship.isValid()).isTrue();
    }

    @Test
    void should_validate_relationship_with_wither_and_multiple_data() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        jsonApiRelationship = jsonApiRelationship.addDataObject(new JsonApiResource("1", "type"));
        jsonApiRelationship = jsonApiRelationship.addDataObject(new JsonApiResource("2", "type"));
        jsonApiRelationship = jsonApiRelationship.addDataObject(new JsonApiResource("3", "type"));
        jsonApiRelationship = jsonApiRelationship.withLinks(Links.of(Link.of("x")));
        jsonApiRelationship = jsonApiRelationship.withMeta(new HashMap<>());
        assertThat(jsonApiRelationship.isValid()).isTrue();
    }

    @Test
    void should_validate_invalid_null_relationship() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, null);
        assertThat(jsonApiRelationship.isValid()).isFalse();
    }

    @Test
    void should_validate_invalid_single_data() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(new Object(), null, null);
        assertThat(jsonApiRelationship.isValid()).isFalse();
    }

    @Test
    void should_validate_invalid_collection_data() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(Collections.singletonList(new Object()), null, null);
        assertThat(jsonApiRelationship.isValid()).isFalse();
    }

    @Test
    void should_validate_invalid_meta() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, null, new HashMap<>());
        assertThat(jsonApiRelationship.isValid()).isTrue();
    }

    @Test
    void should_validate_invalid_links() {
        JsonApiRelationship jsonApiRelationship = new JsonApiRelationship(null, Links.NONE, null);
        assertThat(jsonApiRelationship.isValid()).isFalse();
    }
}