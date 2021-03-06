package de.prokimedo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.prokimedo.Application;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Medikament;
import de.prokimedo.repository.IcdRepo;
import de.prokimedo.repository.IcdVersionRepo;
import de.prokimedo.repository.KrankheitRepo;
import de.prokimedo.repository.MedikamentRepo;
import de.prokimedo.repository.MedikamentVersionRepo;
import de.prokimedo.repository.ProzedurRepo;
import de.prokimedo.service.IcdService;
import de.prokimedo.service.KrankheitService;
import de.prokimedo.service.MedikamentService;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Bilel-PC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class KrankheitTest {

    private final RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Autowired
    KrankheitRepo krankheitRepo;
    @Autowired
    IcdService icdService;
    @Autowired
    MedikamentService medikamentService;
    @Autowired
    IcdRepo icdRepo;
    @Autowired
    IcdVersionRepo icdVersionRepo;
    @Autowired
    MedikamentRepo medikamentRepo;
    @Autowired
    ProzedurRepo prozedurRepo;
    @Autowired
    MedikamentVersionRepo medikamentVersionRepo;
    @Autowired
    KrankheitService krankheitService;

    public KrankheitTest() {
    }

    @Before
    public void setUp() {
        krankheitRepo.deleteAll();
        prozedurRepo.deleteAll();
        medikamentVersionRepo.deleteAll();
        medikamentRepo.deleteAll();
        icdVersionRepo.deleteAll();
        icdRepo.deleteAll();
    }

    @Test
    public void createKrankheitTest() throws JsonProcessingException {

        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        String URL = "http://localhost:" + port + "/krankheit/save";
        restTemplate.postForEntity(URL, k, Krankheit.class);
        String URL2 = "http://localhost:" + port + "/krankheit/test";
        Krankheit krankheit = restTemplate.getForEntity(URL2, Krankheit.class).getBody();
        assertEquals("bilel", krankheit.getAutor());
    }

    @Test
    public void updateKrankheitTest() throws JsonProcessingException {

        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        Krankheit response = krankheitRepo.save(k);
        response.setNotes("Notes");
        String URL = "http://localhost:" + port + "/krankheit/update";
        restTemplate.postForEntity(URL, k, Krankheit.class);
        String URL2 = "http://localhost:" + port + "/krankheit/test";
        Krankheit krankheit = restTemplate.getForEntity(URL2, Krankheit.class).getBody();
        assertEquals("Notes", krankheit.getNotes());
    }

    @Test
    public void deleteKrankheitTest() throws JsonProcessingException {

        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        krankheitRepo.save(k);
        String URL = "http://localhost:" + port + "/krankheit/delete/test";
        restTemplate.getForEntity(URL, Krankheit.class).getBody();
        String URL2 = "http://localhost:" + port + "/krankheit/test";
        Krankheit response = restTemplate.getForEntity(URL2, Krankheit.class).getBody();
        assertNull(response);
    }

    @Test
    public void queryKrankheitTest() throws JsonProcessingException {

        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        this.krankheitRepo.save(k);
        Krankheit k2 = new Krankheit();
        k2.setAutor("bilel");
        k2.setTitle("test");
        this.krankheitRepo.save(k2);
        String URL2 = "http://localhost:" + port + "/krankheit/query";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(2, list.size());
    }
//        @Test
//    public void queryKrankheitTestFilter() throws JsonProcessingException {
//
//        Krankheit k = new Krankheit();
//        k.setAutor("bilel");
//        k.setTitle("test");
//        this.krankheitRepo.save(k);
//        Krankheit k2 = new Krankheit();
//        k2.setAutor("bilel");
//        k2.setTitle("title");
//        this.krankheitRepo.save(k2);
//        List<Krankheit> response = this.krankheitService.query("bilel");
//        assertEquals(1, response.size());
//
//    }

    @Test
    public void getIcdKrankheitTest() throws JsonProcessingException {
        Icd icd = new Icd();
        icd.setCode("Z125");
        icd.setDiagnose("Test");
        icd.setType("Hauptdiagnose");
        icdService.save(icd);
        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setNotes("Z125");
        this.krankheitService.save(k);
        String URL2 = "http://localhost:" + port + "/krankheit/icd/test";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(1, list.size());
    }

    @Test
    public void getMedikamentKrankheitTest() throws JsonProcessingException {
        Medikament med = new Medikament();
        med.setPzn("123");
        med.setBezeichnung("test");
        medikamentService.save(med);
        Krankheit k = new Krankheit();
        k.setAutor("bilel");
        k.setTitle("test");
        k.setTherapieTxt("123");
        this.krankheitService.save(k);
        String URL2 = "http://localhost:" + port + "/krankheit/medikament/test";
        List list = restTemplate.getForEntity(URL2, List.class).getBody();
        assertEquals(1, list.size());
    }

    @After
    public void TearDown() {

        krankheitRepo.deleteAll();
        prozedurRepo.deleteAll();
        medikamentVersionRepo.deleteAll();
        medikamentRepo.deleteAll();
        icdVersionRepo.deleteAll();
        icdRepo.deleteAll();

    }
}
