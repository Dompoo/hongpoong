package Dompoo.Hongpoong.request.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomCreateRequest {

    private String name;
    private List<Long> members;
}
