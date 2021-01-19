package com.hanium.product.service.impl;

import com.hanium.product.dto.ChatRoomDto;
import com.hanium.product.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final RestTemplate restTemplate;
	@Value("${service.chat.host}")
	private String SERVICE_CHAT_HOST;
	@Value("${service.chat.port}")
	private String SERVICE_CHAT_PORT;

	@Override
	public Integer getChatRoomId(Integer articleId, Integer buyerId) {
		final String baseUrl = "http://" + SERVICE_CHAT_HOST +":" + SERVICE_CHAT_PORT + "/api/chat/room?article_id={articleId}&user_id={buyerId}";
		Map<String, Integer> query = new HashMap<>();
		query.put("articleId", articleId);
		query.put("buyerId", buyerId);
		ResponseEntity<ChatRoomDto.ResponseInfo> response = restTemplate.getForEntity(baseUrl, ChatRoomDto.ResponseInfo.class, query);
		List<ChatRoomDto.Info> chatRoomDtoList = response.getBody().getChatRoom();
		if(chatRoomDtoList.size() > 0) {
			return chatRoomDtoList.get(0).getId();
		} else {
			return null;
		}
	}
}
