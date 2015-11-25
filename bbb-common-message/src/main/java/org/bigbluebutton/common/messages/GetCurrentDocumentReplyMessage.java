package org.bigbluebutton.common.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GetCurrentDocumentReplyMessage implements ISubscribedMessage {
	public static final String GET_CURRENT_DOCUMENT_REPLY = "get_current_document_reply";
	public final String VERSION = "0.0.1";

	public final String meetingID;
	public final String requesterID;
	public final Map<String, Object> notes;

	public GetCurrentDocumentReplyMessage(String meetingID, String requesterID, Map<String, Object> notes) {
		this.meetingID = meetingID;
		this.requesterID = requesterID;
		this.notes = notes;
	}

	public String toJson() {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put(Constants.MEETING_ID, meetingId);
		payload.put(Constants.REQUESTER_ID, requesterID);
		payload.put(Constants.NOTES, notes);

		java.util.HashMap<String, Object> header = MessageBuilder.buildHeader(GET_CURRENT_DOCUMENT_REPLY, VERSION, null);

		return MessageBuilder.buildJson(header, payload);
	}

	public static GetCurrentDocumentReplyMessage fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);

		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");

			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (GET_CURRENT_DOCUMENT_REPLY.equals(messageName)) {
					if (payload.has(Constants.MEETING_ID) 
							&& payload.has(Constants.REQUESTER_ID)
							&& payload.has(Constants.NOTES)) {
						String meetingId = payload.get(Constants.MEETING_ID).getAsString();
						String requesterId = payload.get(Constants.REQUESTER_ID).getAsString();

						Type type = new TypeToken<Map<String, Object>>(){}.getType();
						Gson gson = new Gson();
						Map<String, Object> notes = gson.fromJson(payload.get(Constants.NOTES), type);

						return new GetCurrentDocumentReplyMessage(meetingId, requesterId, notes);
					}
				}
			}
		}
		return null;
	}
}