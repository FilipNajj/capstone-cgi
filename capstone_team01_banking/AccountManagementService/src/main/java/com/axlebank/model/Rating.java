package com.axlebank.model;

import java.time.LocalDate;

public class Rating {
	private LocalDate date = LocalDate.now();;
	private String comment;
	private double ratingScore;
	
	public Rating () {}

	public Rating(String comment, double ratingScore) {
		this.comment = comment;
		this.ratingScore = ratingScore;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = LocalDate.now();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(double ratingScore) {
		this.ratingScore = ratingScore;
	}

	@Override
	public String toString() {
		return "Rating [date=" + date + ", comment=" + comment + ", ratingScore=" + ratingScore + "]";
	}

	
}
