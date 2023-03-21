package com.my.citybike.web;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.citybike.MyUser;
import com.my.citybike.forms.JourneyForm;
import com.my.citybike.forms.StationInfo;
import com.my.citybike.forms.StationStatsForm;
import com.my.citybike.model.Journey;
import com.my.citybike.model.JourneyRepository;
import com.my.citybike.model.Station;
import com.my.citybike.model.StationRepository;
import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;

@RestController
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	UserRepository urepository;
	
	@Autowired
	StationRepository srepository;
	
	@Autowired
	JourneyRepository jrepository;
	
	@RequestMapping("/stations/{stationid}/stats")
	public @ResponseBody StationStatsForm stationStatsRest(@PathVariable("stationid") Long stationId) {
		if (srepository.findById(stationId).isPresent()) {
			Double avgStartingFrom = jrepository.avgDistanceByDepartureStationId(stationId);
			Double avgEndingAt = jrepository.avgDistanceByReturnStationId(stationId);
			List<Long> topPopularReturnSationsIds = jrepository.topPopReturnByDepartureStationId(stationId);
			List<Long> topPopularDepartureStationsIds = jrepository.topPopDepartureByReturnStationId(stationId);
			List<Station> topPopReturnStations = new ArrayList<Station>();
			List<Station> topPopDepartureStations = new ArrayList<Station>();
			for (Long id : topPopularReturnSationsIds) {
				topPopReturnStations.add(srepository.findById(id).get());
			}
			for (Long id : topPopularDepartureStationsIds) {
				topPopDepartureStations.add(srepository.findById(id).get());
			}
			
			return new StationStatsForm(Math.round(avgStartingFrom), Math.round(avgEndingAt), topPopReturnStations, topPopDepartureStations);
		} else {
			return null;
		}
	}
	
	@RequestMapping("/stations")
	public @ResponseBody List<Station> stationsListRest() {
		return (List<Station>) srepository.findAll();
	}
	
	@RequestMapping("/journeys")
	public @ResponseBody List<Journey> journeyssListRest() {
		return (List<Journey>) jrepository.findAll();
	}
	
	@RequestMapping("/stations/{stationid}")
	public @ResponseBody StationInfo stationByIdRest(@PathVariable("stationid") Long stationId) {
		Optional<Station> optStation = srepository.findById(stationId);
		if (optStation.isPresent()) {
			Station station = optStation.get();
			return new StationInfo(station.getId(), station.getJourneysFrom().size(), station.getJourneysTo().size(), station.getAddress(), station.getLatitude(), station.getLongitude(), station.getName());
		} else {
			return null;
		}
	}
		
	@RequestMapping(value = "/sendstations", method = RequestMethod.POST)
	public ResponseEntity<?> importStations(@RequestBody List<Station> stations, Authentication auth) {
		if (auth != null) {
			if (auth.getPrincipal().getClass().toString().equals("class com.my.citybike.MyUser")) {
				MyUser myUser = (MyUser) auth.getPrincipal();
				Optional<User> optUser = urepository.findByUsername(myUser.getUsername());
				if (optUser.isPresent()) {
					if (optUser.get().getRole().equals("ADMIN")) {
						srepository.saveAll(stations);
						return new ResponseEntity<>("Stations were added to the database", HttpStatus.OK);
					} else if (stations.size() <= 500) {
						srepository.saveAll(stations);
						return new ResponseEntity<>("Stations were added to the database", HttpStatus.OK);
					} else {
						return new ResponseEntity<>("The dataset is too large for user with this authority", HttpStatus.CONFLICT);
					}
				} else {
					return new ResponseEntity<>("User cannot be find", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<>("Unauthorized request", HttpStatus.UNAUTHORIZED);
			}
		} else if (stations.size() <= 500) {
			srepository.saveAll(stations);
			return new ResponseEntity<>("Stations were added to the database", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("The dataset is too large for user with this authority", HttpStatus.CONFLICT);
		}
	}
	
	@RequestMapping(value = "/sendjourneys", method = RequestMethod.POST)
	public ResponseEntity<?> importJourneys(@RequestBody List<JourneyForm> journeys, Authentication auth) {
		boolean flag = true;
				
		if (auth != null) {
			if (auth.getPrincipal().getClass().toString().equals("class com.my.citybike.MyUser")) {
				MyUser myUser = (MyUser) auth.getPrincipal();
				Optional<User> optUser = urepository.findByUsername(myUser.getUsername());
				if (optUser.isPresent()) {
					if (optUser.get().getRole().equals("ADMIN")) {
						flag = saveJourneys(journeys);
						
						if (flag) {
							return new ResponseEntity<>("Each instance was added to database successfully", HttpStatus.OK);
						} else {
							return new ResponseEntity<>("For some journeys there were no existing stations", HttpStatus.ACCEPTED);
						}
					} else if (journeys.size() <= 500) {
						flag = saveJourneys(journeys);

						if (flag) {
							return new ResponseEntity<>("Each instance was added to database successfully", HttpStatus.OK);
						} else {
							return new ResponseEntity<>("For some journeys there were no existing stations", HttpStatus.ACCEPTED);
						}
					} else {
						return new ResponseEntity<>("The dataset is too large for user with this authority", HttpStatus.CONFLICT);
					}
				} else {
					return new ResponseEntity<>("User cannot be find", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<>("Unauthorized request", HttpStatus.UNAUTHORIZED);
			}
		} else if (journeys.size() <= 500) {
			flag = saveJourneys(journeys);

			if (flag) {
				return new ResponseEntity<>("Each instance was added to database successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("For some journeys there were no existing stations", HttpStatus.ACCEPTED);
			}
		} else {
			return new ResponseEntity<>("The dataset is too large for user with this authority", HttpStatus.CONFLICT);
		}
	}
	
	private boolean saveJourneys(List<JourneyForm> journeys) {
		Optional<Station> optStationReturn;
		Optional<Station> optStationDep;
		String[] dateDepart;
		String[] dateReturn;
		String dateDep;
		String dateRet;
		Journey journey;
		boolean flag = true;
		
		for (int i = 0; i < journeys.size(); i++) {
			optStationReturn = srepository.findById(journeys.get(i).getReturnStationId());
			optStationDep = srepository.findById(journeys.get(i).getDepartureStationId());
			
			if (optStationDep.isPresent() && optStationReturn.isPresent()) {
				if (journeys.get(i).getDepartureTime().contains("/")) {
					dateDepart = journeys.get(i).getDepartureTime().split("/");
					dateDep = dateDepart[2] + "-" + dateDepart[0] + "-" + dateDepart[1];
				} else {
					dateDep = journeys.get(i).getDepartureTime().split("T")[0];
				}
				
				if (journeys.get(i).getReturnTime().contains("/")) {
					dateReturn = journeys.get(i).getReturnTime().split("/");
					dateRet = dateReturn[2] + "-" + dateReturn[0] + "-" + dateReturn[1];
				} else {
					dateRet = journeys.get(i).getReturnTime().split("T")[0];
				}
				
				journey = new Journey(Date.valueOf(dateDep), Date.valueOf(dateRet), journeys.get(i).getDistance(), journeys.get(i).getDuration(), optStationReturn.get(), optStationDep.get());
				jrepository.save(journey);
			} else {
				flag = false;
			}
		}
		
		return flag;
	}
}
