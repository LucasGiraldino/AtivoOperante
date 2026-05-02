package unoeste.fipp.ativooperante_be;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Ativo Operante API",
        version = "1.0",
        description = "API para denúncias comunitárias — cidadãos reportam problemas e admin gerencia",
        contact = @Contact(name = "Ativo Operante")
    )
)
public class AtivoOperanteBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtivoOperanteBeApplication.class, args);
    }

}
