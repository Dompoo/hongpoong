package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Instrument {

    private final Long id;
    private final String name;
    private final InstrumentType type;
    private final Boolean available;
    private final String imageUrl;
    private final Club club;
    
    public Instrument withEdited(String name, InstrumentType type, Boolean available, String imageUrl) {
        return Instrument.builder()
                .id(this.id)
                .name(name == null ? this.name : name)
                .type(type == null ? this.type : type)
                .available(available == null ? this.available : available)
                .imageUrl(imageUrl == null ? this.imageUrl : imageUrl)
                .club(this.club)
                .build();
    }
    
    public Instrument withBorrow() {
        return Instrument.builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .available(false)
                .imageUrl(this.imageUrl)
                .club(this.club)
                .build();
    }
    
    public Instrument returnInstrument() {
        return Instrument.builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .available(true)
                .imageUrl(this.imageUrl)
                .club(this.club)
                .build();
    }
}
