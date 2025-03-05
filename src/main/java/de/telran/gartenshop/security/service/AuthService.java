package de.telran.gartenshop.security.service;


import de.telran.gartenshop.dto.responsedto.UserResponseDto;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.UserRepository;
import de.telran.gartenshop.security.dto.JwtRequestDto;
import de.telran.gartenshop.security.dto.JwtResponseDto;
import de.telran.gartenshop.security.jwt.JwtAuthentication;
import de.telran.gartenshop.security.jwt.JwtProvider;
import de.telran.gartenshop.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService usersService;
    private final UserRepository userRepository;
    private final Mappers mappers;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;


    public JwtResponseDto login(JwtRequestDto authRequest) throws AuthException {
        UserEntity user = userRepository.findByEmail(authRequest.getEmail());
        String refreshToken;
        if (user != null) {
            final UserResponseDto userResponseDto = mappers.convertToUserResponseDto(user);

            if (passwordEncoder.matches(authRequest.getPassword(), userResponseDto.getPasswordHash())) {
                final String accessToken = jwtProvider.generateAccessToken(userResponseDto);
                refreshToken = jwtProvider.generateRefreshToken(userResponseDto);
                usersService.updateUserRefreshToken(userResponseDto, refreshToken); // сохраняем в БД новый refreshToken
                return new JwtResponseDto(accessToken, refreshToken);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
            }
        } else {
            throw new AuthException("User not found");
        }
    }

    public JwtResponseDto getAccessToken(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            UserResponseDto currentUser = usersService.getUserByEmail(login);
            final String savedRefreshToken = currentUser!=null ? currentUser.getRefreshToken() : null;
            //----
            // Compare the stored refresh token with the provided token
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                // Fetch the user data
                UserEntity user = userRepository.findByEmail(login);
                if (user == null) throw new AuthException("User is not found");
                user.setFavorites(null);
                final UserResponseDto userResponseDto = mappers.convertToUserResponseDto(user);
                // Generate a new access token
                final String accessToken = jwtProvider.generateAccessToken(userResponseDto);
                // Return a JwtResponse with the new access token
                return new JwtResponseDto(accessToken, null);
            }
        }
        // Return a JwtResponse with null values if validation fails
        return new JwtResponseDto(null, null);
    }


    public JwtResponseDto refresh(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            // Retrieve the stored refresh token for the user берем из нашей БД
            UserResponseDto currentUser = usersService.getUserByEmail(login);
            final String savedRefreshToken = currentUser!=null ? currentUser.getRefreshToken() : null;
            // Compare the stored refresh token with the provided token
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                // Fetch the user data
                UserEntity user = userRepository.findByEmail(login);
                if (user == null) throw new AuthException("User is not found");
                user.setFavorites(null);
                final UserResponseDto userResponseDto = mappers.convertToUserResponseDto(user);
                // Generate new access and refresh tokens
                final String newAccessToken = jwtProvider.generateAccessToken(userResponseDto);
                final String newRefreshToken = jwtProvider.generateRefreshToken(userResponseDto);

                usersService.updateUserRefreshToken(userResponseDto, refreshToken); // сохраняем в БД новый refreshToken
                // Return a JwtResponse with the new access and refresh tokens
                return new JwtResponseDto(newAccessToken, newRefreshToken);
            }
        }
        // Throw an AuthException if validation fails
        throw new AuthException("Invalid JWT token");
    }

      // return the JwtAuthentication object containing the authentication information.
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}



