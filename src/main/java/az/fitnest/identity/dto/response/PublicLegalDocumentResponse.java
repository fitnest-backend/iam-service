package az.fitnest.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record PublicLegalDocumentResponse(
        String version,
        String content,

        @JsonProperty("updated_at")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime updatedAt
) {
    public static PublicLegalDocumentResponse from(LegalDocumentResponse source) {
        return new PublicLegalDocumentResponse(
                source.version(),
                source.content(),
                source.updatedAt()
        );
    }
}
