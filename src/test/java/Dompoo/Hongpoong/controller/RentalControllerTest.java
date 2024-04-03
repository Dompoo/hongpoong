package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private RentalRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("전체 대여 조회")
    void list() throws Exception {
        //given
        repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        repository.save(Rental.builder()
                .product("소고")
                .count(4)
                .fromMember("화랑")
                .toMember("들녘")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build());

        //expected
        mockMvc.perform(get("/rental"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product").value("장구"))
                .andExpect(jsonPath("$[0].count").value(1))
                .andExpect(jsonPath("$[0].fromMember").value("악반"))
                .andExpect(jsonPath("$[0].toMember").value("산틀"))
                .andExpect(jsonPath("$[0].date").value("2000-12-20"))
                .andExpect(jsonPath("$[0].time").value(13))
                .andExpect(jsonPath("$[1].product").value("소고"))
                .andExpect(jsonPath("$[1].count").value(4))
                .andExpect(jsonPath("$[1].fromMember").value("화랑"))
                .andExpect(jsonPath("$[1].toMember").value("들녘"))
                .andExpect(jsonPath("$[1].date").value("2000-12-30"))
                .andExpect(jsonPath("$[1].time").value(15))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가")
    void addOne() throws Exception {
        //given
        RentalCreateRequest request = RentalCreateRequest.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("대여 상세 조회")
    void getOne() throws Exception {
        //given
        Rental retnal = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //expected
        mockMvc.perform(get("/rental/{id}", retnal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("장구"))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.fromMember").value("악반"))
                .andExpect(jsonPath("$.toMember").value("산틀"))
                .andExpect(jsonPath("$.date").value("2000-12-20"))
                .andExpect(jsonPath("$.time").value(13))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 상세 조회")
    void getOneFail() throws Exception {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //expected
        mockMvc.perform(get("/rental/{id}", rental.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 대여입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 수정")
    void edit() throws Exception {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("대여 삭제")
    void delete1() throws Exception {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    void deleteFail() throws Exception {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId() + 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}