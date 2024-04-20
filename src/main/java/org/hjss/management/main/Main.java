package org.hjss.management.main;

import org.hjss.management.constants.BookingStatus;
import org.hjss.management.constants.Grade;
import org.hjss.management.models.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final SwimmingSchool swimmingSchool = new SwimmingSchool();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("__________________________________________________________________________________");
            System.out.println("_______________________ HJSS Management Tool ____________________________________");
            System.out.println("Select an option:");
            System.out.println("1. View whole timetable");
            System.out.println("2. View timetable by day");
            System.out.println("3. View timetable by grade level");
            System.out.println("4. View timetable by coach's name");
            System.out.println("5. Book lesson");
            System.out.println("6. Change Booking");
            System.out.println("7. Cancel booking");
            System.out.println("8. Mark your booking attended");
            System.out.println("9. Generate 4 weeks of Booking report");
            System.out.println("10. Generate report for coaches");
            System.out.println("11. Get All Bookings available");
            System.out.println("12. Add new Leaner");
            System.out.println("13. View all learners");
            System.out.println("Select your input or 0 to exit:");
            int input = scanner.nextInt();
            switch (input) {
                case 0:
                    System.exit(0);
                case 1:
                    viewTimetable(swimmingSchool.viewTimetable(null, 0, null));
                    break;
                case 2:
                    viewTimetableByDay(scanner);
                    break;
                case 3:
                    viewTimetableByGrade(scanner);
                    break;
                case 4:
                    viewTimetableByCoach(scanner);
                    break;
                case 5:
                    bookLesson(scanner);
                    break;
                case 6:
                    changeBooking(scanner);
                    break;
                case 7:
                    cancelBooking(scanner);
                    break;
                case 8:
                    markBookingAttended(scanner);
                    break;
                case 9:
                    GenerateReport(scanner);
                    break;
                case 10:
                    swimmingSchool.generateCoachRatingsReport();
                    break;
                case 11:
                    viewAvailableBookings();
                    break;
                case 12:
                    addNewLearner(scanner);
                    break;
                case 13:
                    viewAvailableLearners();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void GenerateReport(Scanner scanner) {
        System.out.println("Select the type of report:");
        System.out.println("1. Summary of bookings");
        System.out.println("2. Detailed information for each booking");
        int reportType = scanner.nextInt();
        switch (reportType) {
            case 1:
                // Generate summary of bookings
                System.out.println("Enter month number (1-12) for the report:");
                int monthNumber = scanner.nextInt();
                swimmingSchool.generateMonthlySummaryOfBookings(monthNumber);
                break;
            case 2:
                // Generate detailed information for each booking
                System.out.println("Enter month number (1-12) for the report:");
                monthNumber = scanner.nextInt();
                swimmingSchool.generateDetailedLearnerReport(monthNumber);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private static void addNewLearner(Scanner scanner) {
        System.out.println("Enter learner name:");
        String name = scanner.next();

        System.out.println("Enter learner gender (male/female):");
        String gender = scanner.next();

        int age;
        while (true) {
            System.out.println("Enter learner age between (4 to 11):");
            age = scanner.nextInt();
            if (age >= 4 && age <= 11) {
                break;
            } else {
                System.out.println("Invalid age! Please enter an age between 4 and 11.");
            }
        }

        System.out.println("Enter emergency contact:");
        String emergencyContact = scanner.next();

        int grade;
        while (true) {
            System.out.println("Enter learner grade (1-5):");
            grade = scanner.nextInt();
            if (grade >= 1 && grade <= 5) {
                break;
            } else {
                System.out.println("Invalid grade! Please enter a grade between 1 and 5.");
            }
        }
        swimmingSchool.addLearner(name, gender, age, emergencyContact, grade);
    }


    public static void viewTimetable(List<SwimmingLesson> swimmingLessons) {
        System.out.printf("%-10s | %-10s | %-12s | %-12s | %-15s | %-10s%n",
                "Grade", "Day", "Date", "Time", "Coach", "Available Slots");

        for (SwimmingLesson swimmingLesson : swimmingLessons) {
            Coach coach = swimmingLesson.getCoach();
            System.out.printf("%-10s | %-10s | %-12s | %-12s | %-15s | %-10s%n",
                    swimmingLesson.getGrade(),
                    swimmingLesson.getDate().getDayOfWeek(),
                    swimmingLesson.getDate(),
                    swimmingLesson.getTimeSlot(),
                    coach.getName(),
                    swimmingLesson.getAvailableSlots());
        }
    }

    private static void cancelBooking(Scanner scanner) {
        viewAvailableBookedBookings();
        System.out.print("Enter booking ID to cancel: ");
        String bookingIdToCancel = scanner.next();
        System.out.println(swimmingSchool.cancelBooking(bookingIdToCancel));
    }
    private static void markBookingAttended(Scanner scanner) {
        viewAvailableBookedBookings();
        try {
            System.out.print("Enter booking Id: ");
            String bookingId = scanner.next();
            if (bookingId == null || bookingId.isEmpty()) {
                System.out.println("Enter a valid booking id.");
                return;
            }
            String reply = swimmingSchool.markBookingAttended(bookingId.trim());
            System.out.println(reply);

            // Prompt the user to give a rating
            System.out.println("Now please provide your rating for this booking:");
            System.out.println("Enter 1 for VERY DISSATISFIED");
            System.out.println("Enter 2 for DISSATISFIED");
            System.out.println("Enter 3 for OK");
            System.out.println("Enter 4 for SATISFIED");
            System.out.println("Enter 5 for VERY SATISFIED");
            System.out.print("Enter rating (1 to 5): ");
            int rating = scanner.nextInt();
            System.out.println(swimmingSchool.provideReview(bookingId, rating));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void viewTimetableByDay(Scanner scanner) {
        System.out.println("Enter day (Monday, Wednesday, Friday, Saturday):");
        String dayInput = scanner.next();
        DayOfWeek day = DayOfWeek.valueOf(dayInput.toUpperCase());
        viewTimetable(swimmingSchool.viewTimetable(day, 0, null));
    }

    private static void viewTimetableByGrade(Scanner scanner) {
        System.out.println("Enter grade level (1, 2, 3, 4, 5):");
        int grade = scanner.nextInt();
        viewTimetable(swimmingSchool.viewTimetable(null, grade, null));
    }

    private static void viewTimetableByCoach(Scanner scanner) {
        System.out.println("Enter coach's name:");
        String coachName = scanner.next();
        viewTimetable(swimmingSchool.viewTimetable(null, 0, coachName));
    }

    public static void viewAvailableLearners() {
        System.out.println("Check out the Available Learners in the system:");
        System.out.println("-------------------------------");
        System.out.printf("%-5s | %-15s | %-10s%n", "ID", "Name", "Grade");
        System.out.println("-------------------------------");
        for (Learner learner : swimmingSchool.getLearners()) {
            System.out.printf("%-5s | %-15s | %-10s%n", learner.getId(), learner.getName(), learner.getCurrentGrade());
        }
    }

    private static void bookLesson(Scanner scanner) {
        viewAvailableLearners();
        viewTimetableOption(scanner);

        System.out.println("Now Enter learner ID to proceed with the booking: ");
        String learnerId = scanner.next();

        LocalDate date = getDateInput(scanner);
        LocalTime time = getTimeInput(scanner, date);

        System.out.println(swimmingSchool.bookLesson(time, date, learnerId));
    }

    private static void changeBooking(Scanner scanner) {

        viewAvailableBookedBookings();

        String bookingId = null;
        boolean validBookingId = false;
        Booking booking = null;
        while (!validBookingId) {
            System.out.print("Enter booking ID to change: ");
            bookingId = scanner.next();
            booking = swimmingSchool.getBookingById(bookingId);

            if (booking == null) {
                System.out.println("Invalid Booking ID! Please try again.");
            } else {
                validBookingId = true;
            }
        }
        System.out.println("You are changing booking details of: " + booking.getLearner().getName() + " for booking ID: " + booking.getBookingID());
        viewTimetableOption(scanner);
        // Get day of the week from the user
        LocalDate date = getDateInput(scanner);

        // Get time slot from the user
        LocalTime time = getTimeInput(scanner, date);

        System.out.println(swimmingSchool.changeBooking(bookingId.trim(), time, date));
    }

    private static List<Booking> viewAvailableBookedBookings() {
        List<Booking> availableBookings = new ArrayList<>();
        for (Booking booking : swimmingSchool.getBookings().values()) {
            if (booking.getBookingStatus().equals(BookingStatus.BOOKED.name())) {
                availableBookings.add(booking);
            }
        }
        System.out.println("Available Bookings:");
        System.out.printf("%-10s | %-15s | %-12s | %-12s | %-15s | %-12s | %-12s | %-10s%n",
                "Booking ID", "Learner Name", "Current Grade", "Booked Grade", "Lesson Date", "Lesson Day", "Lesson Time", "Booking Status");

        for (Booking availableBooking : availableBookings) {
            Learner learner = availableBooking.getLearner();
            SwimmingLesson lesson = availableBooking.getLesson();
            Grade learnerCurrentGrade = learner.getCurrentGrade();
            Grade learnerBookedGrade = lesson.getGrade();
            String status = availableBooking.getBookingStatus();
            LocalDate lessonDate = lesson.getDate();
            DayOfWeek lessonDay = lesson.getDate().getDayOfWeek();
            String lessonTime = formatTimeSlot(lesson.getTimeSlot());


            System.out.printf("%-10s | %-15s | %-12s | %-12s | %-15s | %-12s | %-12s | %-10s%n",
                    availableBooking.getBookingID(), learner.getName(), learnerCurrentGrade,
                    learnerBookedGrade, lessonDate, lessonDay, lessonTime, status);
        }
        return availableBookings;
    }

    private static void viewAvailableBookings() {

        List<Booking> availableBookings = new ArrayList<>(swimmingSchool.getBookings().values());

        System.out.println("Available Bookings:");
        System.out.printf("%-10s | %-15s | %-12s | %-12s | %-15s | %-12s | %-12s | %-10s%n",
                "Booking ID", "Learner Name", "Current Grade", "Booked Grade", "Lesson Date", "Lesson Day", "Lesson Time", "Booking Status");

        for (Booking availableBooking : availableBookings) {
            Learner learner = availableBooking.getLearner();
            SwimmingLesson lesson = availableBooking.getLesson();
            Grade learnerCurrentGrade = learner.getCurrentGrade();
            Grade learnerBookedGrade = lesson.getGrade();
            String status = availableBooking.getBookingStatus();
            LocalDate lessonDate = lesson.getDate();
            DayOfWeek lessonDay = lesson.getDate().getDayOfWeek();
            String lessonTime = formatTimeSlot(lesson.getTimeSlot());


            System.out.printf("%-10s | %-15s | %-12s | %-12s | %-15s | %-12s | %-12s | %-10s%n",
                    availableBooking.getBookingID(), learner.getName(), learnerCurrentGrade,
                    learnerBookedGrade, lessonDate, lessonDay, lessonTime, status);
        }
    }


    private static void viewTimetableOption(Scanner scanner) {
        System.out.println("In case you want to view the timetable, select an option from below or enter 0 to proceed with the booking:");
        System.out.println("1. View timetable by day");
        System.out.println("2. View timetable by grade level");
        System.out.println("3. View timetable by coach's name");

        int viewOption = scanner.nextInt();
        switch (viewOption) {
            case 0:
                break;
            case 1:
                viewTimetableByDay(scanner);
                break;
            case 2:
                viewTimetableByGrade(scanner);
                break;
            case 3:
                viewTimetableByCoach(scanner);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private static LocalTime getTimeInput(Scanner scanner, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalTime[] lessonTimes = {LocalTime.of(16, 0), LocalTime.of(17, 0), LocalTime.of(18, 0)};
        LocalTime[] saturdayLessonTimes = {LocalTime.of(14, 0), LocalTime.of(15, 0)};
        LocalTime[] availableTimes = (dayOfWeek == DayOfWeek.SATURDAY) ? saturdayLessonTimes : lessonTimes;

        // Display available time slots for the given day
        System.out.println("Available time slots:");
        for (LocalTime lessonTime : availableTimes) {
            System.out.println(formatTimeSlot(lessonTime));
        }

        // Get time slot from the user
        LocalTime time = null;
        boolean validInput = false;
        do {
            System.out.print("Enter time (HH:mm): ");
            String timeInput = scanner.next();
            try {
                time = LocalTime.parse(timeInput);
                for (LocalTime availableTime : availableTimes) {
                    if (time.equals(availableTime)) {
                        validInput = true;
                        break;
                    }
                }
                if (!validInput) {
                    throw new IllegalArgumentException("Invalid time slot. Please enter a valid time slot.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!validInput);
        return time;
    }

    private static String formatTimeSlot(LocalTime time) {
        // Format time slot in desired format
        return String.format("%02d:%02d (%d:%02d %s)",
                time.getHour(),
                time.getMinute(),
                time.getHour() > 12 ? time.getHour() - 12 : time.getHour(),
                time.getMinute(),
                time.getHour() < 12 ? "AM" : "PM");
    }

    private static LocalDate getDateInput(Scanner scanner) {
        LocalDate date = null;
        boolean validInput = false;
        do {
            System.out.print("Enter date (yyyy-MM-dd): ");
            String dateInput = scanner.next();
            try {
                date = LocalDate.parse(dateInput);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
            }
        } while (!validInput);
        return date;
    }


}