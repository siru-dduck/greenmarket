package com.hanium.product.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hanium.product.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final MappingJackson2HttpMessageConverter converter;
	private final RestTemplate restTemplate;
	@Value("${service.chat.host}")
	private String SERVICE_CHAT_HOST;
	@Value("${service.chat.port}")
	private String SERVICE_CHAT_PORT;

	@Override
	public Integer getChatRoomId(Integer articleId, Integer buyerId) {
		if (articleId == null || buyerId == null) {
			return null;
		}
		final String baseUrl = "http://" + SERVICE_CHAT_HOST +":" + SERVICE_CHAT_PORT + "/api/chat/room?article_id={articleId}&user_id={buyerId}";
		Map<String, Integer> query = new HashMap<>();
		query.put("articleId", articleId);
		query.put("buyerId", buyerId);
		ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class, query);
		
		try {
			ObjectMapper mapper = converter.getObjectMapper();
			JsonNode chatRoomNode = mapper.readTree(response.getBody()).findPath("chatRoom");
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
			List<Map<String,Object>> chatRoom = mapper.readValue(chatRoomNode.toString() , collectionType);
			if(chatRoom.size() > 0) {
				return (Integer) chatRoom.get(0).get("id");				
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
