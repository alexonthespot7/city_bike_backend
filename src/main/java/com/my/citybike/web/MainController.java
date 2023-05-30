package com.my.citybike.web;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.citybike.MyUser;
import com.my.citybike.forms.JourneyForm;
import com.my.citybike.forms.StationStatsForm;
import com.my.citybike.model.Journey;
import com.my.citybike.model.JourneyRepository;
import com.my.citybike.model.JourneySpecification;
import com.my.citybike.model.SearchCriteria;
import com.my.citybike.model.Station;
import com.my.citybike.model.StationRepository;
import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;

@RestController
public class MainController {
	@Autowired
	UserRepository urepository;

	@Autowired
	StationRepository srepository;

	@Autowired
	JourneyRepository jrepository;
	
	@RequestMapping("/removeDuplicates")
    public ResponseEntity<String> removeDuplicates() {
		jrepository.deleteDuplicates();
        return ResponseEntity.ok("Duplicate records have been removed.");
    }

	@RequestMapping("/stations/{stationid}/stats")
	public @ResponseBody StationStatsForm stationStatsRest(@PathVariable("stationid") Long stationId) {
		Optional<Station> optStation = srepository.findById(stationId);
		if (optStation.isPresent()) {
			Optional<Double> avgStartingFromOpt = jrepository.avgDistanceByDepartureStationId(stationId);
			Optional<Double> avgEndingAtOpt = jrepository.avgDistanceByReturnStationId(stationId);
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

			Long avgStartingFrom;
			Long avgEndingAt;

			if (avgStartingFromOpt.isPresent()) {
				avgStartingFrom = Math.round(avgStartingFromOpt.get());
			} else {
				avgStartingFrom = new Long(0);
			}
			if (avgEndingAtOpt.isPresent()) {
				avgEndingAt = Math.round(avgEndingAtOpt.get());
			} else {
				avgEndingAt = new Long(0);
			}

			return new StationStatsForm(avgStartingFrom, avgEndingAt, topPopReturnStations, topPopDepartureStations,
					optStation.get().getJourneysFrom().size(), optStation.get().getJourneysTo().size());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/stations/{stationid}/stats", method = RequestMethod.POST)
	public @ResponseBody StationStatsForm stationStatsRestByMonth(@PathVariable("stationid") Long stationId,
			@RequestBody String dateString) {
		Optional<Station> optStation = srepository.findById(stationId);
		if (optStation.isPresent() && dateString.contains("T")) {
			dateString = dateString.split("T")[0];
			Date firstDay = Date.valueOf(dateString);
			Date secondDay = Date.valueOf(dateString.split("-")[0] + "-"
					+ String.valueOf(Integer.parseInt(dateString.split("-")[1]) + 2) + "-01");
			Optional<Double> avgStartingFromOpt = jrepository.avgDistanceByDepartureStationIdAndMonth(stationId,
					firstDay, secondDay);
			Optional<Double> avgEndingAtOpt = jrepository.avgDistanceByReturnStationIdAndMonth(stationId, firstDay,
					secondDay);
			List<Long> topPopularReturnSationsIds = jrepository.topPopReturnByDepartureStationIdAndMonth(stationId,
					firstDay, secondDay);
			List<Long> topPopularDepartureStationsIds = jrepository.topPopDepartureByReturnStationIdAndMonth(stationId,
					firstDay, secondDay);
			List<Station> topPopReturnStations = new ArrayList<Station>();
			List<Station> topPopDepartureStations = new ArrayList<Station>();

			for (Long id : topPopularReturnSationsIds) {
				topPopReturnStations.add(srepository.findById(id).get());
			}
			for (Long id : topPopularDepartureStationsIds) {
				topPopDepartureStations.add(srepository.findById(id).get());
			}

			Long avgStartingFrom;
			Long avgEndingAt;

			if (avgStartingFromOpt.isPresent()) {
				avgStartingFrom = Math.round(avgStartingFromOpt.get());
			} else {
				avgStartingFrom = new Long(0);
			}
			if (avgEndingAtOpt.isPresent()) {
				avgEndingAt = Math.round(avgEndingAtOpt.get());
			} else {
				avgEndingAt = new Long(0);
			}

			return new StationStatsForm(avgStartingFrom, avgEndingAt, topPopReturnStations, topPopDepartureStations,
					jrepository.findJourneysFromByDepartureStationIdAndMonth(stationId, firstDay, secondDay).size(),
					jrepository.findJourneysToByReturnStationIdAndMonth(stationId, firstDay, secondDay).size());
		} else {
			return null;
		}
	}

	@RequestMapping("/stations")
	public @ResponseBody List<Station> stationsListRest() {
		return (List<Station>) srepository.findAll();
	}

	@RequestMapping("/journeys")
	public @ResponseBody Page<Journey> journeyssListRest(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "") String order,
			@RequestParam(defaultValue = "") String depValue, @RequestParam(defaultValue = "") String depOperation,
			@RequestParam(defaultValue = "") String retValue, @RequestParam(defaultValue = "") String retOperation,
			@RequestParam(defaultValue = "0") int distValue, @RequestParam(defaultValue = "") String distOperation,
			@RequestParam(defaultValue = "0") int durValue, @RequestParam(defaultValue = "") String durOperation) {
		Pageable pageable;

		if (!sort.equals("")) {
			Sort sortParams = Sort.by(sort);
			if (!order.equals("asc")) {
				sortParams = sortParams.descending();
			}

			pageable = PageRequest.of(page, 100, sortParams);
		} else {
			pageable = PageRequest.of(page, 100);
		}

		boolean flag = true;
		JourneySpecification spec = null;

		if (!depValue.equals("")) {
			spec = new JourneySpecification(new SearchCriteria("departureStation.name", depOperation, depValue));
			flag = false;
		}

		if (!retValue.equals("")) {
			if (flag) {
				spec = new JourneySpecification(new SearchCriteria("returnStation.name", retOperation, retValue));
				flag = false;
			} else {
				spec.and(new JourneySpecification(new SearchCriteria("returnStation.name", retOperation, retValue)));
			}
		}

		if (!distOperation.equals("")) {
			if (flag) {
				spec = new JourneySpecification(new SearchCriteria("distance", distOperation, distValue));
				flag = false;
			} else {
				spec.and(new JourneySpecification(new SearchCriteria("distance", distOperation, distValue)));
			}
		}

		if (!durOperation.equals("")) {
			if (flag) {
				spec = new JourneySpecification(new SearchCriteria("duration", durOperation, durValue));
				flag = false;
			} else {
				spec.and(new JourneySpecification(new SearchCriteria("duration", durOperation, durValue)));
			}
		}

		if (flag) {
			return jrepository.findAll(pageable);
		} else {
			return jrepository.findAll(spec, pageable);
		}

	}

	@RequestMapping("/stations/{stationid}")
	public @ResponseBody Station stationByIdRest(@PathVariable("stationid") Long stationId) {
		Optional<Station> optStation = srepository.findById(stationId);
		if (optStation.isPresent()) {
			Station station = optStation.get();
			return station;
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
						return new ResponseEntity<>("The dataset is too large for user with this authority",
								HttpStatus.CONFLICT);
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
							return new ResponseEntity<>("Each instance was added to database successfully",
									HttpStatus.OK);
						} else {
							return new ResponseEntity<>("For some journeys there were no existing stations",
									HttpStatus.ACCEPTED);
						}
					} else if (journeys.size() <= 500) {
						flag = saveJourneys(journeys);

						if (flag) {
							return new ResponseEntity<>("Each instance was added to database successfully",
									HttpStatus.OK);
						} else {
							return new ResponseEntity<>("For some journeys there were no existing stations",
									HttpStatus.ACCEPTED);
						}
					} else {
						return new ResponseEntity<>("The dataset is too large for user with this authority",
								HttpStatus.CONFLICT);
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

				List<Journey> optJourneys = jrepository.findByFields(Date.valueOf(dateDep), Date.valueOf(dateRet),
						journeys.get(i).getDistance(), journeys.get(i).getDuration(), optStationReturn.get().getId(),
						optStationDep.get().getId());
				if (optJourneys.size() == 0) {

					journey = new Journey(Date.valueOf(dateDep), Date.valueOf(dateRet), journeys.get(i).getDistance(),
							journeys.get(i).getDuration(), optStationReturn.get(), optStationDep.get());
					jrepository.save(journey);
				}
			} else {
				flag = false;
			}
		}

