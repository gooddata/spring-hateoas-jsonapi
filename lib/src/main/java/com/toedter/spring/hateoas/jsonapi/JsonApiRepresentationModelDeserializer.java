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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

class JsonApiRepresentationModelDeserializer extends AbstractJsonApiModelDeserializer<RepresentationModel<?>>
        implements ContextualDeserializer {

    JsonApiRepresentationModelDeserializer() {
        super();
    }

    protected JsonApiRepresentationModelDeserializer(JavaType contentType) {
        super(contentType);
    }

    @Override
    protected RepresentationModel<?> convertToRepresentationModel(List<Object> resources, JsonApiDocument doc) {
        Links links = doc.getLinks();
        if (resources.size() == 1 && resources.get(0) instanceof RepresentationModel<?>) {
            RepresentationModel<?> representationModel = (RepresentationModel<?>) resources.get(0);
            representationModel.add(links);
            return representationModel;
        }
        throw new RuntimeException("Cannot deserialize input to RepresentationModel");
    }

    protected JsonDeserializer<?> createJsonDeserializer(JavaType type) {
        return new JsonApiRepresentationModelDeserializer(type);
    }
}