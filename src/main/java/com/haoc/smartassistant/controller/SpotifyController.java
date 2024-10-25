package com.haoc.smartassistant.controller;

import com.haoc.smartassistant.constant.SpotifyConstant;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.net.URI;


/**
 * spotify controller
 *
 */

@RestController
@RequestMapping("/spotify")
@Slf4j
public class SpotifyController {


    private static final URI redirectUri = SpotifyHttpManager.makeUri(SpotifyConstant.REDIRECT_URL);
    private static  String code = "";


    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(SpotifyConstant.CLIENT_ID)
            .setClientSecret(SpotifyConstant.CLIENT_SECRET)
            .setRedirectUri(redirectUri)
            .build();


    @GetMapping("/login")
    @ResponseBody
    public String spotifyLogin() {

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private, user-read-email")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();

    }

    @GetMapping(value = "get-user-code")
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // todo redirect to  frontend spotify page
        response.sendRedirect("http://localhost:3000/dashboard/moodbasedmusicrecommendations");
        return spotifyApi.getAccessToken();
    }


}
