/*
 * Copyright 2023-present febit.org (support@febit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.febit.demo.temporal.api.workflow;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.febit.demo.temporal.JsonApiMapping;
import org.febit.demo.temporal.workflow.HelloService;
import org.febit.demo.temporal.workflow.model.User;
import org.febit.lang.protocol.IBasicApi;
import org.febit.lang.protocol.IResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Common")
@JsonApiMapping({
        "/api/v1/workflows/hello"
})
@RequiredArgsConstructor
public class HelloApi implements IBasicApi {

    final HelloService service;

    @PostMapping
    public IResponse<String> post(
            @RequestBody @Valid User person
    ) {
        var msg = service.post(person);
        return ok(msg);
    }
}
