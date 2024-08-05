package org.febit.demo.temporal.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.febit.boot.common.permission.AnonymousApi;
import org.febit.demo.temporal.JsonApiMapping;
import org.febit.lang.protocol.IBasicApi;
import org.febit.lang.protocol.IResponse;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Common")
@JsonApiMapping({
        "/api/v1/common"
})
@RequiredArgsConstructor
public class CommonApi implements IBasicApi {

    @AnonymousApi
    @GetMapping(value = "/ping")
    public IResponse<String> ping() {
        return ok("pong");
    }
}
