package org.hjss.management.models;

import org.hjss.management.constants.BookingStatus;
import org.hjss.management.constants.Gender;
import org.hjss.management.constants.Grade;
import org.hjss.management.constants.Rating;
import org.hjss.management.exception.InvalidBooking;
import org.hjss.management.service.SwimmingSchoolService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class SwimmingSchool {
    private final List<SwimmingLesson> timetable = new ArrayList<>();
    private final List<Coach> coaches = new ArrayList<>();
    private final List<Learner> learners = new ArrayList<>();
    private final Map<String, List<SwimmingLesson>> lessonMap = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();
    private static SwimmingSchool swimmingSchool = null;
    SwimmingSchoolService swimmingSchoolService = new SwimmingSchoolService();

    /**
     * constructor which when initialises the default data
     */
    public SwimmingSchool() {
        generateTimetable(4);
        generateLearners();
        generateBookings();
        generateReviews();
    }

    private void generateReviews() {
        Review review1 = new Review(Rating.SATISFIED, "L1", 2, LocalDate.of(2024, 3, 18), LocalDate.of(2024, 3, 20), "Great lesson!");
        Review review2 = new Review(Rating.VERY_SATISFIED, "L2", 3, LocalDate.of(2024, 3, 18), LocalDate.of(2024, 3, 20), "Fantastic coaching!");
        Review review3 = new Review(Rating.OK, "L3", 4, LocalDate.of(2024, 3, 18), LocalDate.of(2024, 3, 20), "Enjoyed the session!");


        Booking booking1 = bookings.get("B1L1");
        booking1.setBookingStatus(BookingStatus.ATTENDED.name());
        SwimmingLesson swimmingLesson1 = booking1.getLesson();
        swimmingLesson1.setAvailableSlots(swimmingLesson1.getAvailableSlots() + 1);
        swimmingLesson1.getReviews().add(review1);
        booking1.setReview(review1);

        Booking booking2 = bookings.get("B2L2");
        booking2.setBookingStatus(BookingStatus.ATTENDED.name());
        SwimmingLesson swimmingLesson2 = booking2.getLesson();
        swimmingLesson2.setAvailableSlots(swimmingLesson2.getAvailableSlots() + 1);
        swimmingLesson2.getReviews().add(review2);
        booking2.setReview(review2);

        Booking booking3 = bookings.get("B3L3");
        booking3.setBookingStatus(BookingStatus.ATTENDED.name());
        SwimmingLesson swimmingLesson3 = booking3.getLesson();
        swimmingLesson3.setAvailableSlots(swimmingLesson3.getAvailableSlots() + 1);
        swimmingLesson3.getReviews().add(review3);
        booking3.setReview(review3);
    }


    public List<SwimmingLesson> generateTimetable(int numWeeks) {

        Coach c1 = new Coach("Sumanth", null);
        Coach c2 = new Coach("John", null);
        Coach c3 = new Coach("Helen", null);
        Coach c4 = new Coach("Alice", null);
        coaches.add(c1);
        coaches.add(c2);
        coaches.add(c3);
        coaches.add(c4);

        LocalDate currentDate = LocalDate.now();

        LocalDate previousMonday = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate previousWednesday = currentDate.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY));
        LocalDate previousFriday = currentDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        LocalDate previousSaturday = currentDate.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));


        timetable.add(new SwimmingLesson(Grade.GRADE_1, previousMonday, LocalTime.of(16, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_2, previousMonday, LocalTime.of(17, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_3, previousMonday, LocalTime.of(18, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_1, previousWednesday, LocalTime.of(16, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_2, previousWednesday, LocalTime.of(17, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_3, previousWednesday, LocalTime.of(18, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_1, previousFriday, LocalTime.of(16, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_2, previousFriday, LocalTime.of(17, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_3, previousFriday, LocalTime.of(18, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_1, previousSaturday, LocalTime.of(14, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
        timetable.add(new SwimmingLesson(Grade.GRADE_2, previousSaturday, LocalTime.of(15, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));

        for (int i = 0; i < numWeeks; i++) {
            LocalDate nextMonday = currentDate.plusWeeks(i).with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            LocalDate nextWednesday = currentDate.plusWeeks(i).with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
            LocalDate nextFriday = currentDate.plusWeeks(i).with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            LocalDate nextSaturday = currentDate.plusWeeks(i).with(TemporalAdjusters.next(DayOfWeek.SATURDAY));

            timetable.add(new SwimmingLesson(Grade.GRADE_1, nextMonday, LocalTime.of(16, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_2, nextMonday, LocalTime.of(17, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_3, nextMonday, LocalTime.of(18, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_4, nextWednesday, LocalTime.of(16, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_5, nextWednesday, LocalTime.of(17, 0), c1, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_1, nextWednesday, LocalTime.of(18, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_2, nextFriday, LocalTime.of(16, 0), c4, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_3, nextFriday, LocalTime.of(17, 0), c3, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_4, nextFriday, LocalTime.of(18, 0), c4, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_5, nextSaturday, LocalTime.of(14, 0), c2, new ArrayList<>(), 4, new ArrayList<>(), 4));
            timetable.add(new SwimmingLesson(Grade.GRADE_1, nextSaturday, LocalTime.of(15, 0), c4, new ArrayList<>(), 4, new ArrayList<>(), 4));
        }

        return timetable;
    }


    private void generateLearners() {
        Learner l1 = new Learner("L1", "John Doe", Gender.MALE, 4, "Emergency Contact 1", Grade.GRADE_1, new ArrayList<>());
        Learner l2 = new Learner("L2", "Jane Smith", Gender.MALE, 5, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l3 = new Learner("L3", "Emily Johnson", Gender.FEMALE, 6, "Emergency Contact 2", Grade.GRADE_2, new ArrayList<>());
        Learner l4 = new Learner("L4", "Michael Williams", Gender.MALE, 7, "Emergency Contact 2", Grade.GRADE_4, new ArrayList<>());
        Learner l5 = new Learner("L5", "Sarah Brown", Gender.FEMALE, 8, "Emergency Contact 2", Grade.GRADE_5, new ArrayList<>());
        Learner l6 = new Learner("L6", "David Jones", Gender.MALE, 9, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l7 = new Learner("L7", "Jessica Davis", Gender.FEMALE, 10, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l8 = new Learner("L8", "Daniel Miller", Gender.MALE, 11, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l9 = new Learner("L9", "Amanda Wilson", Gender.FEMALE, 10, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l10 = new Learner("L10", "James Taylor", Gender.MALE, 9, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l11 = new Learner("L11", "Ashley Anderson", Gender.MALE, 8, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l12 = new Learner("L12", "Robert Martinez", Gender.MALE, 7, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l13 = new Learner("L13", "Jennifer Lee", Gender.FEMALE, 6, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l14 = new Learner("L14", "William Clark", Gender.MALE, 5, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        Learner l15 = new Learner("L15", "Christopher Hall", Gender.FEMALE, 4, "Emergency Contact 2", Grade.GRADE_1, new ArrayList<>());
        learners.add(l1);
        learners.add(l2);
        learners.add(l3);
        learners.add(l4);
        learners.add(l5);
        learners.add(l6);
        learners.add(l7);
        learners.add(l8);
        learners.add(l9);
        learners.add(l10);
        learners.add(l11);
        learners.add(l12);
        learners.add(l13);
        learners.add(l14);
        learners.add(l15);
    }

    public void generateBookings() {
        // Clear existing bookings to avoid duplication

        // Choose specific lessons from the timetable for bookings
        SwimmingLesson swimmingLesson1 = timetable.get(0);
        SwimmingLesson swimmingLesson2 = timetable.get(1);
        SwimmingLesson swimmingLesson3 = timetable.get(2);
        SwimmingLesson swimmingLesson4 = timetable.get(3);
        SwimmingLesson swimmingLesson5 = timetable.get(4);

        Learner learner1 = getLearnerByLearnerId("L1");
        Learner learner2 = getLearnerByLearnerId("L2");
        Learner learner3 = getLearnerByLearnerId("L3");
        Learner learner4 = getLearnerByLearnerId("L4");
        Learner learner5 = getLearnerByLearnerId("L5");

        Booking b1 = new Booking("B1L1", LocalDate.now(), learner1, swimmingLesson1, BookingStatus.BOOKED.name(), null);
        Booking b2 = new Booking("B2L2", LocalDate.now(), learner2, swimmingLesson2, BookingStatus.BOOKED.name(), null);
        Booking b3 = new Booking("B3L3", LocalDate.now(), learner3, swimmingLesson3, BookingStatus.BOOKED.name(), null);
        Booking b4 = new Booking("B4L4", LocalDate.now(), learner4, swimmingLesson4, BookingStatus.BOOKED.name(), null);
        Booking b5 = new Booking("B5L5", LocalDate.now(), learner5, swimmingLesson5, BookingStatus.BOOKED.name(), null);

        learner1.getBookings().add(b1);
        bookings.put(b1.getBookingID(), b1);
        swimmingLesson1.setAvailableSlots(swimmingLesson1.getAvailableSlots() - 1);

        learner2.getBookings().add(b2);
        bookings.put(b2.getBookingID(), b2);
        swimmingLesson2.setAvailableSlots(swimmingLesson2.getAvailableSlots() - 1);

        learner3.getBookings().add(b3);
        bookings.put(b3.getBookingID(), b3);
        swimmingLesson3.setAvailableSlots(swimmingLesson3.getAvailableSlots() - 1);

        learner4.getBookings().add(b4);
        bookings.put(b4.getBookingID(), b4);
        swimmingLesson4.setAvailableSlots(swimmingLesson4.getAvailableSlots() - 1);

        learner5.getBookings().add(b5);
        bookings.put(b5.getBookingID(), b5);
        swimmingLesson5.setAvailableSlots(swimmingLesson5.getAvailableSlots() - 1);
    }


    public List<SwimmingLesson> viewTimetable(DayOfWeek dayOfWeek, Integer grade, String coachName) {
        List<SwimmingLesson> filteredTimetable = new ArrayList<>();

        // Get today's date
        LocalDate today = LocalDate.now();

        // Filter timetable based on the provided parameters and exclude past dates
        for (SwimmingLesson swimmingLesson : timetable) {
            LocalDate lessonDate = swimmingLesson.getDate();
            if (lessonDate.isAfter(today) && // Exclude past dates
                    (dayOfWeek == null || lessonDate.getDayOfWeek() == dayOfWeek) &&
                    (grade == 0 || Objects.equals(swimmingLesson.getGrade().getValue(), grade)) &&
                    (coachName == null || swimmingLesson.getCoach().getName().equals(coachName))) {
                filteredTimetable.add(swimmingLesson);
            }
        }

        if (dayOfWeek == null && grade == 0 && coachName == null) {
            for (SwimmingLesson swimmingLesson : timetable) {
                LocalDate lessonDate = swimmingLesson.getDate();
                if (lessonDate.isAfter(today)) {
                    filteredTimetable.add(swimmingLesson);
                }
            }
        }

        return filteredTimetable;
    }


    public String bookLesson(LocalTime time, LocalDate date, String learnerId) {
        System.out.println("----------- Booking Swimming Lesson -----------------");
        // Search for the lesson in the timetable based on the provided time and day
        SwimmingLesson swimmingLessonToBook = getLessonByTimeAndDay(time, date);
        try {
            // Find the learner with the given ID
            Learner learner = getLearnerByLearnerId(learnerId);
            return swimmingSchoolService.bookLesson(swimmingLessonToBook, learner, bookings);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String changeBooking(String bookingId, LocalTime time, LocalDate date) {
        System.out.println("----------- Changing Lesson Booking:-----------------");
        Booking booking = getBookingById(bookingId);
        Learner learner = booking.getLearner();
        SwimmingLesson swimmingLessonToBook = getLessonByTimeAndDay(time, date);
        try {
            return swimmingSchoolService.changeBooking(booking, learner, swimmingLessonToBook);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public SwimmingLesson getLessonByTimeAndDay(LocalTime time, LocalDate date) {
        for (SwimmingLesson swimmingLesson : timetable) {
            if (swimmingLesson.getTimeSlot().equals(time) && swimmingLesson.getDate().equals(date)) {
                return swimmingLesson;
            }
        }
        return null;
    }

    public Learner getLearnerByLearnerId(String learnerId) {
        for (Learner l : learners) {
            if (l.getId().equals(learnerId)) {
                return l;
            }
        }
        return null;
    }

    public String markBookingAttended(String bookingId) throws InvalidBooking {
        Booking booking = getBookingById(bookingId);
            return swimmingSchoolService.markBookingAttended(booking);
    }

    public Booking getBookingById(String bookingId) {
        return bookings.get(bookingId);
    }


    // Method to generate coach ratings report
    public void generateCoachRatingsReport() {
        System.out.println("------------ Coach Ratings Report --------------");
        System.out.println("Coach Name\tAverage Monthly Rating");

        Map<String, List<Review>> coachReviewsMap = new HashMap<>(); // Map to store reviews for each coach

        // Initialize the map with empty lists for each coach
        for (Coach coach : coaches) {
            coachReviewsMap.put(coach.getName(), new ArrayList<>());
        }

        // Collect reviews for each coach
        for (SwimmingLesson swimmingLesson : timetable) {
            List<Review> reviews = swimmingLesson.getReviews();
            for (Review review : reviews) {
                List<Review> coachReviews = coachReviewsMap.get(swimmingLesson.getCoach().getName());
                coachReviews.add(review);
            }
        }

        // Calculate and display average ratings for each coach
        for (Map.Entry<String, List<Review>> entry : coachReviewsMap.entrySet()) {
            String coachName = entry.getKey();
            List<Review> reviews = entry.getValue();

            double totalRating = 0;
            int numRatings = 0;

            // Calculate total rating and count number of ratings
            for (Review review : reviews) {
                totalRating += review.getRating().getValue();
                numRatings++;
            }

            // Calculate average rating
            double avgRating = numRatings > 0 ? totalRating / numRatings : 0;

            // Display coach name and average rating
            System.out.println(coachName + "\t\t" + avgRating);
        }
    }


    public String cancelBooking(String bookingId) {
        Booking bookedLesson = getBookingById(bookingId);
        try {
            return swimmingSchoolService.cancelBooking(bookedLesson);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String provideReview(String bookingId, int rating) {
        Booking booking = getBookingById(bookingId);
        Learner learner = booking.getLearner();
        try {
            return swimmingSchoolService.provideReview(learner, booking, rating);
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String addLearner(String name, String gender, int age, String emergencyContact, int grade){
        Learner learner = new Learner();
        int sizeOfLearners = learners.size();
        String id = "L"+(sizeOfLearners+1);
        learner.setId(id);
        learner.setGender(Gender.valueOfGenderString(gender));
        learner.setName(name);
        learner.setAge(age);
        learner.setEmergencyContact(emergencyContact);
        learner.setCurrentGrade(Grade.valueOfGrade(grade));
        learner.setBookings(new ArrayList<>());
        learners.add(learner);
        System.out.println("New learner with ID " + id + " and name " + name + " has been added.");
        return id;
    }

    public static synchronized SwimmingSchool getInstance() {
        if (swimmingSchool == null) {
            swimmingSchool = new SwimmingSchool();
        }
        return swimmingSchool;
    }

    public List<SwimmingLesson> getTimetable() {
        return timetable;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public List<Learner> getLearners() {
        return learners;
    }

    public Map<String, List<SwimmingLesson>> getLessonMap() {
        return lessonMap;
    }

    public Map<String, Booking> getBookings() {
        return bookings;
    }

    public void addBookingToBookingMap(Booking booking) {
        this.bookings.put(booking.getBookingID(), booking);
    }

    public void generateDetailedLearnerReport(int monthNumber) {
        System.out.println("------------------------ Detailed Monthly Learner Information Report ------------------------------");
        System.out.println("LearnerID   | BookingID | Grade | Lesson Date   | Time      | Coach     | Booking Status | Review");
        System.out.println("___________________________________________________________________________________________________");

        LocalDate startOfMonth = LocalDate.now().withMonth(monthNumber).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        for (Learner learner : getLearners()) {
            for (Booking booking : learner.getBookings()) {
                SwimmingLesson swimmingLesson = booking.getLesson();
                Review review = booking.getReview();

                // Check if the lesson falls within the specified month
                LocalDate lessonDate = swimmingLesson.getDate();
                if (lessonDate.isEqual(startOfMonth) || (lessonDate.isAfter(startOfMonth) && lessonDate.isBefore(endOfMonth))) {
                    String rating = (review != null) ? review.getRating().name() : "-";
                    printBookingDetails(learner.getId(), booking.getBookingID(), swimmingLesson.getGrade(), swimmingLesson.getDate(), swimmingLesson.getTimeSlot(), swimmingLesson.getCoach().getName(), booking.getBookingStatus(), rating);
                }
            }
        }
    }

    private void printBookingDetails(String learnerId, String bookingId, Grade grade, LocalDate lessonDate, LocalTime time, String coach, String bookingStatus, String review) {
        System.out.printf("%-10s | %-9s | %-5s | %-13s | %-8s | %-9s | %-14s | %-6s%n",
                learnerId, bookingId, grade, lessonDate, time, coach, bookingStatus, review);
    }


    public void generateMonthlySummaryOfBookings(int monthNumber) {
        System.out.println("------------------------ Summary of Monthly Learners Bookings -------------------------------");
        System.out.println("LearnerID | Learner Name           | Current Grade | Booked | Changed | Cancelled | Attended");
        System.out.println("_____________________________________________________________________________________________");

        LocalDate startOfMonth = LocalDate.now().withMonth(monthNumber).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        for (Learner learner : learners) {
            String learnerName = learner.getName();
            String learnerId = learner.getId();
            String currentGrade = learner.getCurrentGrade().name();
            int booked = 0, cancelled = 0, attended = 0, changed = 0;

            for (Booking booking : learner.getBookings()) {
                SwimmingLesson swimmingLesson = booking.getLesson();
                LocalDate lessonDate = swimmingLesson.getDate();

                // Check if the lesson falls within the specified month
                if ((lessonDate.isEqual(startOfMonth) || lessonDate.isAfter(startOfMonth)) && lessonDate.isBefore(endOfMonth)) {
                    switch (booking.getBookingStatus()) {
                        case "BOOKED":
                            booked++;
                            break;
                        case "CANCELLED":
                            cancelled++;
                            break;
                        case "ATTENDED":
                            attended++;
                            break;
                        case "CHANGED":
                            changed++;
                            break;
                    }
                }
            }

            System.out.printf("%-10s | %-24s | %-13s | %-6s | %-8s | %-9s | %-8s%n",
                    learnerId, learnerName, currentGrade, booked, changed, cancelled, attended);
        }
    }



}
