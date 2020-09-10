package com.firstorion.project.DerickMaloneVertxApp.repository;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.RedisKeys.BAND_KEY;

public class BandsRepositoryImpl implements BandsRepository {
	private static RedissonClient redissonClient;

	public BandsRepositoryImpl(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public Band getBandById(Integer id) {
		RMap<Integer,Band> bandsMap = redissonClient.getMap(BAND_KEY);
		return bandsMap.get(id);
	}

	@Override
	public Band getBandByName(String name) {
		//todo dm fill this in
		return null;
	}

	@Override
	public List<Band> getAllBands() {
		//todo dm sort by id or name - https://www.java67.com/2019/06/top-5-sorting-examples-of-comparator-and-comparable-in-java.html
		RMap<Integer,Band> bandsMap = redissonClient.getMap(BAND_KEY);
		List<Band> bands = (List)bandsMap.readAllValues();
//		Collections.sort(bands);
		return bands;
	}

	@Override
	public Band updateBand(Band band) {
		RMap<Integer,Band> bandsMap = redissonClient.getMap(BAND_KEY);
		return bandsMap.put(band.getId(), band);
	}

	@Override
	public Band insertBand(Band band) {
		RMap<Integer,Band> bandsMap = redissonClient.getMap(BAND_KEY);
		return bandsMap.putIfAbsent(band.getId(), band);
	}

	@Override
	public void deleteBand(Integer id) {
		RMap<Integer,Band> bandsMap = redissonClient.getMap(BAND_KEY);
		bandsMap.remove(Integer.valueOf(id));
	}

	public static void seedBandRepository(){
		Band theMoodyBlues = new Band("The Moody Blues");
		Band theBeatles = new Band("The Beatles");
		Band thePolice = new Band("The Police");

		redissonClient.getMap(BAND_KEY).clear();

		RMap<Integer, Band> bandsMap = redissonClient.getMap(BAND_KEY);

		bandsMap.put(theMoodyBlues.getId(),theMoodyBlues);
		bandsMap.put(theBeatles.getId(),theBeatles);
		bandsMap.put(thePolice.getId(),thePolice);
	}
}
