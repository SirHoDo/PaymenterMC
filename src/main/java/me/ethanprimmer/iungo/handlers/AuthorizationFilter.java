package me.ethanprimmer.iungo.handlers;

import me.ethanprimmer.iungo.Main;
import spark.Filter;
import spark.Request;
import spark.Response;

public class AuthorizationFilter implements Filter {
	private final Main plugin;
	public AuthorizationFilter(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handle(Request request, Response response) {
		String expectedToken = plugin.getConfig().getString("apiToken", "xxxxxxxxxxxxx");

		String authorizationHeader = request.headers("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			response.status(401);
			response.body("Unauthorized: Missing or invalid Authorization header");
			return;
		}

		String token = authorizationHeader.substring("Bearer ".length());

		if (!expectedToken.equals(token)) {
			response.status(401);
			response.body("Unauthorized: Invalid token");
			return;
		}

		request.attribute("authorized", true);
	}

}

