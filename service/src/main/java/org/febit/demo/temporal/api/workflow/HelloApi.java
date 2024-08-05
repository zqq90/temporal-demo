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
