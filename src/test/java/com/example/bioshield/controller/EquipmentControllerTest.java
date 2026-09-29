package com.example.bioshield.controller;

import com.example.bioshield.model.AnalyzerEquipment;
import com.example.bioshield.model.Equipment;
import com.example.bioshield.model.ThermalEquipment;
import com.example.bioshield.service.EquipmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipmentService equipmentService;

    private Equipment eq1;
    private Equipment eq2;

    @BeforeEach
    public void setup() {
        ThermalEquipment tEq = new ThermalEquipment();
        tEq.setId(1L);
        tEq.setAssetTag("EQ-1001");
        tEq.setName("Incubator A");
        tEq.setStatus("ACTIVE");
        tEq.setMinTemperatureCelsius(4.0);
        tEq.setMaxTemperatureCelsius(37.0);
        eq1 = tEq;

        AnalyzerEquipment aEq = new AnalyzerEquipment();
        aEq.setId(2L);
        aEq.setAssetTag("EQ-1002");
        aEq.setName("Mass Spec B");
        aEq.setStatus("WARNING");
        aEq.setFluidChannels(12);
        eq2 = aEq;
    }

    @Test
    public void testGetAllEquipments() throws Exception {
        Mockito.when(equipmentService.getAllEquipments()).thenReturn(Arrays.asList(eq1, eq2));

        mockMvc.perform(get("/api/equipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].assetTag").value("EQ-1001"))
                .andExpect(jsonPath("$[1].assetTag").value("EQ-1002"));
    }

    @Test
    public void testGetEquipmentById_Found() throws Exception {
        Mockito.when(equipmentService.getEquipmentById(1L)).thenReturn(Optional.of(eq1));

        mockMvc.perform(get("/api/equipment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Incubator A"));
    }

    @Test
    public void testGetEquipmentById_NotFound() throws Exception {
        Mockito.when(equipmentService.getEquipmentById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/equipment/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateEquipment_Success() throws Exception {
        Mockito.when(equipmentService.saveEquipment(any(Equipment.class))).thenReturn(eq1);

        mockMvc.perform(post("/api/equipment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"assetTag\": \"EQ-1001\", \"name\": \"Incubator A\", \"status\": \"ACTIVE\", \"equipmentType\": \"THERMAL\", \"minTemperatureCelsius\": 4.0, \"maxTemperatureCelsius\": 37.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assetTag").value("EQ-1001"));
    }

    @Test
    public void testCreateEquipment_ValidationError() throws Exception {
        Mockito.when(equipmentService.saveEquipment(any(Equipment.class)))
                .thenThrow(new IllegalArgumentException("Invalid format"));

        mockMvc.perform(post("/api/equipment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"assetTag\": \"EQ-1001\", \"name\": \"Incubator A\", \"status\": \"ACTIVE\", \"equipmentType\": \"THERMAL\", \"minTemperatureCelsius\": 4.0, \"maxTemperatureCelsius\": 37.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid format"));
    }

    @Test
    public void testUpdateEquipment_Success() throws Exception {
        Mockito.when(equipmentService.updateEquipment(eq(1L), any(Equipment.class))).thenReturn(eq1);

        mockMvc.perform(put("/api/equipment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"assetTag\": \"EQ-1001\", \"name\": \"Incubator A\", \"status\": \"ACTIVE\", \"equipmentType\": \"THERMAL\", \"minTemperatureCelsius\": 4.0, \"maxTemperatureCelsius\": 37.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetTag").value("EQ-1001"));
    }

    @Test
    public void testUpdateEquipment_NotFound() throws Exception {
        Mockito.when(equipmentService.updateEquipment(eq(99L), any(Equipment.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Not found"));

        mockMvc.perform(put("/api/equipment/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"assetTag\": \"EQ-1001\", \"name\": \"Incubator A\", \"status\": \"ACTIVE\", \"equipmentType\": \"THERMAL\", \"minTemperatureCelsius\": 4.0, \"maxTemperatureCelsius\": 37.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void testDeleteEquipment_Success() throws Exception {
        Mockito.doNothing().when(equipmentService).deleteEquipment(1L);

        mockMvc.perform(delete("/api/equipment/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEquipment_NotFound() throws Exception {
        Mockito.doThrow(new jakarta.persistence.EntityNotFoundException("Not found"))
                .when(equipmentService).deleteEquipment(99L);

        mockMvc.perform(delete("/api/equipment/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStatuses() throws Exception {
        mockMvc.perform(get("/api/equipment/statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$[0]").value("ACTIVE"))
                .andExpect(jsonPath("$[1]").value("WARNING"));
    }
}
