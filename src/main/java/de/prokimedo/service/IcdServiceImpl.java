/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.prokimedo.service;

import com.google.common.collect.Lists;
import de.prokimedo.entity.Krankheit;
import de.prokimedo.entity.Icd;
import de.prokimedo.entity.IcdUsed;
import de.prokimedo.entity.IcdVersion;
import de.prokimedo.entity.Prozedur;
import de.prokimedo.repository.IcdRepo;
import de.prokimedo.repository.IcdVersionRepo;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Bilel-PC
 */
@Service
public class IcdServiceImpl implements IcdService {

    IcdRepo repo;
    IcdVersionRepo versionRepo;
    @Autowired
    KrankheitService krankheitService;
    @Autowired
    ProzedurService prozedurService;

    @Autowired
    public IcdServiceImpl(IcdRepo repo, IcdVersionRepo versionRepo, EntityManager em) {
        this.repo = repo;
        this.versionRepo = versionRepo;
    }

    @Override
    public Icd read(String code) {
        return repo.findByCode(code).get(0);
    }

    /**
     *Save icd
     * @param icd
     * @return icd
     */
    @Override
    public Icd save(Icd icd) {

        Icd icd2 = repo.save(icd);
        IcdVersion version = this.readCurrent();
        if (version == null) {
            version = new IcdVersion();
            version.setTitle("default");
            version.setCurrent(Boolean.TRUE);
            this.versionRepo.save(version);
        }
        version.getListIcd().add(icd2);
        this.versionRepo.save(version);
        return icd2;
    }

    /**
     *Search the list of all the medicaments in the actual version
     * @return list medicaments
     */
    @Override
    public List<Icd> query() {
        IcdVersion version = this.readCurrent();
        if (version == null) {
            return null;
        } else {
            return version.getListIcd();
        }
    }


    @Override
    public Icd update(Icd icd) {
        return this.repo.save(icd);
    }

    /**
     *delete medicament
     * @param icd
     */
    @Override
    public void delete(Icd icd) {
        List<Krankheit> krankheits = this.krankheitService.readIcdKrankheit(icd.getCode());
        krankheits.stream().map((krankheit) -> {
            krankheit.getListIcd().remove(icd);
            return krankheit;
        }).forEach((krankheit) -> {
            this.krankheitService.save2(krankheit);
        });

        List<Prozedur> prozedurs = this.prozedurService.readIcdProzedur(icd.getCode());
        prozedurs.stream().map((prozedur) -> {
            prozedur.getListIcd().remove(icd);
            return prozedur;
        }).forEach((prozedur) -> {
            this.prozedurService.save2(prozedur);
        });        
        IcdVersion version = this.readCurrent();
        version.getListIcd().remove(icd);
        this.versionRepo.save(version);
        this.repo.delete(icd);
    }


    /**
     * read the actual version
     * @return IcdVersion
     */
    @Override
    public IcdVersion readCurrent() {
        return versionRepo.findByCurrent(Boolean.TRUE);
    }

    /**
     * save a new version from a CSV file
     * @param file
     * @param version
     * @return the new, deleted and updated medicaments
     * @throws Throwable
     */
    @Override
    public HashMap saveVersion(MultipartFile file, String version) throws Throwable {
        String csvFile = this.transferToFile(file);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int i = 0;
        List<Icd> icdList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                if (i != 0) {
                    String[] icdNummer = line.split(cvsSplitBy);

                    if ("Diagnose".equals(icdNummer[0])) {
                    } else {
                        icdList.add(new Icd(icdNummer[1], icdNummer[0], icdNummer[2]));
                    }
                }
                i++;

            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                br.close();
            }
        }
        HashMap response = this.comparator(icdList);
        IcdVersion oldVersion = readCurrent();
        IcdVersion medVersion = new IcdVersion();
        versionRepo.save(medVersion);
        medVersion.setTitle(version);
        medVersion.setCurrent(Boolean.TRUE);
        ArrayList<Icd> list = new ArrayList<>();
        icdList.stream().forEach((icd) -> {
            Icd icd2 = repo.save(icd);
            list.add(icd2);

        });
        medVersion.setListIcd(list);
        versionRepo.save(medVersion);
        if (oldVersion == null) {
        } else {
            oldVersion.setCurrent(Boolean.FALSE);
            versionRepo.save(oldVersion);
        }
        return response;
    }

    /**
     * compare two medicament lists 
     * @param list1
     * @return 
     */
    public HashMap comparator(List<Icd> list1) {
        IcdVersion current = this.readCurrent();
        if (current != null) {
            List<Icd> list2;
            list2 = current.getListIcd();
            if (list2 != null) {
                List<Icd> cp1 = new ArrayList(list1);
                List<Icd> cp2 = new ArrayList(list2);
                List<Icd> diagnose = new ArrayList();
                List<Icd> type = new ArrayList();
                for (Icd icd : list2) {
                    list1.stream().filter((icd2) -> (icd.getCode().equals(icd2.getCode()))).map((icd2) -> {
                        cp1.remove(icd2);
                        return icd2;
                    }).map((icd2) -> {
                        cp2.remove(icd);
                        if (!icd.getDiagnose().equals(icd2.getDiagnose())) {
                            diagnose.add(icd);
                        }
                        return icd2;
                    }).filter((icd2) -> (!icd.getType().equals(icd2.getType()))).forEach((_item) -> {
                    type.add(_item);
                });
                }
                HashMap result = new HashMap();
                result.put("new", cp1);
                result.put("deleted", cp2);
                result.put("diagnose", diagnose);
                result.put("type", type);
                return result;
            }
        } else {
            HashMap result = new HashMap();
            result.put("new", list1);
            result.put("deleted", new ArrayList<>());
            return result;
        }
        return null;

    }

    /**
     *
     * @param file
     * @return
     * @throws Throwable
     */
    public String transferToFile(MultipartFile file) throws Throwable {
        String filePath2 = Thread.currentThread().getContextClassLoader().getResource("icd") + "\\" + file.getOriginalFilename();
        String filePath = filePath2.substring(6);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)))) {
                    stream.write(bytes);
                }
                return filePath;
            } catch (Exception e) {
                System.out.println("You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            System.out.println("You failed to upload " + file.getOriginalFilename() + " because the file was empty.");
        }
        return null;
    }

    /**
     * read all existing medicament's verion
     * @return
     */
    @Override
    public List<IcdVersion> readVersions() {
        Iterable<IcdVersion> versions = this.versionRepo.findAll();
        return Lists.newArrayList(versions);
    }

    /**
     * read all medicaments of one version 
     * @param title
     * @return
     */
    @Override
    public List<Icd> readVersionIcd(String title) {
        Iterable<Icd> list = this.versionRepo.findByTitle(title).getListIcd();
        return Lists.newArrayList(list);
    }

    /**
     * search the krankheit or the prozedur that are used one medicament
     * @param icdList
     * @return
     */
    @Override
    public List searchUsedIcd(List<Icd> icdList) {
        List<IcdUsed> result = new ArrayList();
        icdList.stream().forEach((Icd icd) -> {
            List<Krankheit> krankheits = this.krankheitService.readIcdKrankheit(icd.getCode());
            List<Prozedur> prozedurs = this.prozedurService.readIcdProzedur(icd.getCode());
            if (!krankheits.isEmpty() || !prozedurs.isEmpty()) {
                IcdUsed icdStandardList = new IcdUsed();
                icdStandardList.setKrankheits(krankheits);
                icdStandardList.setProzedurs(prozedurs);
                icdStandardList.setIcd(icd);
                result.add(icdStandardList);
            }
        });

        return result;
    }


}