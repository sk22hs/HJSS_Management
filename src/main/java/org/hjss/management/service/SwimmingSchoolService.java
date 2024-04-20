package org.hjss.management.service;

import org.hjss.management.constants.BookingStatus;
import org.hjss.management.constants.Rating;
import org.hjss.management.exception.*;
import org.hjss.management.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwimmingSchoolService {

    /**
     * This is a method to book the lesson
     *
     * @param swimmingLessonToBook
     * @param learner
     * @return
     * @throws InvalidLesson
     * @throws InvalidBooking
     * @throws NoSlotsAvailable
     * @throws AlreadyRegistered
     */
    public String bookLesson(SwimmingLesson swimmingLessonToBook, Learner learner, Map<String, Booking> bookingMap)
            throws InvalidLesson, InvalidBooking, NoSlotsAvailable, AlreadyRegistered {
            if (swimmingLessonToBook != null && learner != null) {
                String bookingId = learner.getId() + getEpochTime(LocalDate.now());
            if (swimmingLessonToBook.getAvailableSlots()<=0) {
                throw new NoSlotsAvailable("No slots available for lesson " + swimmingLessonToBook.getGrade() + " on " + swimmingLessonToBook.getDate());
            }
            // Check if the learner's current grade matches the grade of the lesson or one grade higher
            if (learner.getCurrentGrade().getValue() <= swimmingLessonToBook.getGrade().getValue()
                    && swimmingLessonToBook.getGrade().getValue() <= learner.getCurrentGrade().getValue() + 1 ||
                    learner.getCurrentGrade().getValue() >= swimmingLessonToBook.getGrade().getValue()) {

                // check for already registered case
                List<Booking> leanerBookings = learner.getBookings();
                if(leanerBookings != null) {
                    for (Booking booking : leanerBookings) {
                        if (booking.getLesson().getGrade().equals(swimmingLessonToBook.getGrade())
                                && booking.getLesson().getDate().isEqual(swimmingLessonToBook.getDate())) {
                            throw new AlreadyRegistered(
                                    "You have already registered for the lesson with Id: " + booking.getBookingID());
                        }
                    }
                }

                // Create a new booking
                Booking booking = new Booking();
                booking.setBookingID(bookingId); // Generate a unique booking ID
                booking.setBookingDate(swimmingLessonToBook.getDate());
                booking.setLearner(learner);
                booking.setLesson(swimmingLessonToBook);
                booking.setBookingStatus(BookingStatus.BOOKED.toString());

                // Add the booking to the learner's bookings
                learner.getBookings().add(booking);

                // Update the learner's current grade if they attended a higher grade lesson
                if (swimmingLessonToBook.getGrade().getValue() == learner.getCurrentGrade().getValue() + 1) {
                    learner.setCurrentGrade(swimmingLessonToBook.getGrade());
                }

                // Update the booking in the SwimmingSchool's bookings map
                bookingMap.put(bookingId, booking);

                swimmingLessonToBook.setAvailableSlots(swimmingLessonToBook.getAvailableSlots()-1);

                System.out.println("****** You have successfully registered the lesson Grade: " + swimmingLessonToBook.getGrade() + " on " + swimmingLessonToBook.getDate() + " with booking Id : " + bookingId + " ********");
                return bookingId;
            } else {
                throw new InvalidBooking("Learner cannot book this lesson. It's either too advanced or not available for their grade.");
            }
        } else {
            throw new InvalidLesson("Lesson or learner not found with given details retry again with valid details.");
        }
    }

    /**
     * Method to mark the booking as Attended
     *
     * @param booking
     * @return
     * @throws InvalidBooking
     */
    public String markBookingAttended(Booking booking) throws InvalidBooking {
        if (booking == null) {
            throw new InvalidBooking("Invalid booking details.");
        }

        if (booking.getBookingStatus().equals(BookingStatus.CANCELLED.name())) {
            throw new InvalidBooking("Lesson is cancelled and cannot be changed");
        }

        if (booking.getLesson().getDate().isAfter(LocalDate.now())) {
            throw new InvalidBooking("Cannot mark attended as the lesson has not yet started yet");
        }

        booking.setBookingStatus(BookingStatus.ATTENDED.name());
        booking.getLesson().setAvailableSlots(booking.getLesson().getAvailableSlots() + 1);

        return "Learner - " + booking.getLearner().getId() + " has attended the Lesson " + booking.getLesson().getGrade() + " on " + booking.getLesson().getDate();
    }

    /**
     * Method to cancel the booking
     *
     * @param bookedLesson
     * @return
     * @throws InvalidBooking
     * @throws InvalidDate
     */
    public String cancelBooking(Booking bookedLesson) throws InvalidBooking, InvalidDate {

        if (bookedLesson == null) {
            throw new InvalidBooking("Invalid booking details.");
        }

        if (bookedLesson.getBookingStatus().equals(BookingStatus.ATTENDED.name())) {
            throw new InvalidBooking("Invalid booking details. Booking is already attended or changed");
        }
        if (bookedLesson.getBookingDate().isBefore(LocalDate.now())) {
            throw new InvalidDate("Lesson already attended. Cancel Rejected.");
        }

        bookedLesson.setBookingStatus(BookingStatus.CANCELLED.name());
        bookedLesson.getLesson().setAvailableSlots(bookedLesson.getLesson().getAvailableSlots() + 1);

        return "Your booking : " + bookedLesson.getBookingID() + " for lesson " + bookedLesson.getLesson().getGrade() + " on " + bookedLesson.getLesson().getDate() + " has been cancelled successfully.";
    }

    /**
     * Method to give rating to the Lesson
     *
     * @param learner
     * @param booking
     * @param rating
     * @return
     * @throws InvalidLearner
     * @throws InvalidBooking
     * @throws InvalidDate
     * @throws InvalidRating
     */
    public String provideReview(Learner learner, Booking booking, int rating) throws InvalidLearner, InvalidBooking, InvalidDate, InvalidRating {

        if (learner == null) {
            throw new InvalidLearner("Learner does not exist.");
        }

        if (booking == null) {
            throw new InvalidBooking("Please enter correct booking details.");
        }

        String learnerId = learner.getId();
        String bookingId = booking.getBookingID();

        if (!booking.getLearner().getId().equals(learnerId)) {
            throw new InvalidLearner("Learner " + learnerId + " is invalid for booking " + bookingId);
        }

        if (!booking.getBookingStatus().equals(BookingStatus.ATTENDED.name())) {
            throw new InvalidDate("Lesson " + booking.getLesson().getGrade() + " has not been attended by customer " + learnerId);
        }

        if (rating < 1 || rating > 5) {
            throw new InvalidRating("Rating can only be between 1 and 5.");
        }

        LocalDate today = LocalDate.now();
        Rating valueOfRating = Rating.valueOfRating(rating);

        Review review = new Review(valueOfRating, learnerId, booking.getLesson().getGrade().getValue(), booking.getLesson().getDate(), today, null);

        if (booking.getLesson().getReviews() == null) {
            booking.getLesson().setReviews(new ArrayList<>());
        }

        booking.getLesson().getReviews().add(review);
        booking.setReview(review);

        return "Learner " + learnerId + " has rated " + rating + " for lesson " + booking.getLesson().getGrade();
    }

    /**
     * Method to update/change the booking
     *
     * @param booking
     * @param learner
     * @param swimmingLessonToBook
     * @return
     * @throws InvalidLesson
     * @throws InvalidBooking
     * @throws NoSlotsAvailable
     * @throws AlreadyRegistered
     * @throws InvalidDate
     */
    public String changeBooking(Booking booking, Learner learner, SwimmingLesson swimmingLessonToBook) throws InvalidLesson, InvalidBooking, NoSlotsAvailable, AlreadyRegistered, InvalidDate {

        if (booking == null) {
            throw new InvalidBooking("Invalid booking");
        }

        if (booking.getLesson().getDate().isBefore(LocalDate.now())) {
            throw new InvalidDate("Learner - " + learner.getId() + " has already attended the session " + booking.getLesson().getGrade() + " on " + booking.getLesson().getDate() + ". Change not allowed");
        }
        // Check if the learner's current grade matches the grade of the lesson or one grade higher
        if (learner.getCurrentGrade().getValue() <= swimmingLessonToBook.getGrade().getValue()
                && swimmingLessonToBook.getGrade().getValue() <= learner.getCurrentGrade().getValue() + 1
        || learner.getCurrentGrade().getValue() >= swimmingLessonToBook.getGrade().getValue()) {

            if (swimmingLessonToBook.getCapacity() <= 0) {
                throw new NoSlotsAvailable("No slots available for lesson " + swimmingLessonToBook.getGrade() + " on " + swimmingLessonToBook.getDate());
            }

            // Check if the learner is already registered for the new lesson
            for (Booking existingBooking : learner.getBookings()) {
                if (existingBooking.getLesson().getGrade().equals(swimmingLessonToBook.getGrade())
                        && existingBooking.getLesson().getDate().isEqual(swimmingLessonToBook.getDate())) {
                    throw new AlreadyRegistered("You have already registered for the lesson with Id: " + existingBooking.getBookingID());
                }
            }

            // Update the previous lesson's capacity
            booking.getLesson().setAvailableSlots(booking.getLesson().getAvailableSlots() + 1);

            // Update the existing booking with the new lesson details
            booking.setLesson(swimmingLessonToBook);

            // Update the learner's current grade if they attended a higher grade lesson
            if (swimmingLessonToBook.getGrade().getValue() == learner.getCurrentGrade().getValue() + 1) {
                learner.setCurrentGrade(swimmingLessonToBook.getGrade());
            }

            // Update the new lesson's capacity
            swimmingLessonToBook.setAvailableSlots(swimmingLessonToBook.getAvailableSlots() - 1);

            return "Your Booking " + booking.getBookingID() + " has been successfully changed to lesson " + swimmingLessonToBook.getGrade() + " on " + swimmingLessonToBook.getDate();

        } else {
            throw new InvalidBooking("Learner cannot book this lesson. It's either too advanced or not available for their grade.");
        }
    }

    private Long getEpochTime(LocalDate date) {
        return System.currentTimeMillis();
    }
}
