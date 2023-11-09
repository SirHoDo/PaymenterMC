package me.ethanprimmer.iungo.webhooks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ethanprimmer.iungo.Main;
import me.ethanprimmer.iungo.handlers.donations.DonationCommands;
import spark.Request;
import spark.Response;

public class PaymentWebhook {
	private final Main plugin;

	public PaymentWebhook(Main plugin) {
		this.plugin = plugin;
	}

	public Object handle(Request request, Response response) {
		String jsonContent = request.body();
		JsonObject jsonObject = parseJson(jsonContent);

		plugin.getLogger().info(jsonContent);

		Boolean authorized = request.attribute("authorized");
		if (authorized != null && authorized) {
			if (jsonObject.has("commands")) {
				DonationCommands donationCommands = new DonationCommands(plugin);
				String result = donationCommands.handle(jsonObject);
				return result;
			} else {
				return "Successfully got the request";
			}
		} else {
			response.status(401);
			return "Unauthorized: Missing or invalid Authorization header";
		}
	}

	private JsonObject parseJson(String jsonContent) {
		try {
			JsonParser parser = new JsonParser();
			return parser.parse(jsonContent).getAsJsonObject();
		} catch (Exception e) {
			return new JsonObject();
		}
	}
}



