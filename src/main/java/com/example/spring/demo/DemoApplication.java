package com.example.spring.demo;

import com.example.spring.demo.api.files.upload.FilesStorageService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@OpenAPIDefinition(
			info = @Info(
					title = "Code-First Approach (reflectoring.io)",
					description = "" +
							"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur et rhoncus quam. Aenean quis augue ac eros pulvinar malesuada. " +
							"In sagittis elit egestas tincidunt iaculis. " +
							"Donec eu lacus vitae nulla varius consectetur a vel quam. Aliquam erat volutpat. Duis eget ullamcorper tellus",
					contact = @Contact(name = "Reflectoring", url = "https://reflectoring.io", email = "petros.stergioulas94@gmail.com"),
					license = @License(name = "MIT Licence", url = "https://github.com/thombergs/code-examples/blob/master/LICENSE")),
			servers = @Server(url = "http://localhost:8080")
	)
	@SecurityScheme(name = "api", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
	class OpenAPIConfiguration {
	}

	@Override
	public void run(String... arg) throws Exception {
//    storageService.deleteAll();
		storageService.init();
	}
}
