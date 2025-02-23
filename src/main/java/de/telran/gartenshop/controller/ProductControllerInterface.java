package de.telran.gartenshop.controller;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Products-Endpoint", description = "Controller for adding Products",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
public interface ProductControllerInterface {
}
