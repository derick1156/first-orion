package com.firstorion.project.DerickMaloneVertxApp.repository;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;

import java.util.List;

public interface BandsRepository {

	Band getBandById(Integer id);

	List<Band> getAllBands();

	Band updateBand(Band band);

	Band insertBand(Band band);

	void deleteBand(Integer id);
}
