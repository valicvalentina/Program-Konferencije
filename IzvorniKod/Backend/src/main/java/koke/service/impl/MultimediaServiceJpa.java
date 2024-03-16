package koke.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import koke.dto.MultimediaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import koke.dao.MultimediaRepository;
import koke.domain.Conference;
import koke.domain.Multimedia;
import koke.service.ConferenceService;
import koke.service.MultimediaService;

@Service
public class MultimediaServiceJpa implements MultimediaService {


    @Autowired
    MultimediaRepository multimediaRepo;

    @Autowired
    ConferenceService conferenceService;

    @Override
    public List<Multimedia> getAllImages(Long idConference) {
        return multimediaRepo.findAll().stream()
                .filter(a -> (a.getDate() != null && a.getConference().getIdConference() == idConference))
                .collect(Collectors.toList());
    }

    @Override
    public Multimedia getMultimediaById(Long idMultimedia) {
        return multimediaRepo.findById(idMultimedia).orElse(null);
    }

    @Override
    public List<Multimedia> getAllVideo(Long idConference) {
        return multimediaRepo.findAll().stream()
                .filter(a -> (a.getDate() == null && a.getConference().getIdConference() == idConference))
                .collect(Collectors.toList());
    }

//    @Override
//    public boolean downloadImage(Long idMultimedia, PathDTO dir) {
//        File f = new File(dir.getPath());
//        if (!f.isDirectory() || !f.exists())
//            return false;
//        try {
//            File input = new File(multimediaRepo.findById(idMultimedia).get().getUrl());
//            BufferedImage bImage = ImageIO.read(input);
//            // Process image
//
//            if (dir.getPath().toString().contains("\\")) {
//                ImageIO.write(bImage, "jpg", new File(dir.getPath().toString() + "\\" + idMultimedia + ".jpg"));
//                return true;
//            } else if (dir.toString().contains("/")) {
//                ImageIO.write(bImage, "jpg", new File(dir.getPath().toString() + "/" + idMultimedia + ".jpg"));
//                return true;
//            }
//            return false;
//        } catch (IOException e) {
//            return false;
//        }
//    }

    @Override
    public Multimedia addMultimedia(String url, Date date, Long idConference) {
        Conference conference = conferenceService.getByConferenceId(idConference);
        return multimediaRepo.save(new Multimedia(conference, url, date));
    }

    @Override
    public List<MultimediaDTO> getAllImagesForDate(Date date, Long idConference) {
        List<MultimediaDTO> list = new ArrayList<>();

        for (Multimedia multimedia : getAllImages(idConference)) {
            if (multimedia.getDate().getYear() == date.getYear()
                    && multimedia.getDate().getMonth() == date.getMonth()
                    && multimedia.getDate().getDate() == date.getDate()) list.add(MultimediaDTO.toDTO(multimedia));
        }
        return list;
    }
}
