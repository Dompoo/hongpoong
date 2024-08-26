package Dompoo.Hongpoong.api.dto.request.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "name":"roomName"
    "members":[1, 2, 3]
}
 */
public class ChatRoomCreateRequest {

    private String name;
    private List<Long> members;

    @Builder
    public ChatRoomCreateRequest(String name, List<Long> members) {
        this.name = name;
        this.members = members;
    }
}
