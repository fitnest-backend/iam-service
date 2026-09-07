package az.fitnest.identity.controller;

import az.fitnest.identity.dto.response.PublicLegalDocumentResponse;
import az.fitnest.identity.service.LegalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/public/landing")
@RequiredArgsConstructor
@Tag(name = "Landing Public", description = "Unauthenticated legal documents used by the website.")
public class PublicLandingLegalController {

    private static final CacheControl JSON_CACHE = CacheControl
            .maxAge(Duration.ofMinutes(2))
            .cachePublic()
            .staleWhileRevalidate(Duration.ofMinutes(2));

    private final LegalService legalService;

    @GetMapping("/privacy-policy")
    @Operation(summary = "Public privacy policy", description = "Active privacy policy content. Same document admins edit.")
    public ResponseEntity<PublicLegalDocumentResponse> getPrivacyPolicy(
            @Parameter(description = "Language code (AZ, EN, RU)") @RequestParam(defaultValue = "AZ") String lang
    ) {
        return cached(PublicLegalDocumentResponse.from(legalService.getPrivacyPolicy(lang, "html")));
    }

    @GetMapping("/terms-of-use")
    @Operation(summary = "Public terms of use", description = "Active terms of use content. Same document admins edit.")
    public ResponseEntity<PublicLegalDocumentResponse> getTermsOfUse(
            @Parameter(description = "Language code (AZ, EN, RU)") @RequestParam(defaultValue = "AZ") String lang
    ) {
        return cached(PublicLegalDocumentResponse.from(legalService.getTermsOfUse(lang, "html")));
    }

    private static ResponseEntity<PublicLegalDocumentResponse> cached(PublicLegalDocumentResponse body) {
        return ResponseEntity.ok()
                .cacheControl(JSON_CACHE)
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.VARY, "Accept-Language")
                .body(body);
    }
}
