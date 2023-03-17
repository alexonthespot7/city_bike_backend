package com.my.citybike.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.citybike.MyUser;
import com.my.citybike.forms.JourneyForm;
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
	
	@RequestMapping("/stations")
	public @ResponseBody List<Station> stationsListRest() {
		return (List<Station>) srepository.findAll();
	}
	
	@RequestMapping("/journeys")
	public @ResponseBody List<Journey> journeyssListRest() {
		return (List<Journey>) jrepository.findAll();
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
		Optional<Station> optStationReturn;
		Optional<Station> optStationDep;
		Journey journey;
		boolean flag = true;
		
		if (auth != null) {
			if (auth.getPrincipal().getClass().toString().equals("class com.my.citybike.MyUser")) {
				MyUser myUser = (MyUser) auth.getPrincipal();
				Optional<User> optUser = urepository.findByUsername(myUser.getUsername());
				if (optUser.isPresent()) {
					if (optUser.get().getRole().equals("ADMIN")) {
						for (int i = 0; i < journeys.size(); i++) {
							optStationReturn = srepository.findById(journeys.get(i).getReturnStationId());
							optStationDep = srepository.findById(journeys.get(i).getDepartureStationId());
							
							if (optStationDep.isPresent() && optStationReturn.isPresent()) {
								journey = new Journey(journeys.get(i).getDepartureTime(), journeys.get(i).getReturnTime(), journeys.get(i).getDistance(), journeys.get(i).getDuration(), optStationReturn.get(), optStationDep.get());
								jrepository.save(journey);
							} else {
								flag = false;
							}
						}
						if (flag) {
							return new ResponseEntity<>("Each instance was added to database successfully", HttpStatus.OK);
						} else {
							return new ResponseEntity<>("For some journeys there were no existing stations", HttpStatus.ACCEPTED);
						}
					} else if (journeys.size() <= 500) {
						for (int i = 0; i < journeys.size(); i++) {
							optStationReturn = srepository.findById(journeys.get(i).getReturnStationId());
							optStationDep = srepository.findById(journeys.get(i).getDepartureStationId());
							
							if (optStationDep.isPresent() && optStationReturn.isPresent()) {
								journey = new Journey(journeys.get(i).getDepartureTime(), journeys.get(i).getReturnTime(), journeys.get(i).getDistance(), journeys.get(i).getDuration(), optStationReturn.get(), optStationDep.get());
								jrepository.save(journey);
							} else {
								flag = false;
							}
						}
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
			for (int i = 0; i < journeys.size(); i++) {
				optStationReturn = srepository.findById(journeys.get(i).getReturnStationId());
				optStationDep = srepository.findById(journeys.get(i).getDepartureStationId());
				
				if (optStationDep.isPresent() && optStationReturn.isPresent()) {
					journey = new Journey(journeys.get(i).getDepartureTime(), journeys.get(i).getReturnTime(), journeys.get(i).getDistance(), journeys.get(i).getDuration(), optStationReturn.get(), optStationDep.get());
					jrepository.save(journey);
				} else {
					flag = false;
				}
			}
			if (flag) {
				return new ResponseEntity<>("Each instance was added to database successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("For some journeys there were no existing stations", HttpStatus.ACCEPTED);
			}
		} else {
			return new ResponseEntity<>("The dataset is too large for user with this authority", HttpStatus.CONFLICT);
		}
	}
}