		return flag;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/addstation", method = RequestMethod.POST)
	public ResponseEntity<?> addStation(@RequestBody Station station) {
		Optional<Station> optStation = srepository.findById(station.getId());
		
		if (optStation.isPresent()) {
			return new ResponseEntity<>("The id you are trying to pass is already in use", HttpStatus.BAD_REQUEST);
		} else {
			srepository.save(station);
			return new ResponseEntity<>("The station was added successfully", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/addjourney", method = RequestMethod.POST)
	public ResponseEntity<?> addStation(@RequestBody JourneyForm journeyForm) {
		Optional<Station> returnStation = srepository.findById(journeyForm.getReturnStationId());
		Optional<Station> departureStation = srepository.findById(journeyForm.getDepartureStationId());
		
		if (returnStation.isPresent() && departureStation.isPresent()) {
			Journey journey = new Journey(Date.valueOf(journeyForm.getDepartureTime()), Date.valueOf(journeyForm.getReturnTime()), journeyForm.getDistance(), journeyForm.getDuration(), returnStation.get(), departureStation.get());
			jrepository.save(journey);
			return new ResponseEntity<>("The station was added successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("The stations you are trying to claim as return and departure for your journey are not in database", HttpStatus.BAD_REQUEST); //400
		}
		
	}
}
