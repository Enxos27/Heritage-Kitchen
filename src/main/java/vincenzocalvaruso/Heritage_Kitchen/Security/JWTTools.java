package vincenzocalvaruso.Heritage_Kitchen.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.UnauthorizedException;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(User utente) {

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(utente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }


    public void verifyToken(String token) {

        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (SignatureException ex) {
            // La firma non corrisponde (il token è stato manipolato o il segreto è cambiato)
            throw new UnauthorizedException("Firma del token non valida! Possibile manipolazione rilevata.");

        } catch (MalformedJwtException ex) {
            // Il token non ha la struttura corretta (mancano pezzi o non è un JWT)
            throw new UnauthorizedException("Il token è malformato! Controlla il formato dell'header Authorization.");

        } catch (Exception ex) {
            // Qualsiasi altro errore generico
            throw new UnauthorizedException("Problemi generici col token! Effettua di nuovo il login.");
        }
    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
