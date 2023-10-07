package com.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
// !!! info parametresi, API'nin basligini ve surumunu iceren bir @Info nesnesi alir.
  //security parametresi, API'ye erismek icin gereken guvenlik gereksinimlerini belirler.
@OpenAPIDefinition(info = @Info(title = "StudentManagement01 API",version = "1.0.0"),
security = @SecurityRequirement(name = "Bearer"))
// !!! name parametresi, guvenlik semasinin adini belirtir. type parametresi, guvenlik semasinin
    // turunu belirtir ve SecuritySchemeType.Http degeri kullanarak Http guvenlik semasini belirler
@SecurityScheme(name = "Bearer",type = SecuritySchemeType.HTTP,scheme = "Bearer")
public class OpenAPIConfig {

    // !!! http://localhost:8080/swagger-ui/
}
