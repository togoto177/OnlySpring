package only.service;

import java.util.List;

import only.model.Chat;

public interface TextMessageListService {
	
	public List<Chat> getChatMessageList(String userid);
	
	public int getUnreadMessageCount(String userid);
	
	public List<Chat> UnreadRoomMessageCount(String userid);
}
