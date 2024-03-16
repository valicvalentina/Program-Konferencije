package koke.service;

import java.util.Date;
import java.util.List;

import koke.domain.Multimedia;
import koke.dto.MultimediaDTO;

public interface MultimediaService {

	List<Multimedia> getAllImages(Long idConference);

	Multimedia getMultimediaById(Long idMultimedia);

	List<Multimedia> getAllVideo(Long idConference);

//	boolean downloadImage(Long idMultimedia, PathDTO dir);

	Multimedia addMultimedia(String string, Date date, Long idConference);

	List<MultimediaDTO> getAllImagesForDate(Date date, Long idConference);
}
