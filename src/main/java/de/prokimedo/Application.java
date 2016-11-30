/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Bilel-PC
 */
@SpringBootApplication
@ComponentScan(basePackageClasses={Application.class})
public class Application extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void setup() {
        ProkimedoConfiguration.configureLogger();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        setup();
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        setup();
        SpringApplication.run(Application.class);
    }



// @Bean
//    public CommandLineRunner demo(KrankheitRepo krankheitRepo, MedikamentVersionRepo medVersionRepo, MedikamentRepo medRepo) {
//        return new CommandLineRunner() {
//
//            public void run(String[] args) {
//
//                Date d = new Date(2016, 10, 02);
//                Date d2 = new Date();
//                d2.setYear(115);
//                d2.setMonth(02);
//                d2.setDate(13);
//                System.out.println(d2);
//                Krankheit kr = new Krankheit();
//                kr.setTitle("hallo");
//                kr.setAutor("bilel");
//                krankheitRepo.save(kr);
//                MedikamentVersion ver = new MedikamentVersion();
//                ver.setCurrent(Boolean.TRUE);
//                ver.setTitle("new");
//                Medikament med = new Medikament();
//                med.setPzn("126");
//                med.setBezeichnung("new");
//                medRepo.save(med);
//                ArrayList<Medikament> list = new ArrayList<>();
//                list.add(med);
//                ver.setListMedikament(list);
//                medVersionRepo.save(ver);
//                kr.setListMedikament(list);
//                krankheitRepo.save(kr);
//                List<Krankheit> l = krankheitRepo.findByListMedikamentPzn("126");
//
//            }
//        };
//    }

}
