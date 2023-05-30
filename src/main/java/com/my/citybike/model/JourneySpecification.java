package com.my.citybike.model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class JourneySpecification implements Specification<Journey> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;

	public JourneySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }
	
    @Override
    public Predicate toPredicate
      (Root<Journey> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
 
    	if (criteria.getOperation().equalsIgnoreCase("equals")) {
            String[] propertyPath = criteria.getKey().split("\\.");

            if (propertyPath.length == 2 && propertyPath[0].equals("returnStation")) {
                Join<Journey, Station> returnStationJoin = root.join("returnStation");
                return builder.equal(returnStationJoin.get(propertyPath[1]), criteria.getValue());
            } else if (propertyPath.length == 2 && propertyPath[0].equals("departureStation")) {
                Join<Journey, Station> departureStationJoin = root.join("departureStation");
                return builder.equal(departureStationJoin.get(propertyPath[1]), criteria.getValue());
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("lessThan")) {
            return builder.lessThan(
              root.<Integer> get(criteria.getKey()), (int) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("greaterThan")) {
        	return builder.greaterThan(
        			root.<Integer> get(criteria.getKey()), (int) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("equals")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.equal(
                  root.<String>get(criteria.getKey()), criteria.getValue());
            } else {
                return builder.equal(root.<Integer> get(criteria.getKey()), (int) criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("contains")) {
            String[] propertyPath = criteria.getKey().split("\\.");

            if (propertyPath.length == 2 && propertyPath[0].equals("returnStation")) {
                Join<Journey, Station> returnStationJoin = root.join("returnStation");
                return builder.like(returnStationJoin.get(propertyPath[1]), "%" + criteria.getValue() + "%");
            } else if (propertyPath.length == 2 && propertyPath[0].equals("departureStation")) {
                Join<Journey, Station> departureStationJoin = root.join("departureStation");
                return builder.like(departureStationJoin.get(propertyPath[1]), "%" + criteria.getValue() + "%");
            } else {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            }
        }
        return null;
    }
}
