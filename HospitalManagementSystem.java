import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

// ============================================================
//  HOSPITAL MANAGEMENT SYSTEM
//  A beginner-friendly Java OOP project
// ============================================================

// ---------------------------------------------------------------
// INTERFACE: Billable
// An interface defines a "contract" - any class that implements
// it MUST provide the calculateBill() method.
// This is used for POLYMORPHISM.
// ---------------------------------------------------------------
interface Billable {
    double calculateBill(); // every patient must be able to calculate their own bill
}

// ---------------------------------------------------------------
// ABSTRACT CLASS: Person
// An abstract class is a base class that cannot be created directly.
// Both Doctor and Patient share common properties (id, name),
// so we put those here. This is the base for INHERITANCE.
// ---------------------------------------------------------------
abstract class Person {

    // private = ENCAPSULATION (data hiding)
    private String id;
    private String name;

    // Constructor: sets id and name when object is created
    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters (part of ENCAPSULATION)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Abstract method: every subclass MUST override this
    public abstract void displayInfo();
}

// ---------------------------------------------------------------
// CLASS: Doctor
// Inherits from Person using "extends".
// Doctors are predefined in the system (not entered by user).
// ---------------------------------------------------------------
class Doctor extends Person {

    private String specialization; // "General Physician" or "Specialist"
    // List of patient names assigned to this doctor
    private ArrayList<String> assignedPatients;

    // Constructor
    public Doctor(String id, String name, String specialization) {
        super(id, name); // calls Person constructor
        this.specialization = specialization;
        this.assignedPatients = new ArrayList<>();
    }

    // Getters
    public String getSpecialization() {
        return specialization;
    }

    public ArrayList<String> getAssignedPatients() {
        return assignedPatients;
    }

    // Add a patient to this doctor's list
    public void addPatient(String patientName) {
        assignedPatients.add(patientName);
    }

    // Display doctor info (overrides abstract method from Person)
    @Override
    public void displayInfo() {
        System.out.println("Doctor ID   : " + getId());
        System.out.println("Doctor Name : Dr. " + getName());
        System.out.println("Specialization : " + specialization);
    }

    // Show all assigned patients
    public void showAssignedPatients() {
        if (assignedPatients.isEmpty()) {
            System.out.println("No patients assigned yet.");
        } else {
            System.out.println("Assigned Patients:");
            for (int i = 0; i < assignedPatients.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + assignedPatients.get(i));
            }
        }
    }
}

// ---------------------------------------------------------------
// CLASS: Patient (abstract, extends Person, implements Billable)
// Base class for InPatient and OutPatient.
// This shows INHERITANCE and INTERFACE IMPLEMENTATION.
// ---------------------------------------------------------------
abstract class Patient extends Person implements Billable {

    private int age;
    private String contact;
    private String doctorName;       // doctor assigned
    private String appointmentDate;
    private String diagnosis;
    private String treatment;
    private String medicines;
    private String doctorSpecialization; // needed for billing

    // Constructor
    public Patient(String id, String name, int age, String contact) {
        super(id, name);
        this.age = age;
        this.contact = contact;
        this.diagnosis = "Not yet diagnosed";
        this.treatment = "Not yet treated";
        this.medicines = "None";
        this.doctorName = "Not assigned";
        this.appointmentDate = "Not booked";
        this.doctorSpecialization = "General Physician";
    }

    // Getters and Setters
    public int getAge() { return age; }
    public String getContact() { return contact; }
    public String getDoctorName() { return doctorName; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getMedicines() { return medicines; }
    public String getDoctorSpecialization() { return doctorSpecialization; }

    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setAppointmentDate(String date) { this.appointmentDate = date; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setMedicines(String medicines) { this.medicines = medicines; }
    public void setDoctorSpecialization(String spec) { this.doctorSpecialization = spec; }

    // Show full patient info
    @Override
    public void displayInfo() {
        System.out.println("Patient ID  : " + getId());
        System.out.println("Name        : " + getName());
        System.out.println("Age         : " + age);
        System.out.println("Contact     : " + contact);
        System.out.println("Doctor      : Dr. " + doctorName + " (" + doctorSpecialization + ")");
        System.out.println("Appointment : " + appointmentDate);
        System.out.println("Diagnosis   : " + diagnosis);
        System.out.println("Treatment   : " + treatment);
        System.out.println("Medicines   : " + medicines);
    }

    // Abstract method for patient type label
    public abstract String getPatientType();

    // calculateBill() is from Billable interface - implemented by subclasses
    // This is POLYMORPHISM: each subclass calculates bill differently
}

// ---------------------------------------------------------------
// CLASS: InPatient (extends Patient)
// An in-patient stays in the hospital for multiple days.
// Bill = days * per-day-charge (higher if Specialist)
// ---------------------------------------------------------------
class InPatient extends Patient {

    private int numberOfDays; // how many days admitted

    // Constructor
    public InPatient(String id, String name, int age, String contact, int days) {
        super(id, name, age, contact);
        this.numberOfDays = days;
    }

    public int getNumberOfDays() { return numberOfDays; }

    @Override
    public String getPatientType() {
        return "InPatient";
    }

    // POLYMORPHISM: InPatient bill = days * daily charge
    @Override
    public double calculateBill() {
        double perDayCharge;

        // Specialist charges more than General Physician
        if (getDoctorSpecialization().equalsIgnoreCase("Specialist")) {
            perDayCharge = 2000.0; // Rs. 2000 per day for specialist
        } else {
            perDayCharge = 1200.0; // Rs. 1200 per day for general physician
        }

        double totalBill = numberOfDays * perDayCharge;
        return totalBill;
    }

    @Override
    public void displayInfo() {
        super.displayInfo(); // call parent displayInfo
        System.out.println("Patient Type: InPatient");
        System.out.println("Days Admitted: " + numberOfDays);
    }
}

// ---------------------------------------------------------------
// CLASS: OutPatient (extends Patient)
// An out-patient visits for consultation only.
// Bill = fixed consultation fee
// ---------------------------------------------------------------
class OutPatient extends Patient {

    private String problem; // what problem they came for

    // Constructor
    public OutPatient(String id, String name, int age, String contact, String problem) {
        super(id, name, age, contact);
        this.problem = problem;
    }

    public String getProblem() { return problem; }

    @Override
    public String getPatientType() {
        return "OutPatient";
    }

    // POLYMORPHISM: OutPatient bill = fixed fee
    @Override
    public double calculateBill() {
        // Specialist charges more for consultation
        if (getDoctorSpecialization().equalsIgnoreCase("Specialist")) {
            return 800.0; // Rs. 800 flat for specialist
        } else {
            return 400.0; // Rs. 400 flat for general physician
        }
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Patient Type: OutPatient");
        System.out.println("Problem     : " + problem);
    }
}

// ---------------------------------------------------------------
// CLASS: Appointment
// Stores appointment details: who booked, which doctor, what date
// ---------------------------------------------------------------
class Appointment {

    private String patientId;
    private String patientName;
    private String doctorName;
    private String date;

    public Appointment(String patientId, String patientName, String doctorName, String date) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
    }

    // Display appointment info
    public void displayAppointment() {
        System.out.println("Patient ID   : " + patientId);
        System.out.println("Patient Name : " + patientName);
        System.out.println("Doctor       : Dr. " + doctorName);
        System.out.println("Date         : " + date);
    }

    // Getters
    public String getDoctorName() { return doctorName; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDate() { return date; }
}

// ---------------------------------------------------------------
// CLASS: FileManager
// Handles saving and loading data to/from text files.
// Uses FileWriter and FileReader (simple file handling).
// ---------------------------------------------------------------
class FileManager {

    // Save patient data to a file
    public static void savePatient(Patient p) {
        try {
            // FileWriter with true = append mode (won't overwrite existing data)
            FileWriter fw = new FileWriter("patients.txt", true);
            fw.write("ID:" + p.getId() + ",Name:" + p.getName() +
                     ",Age:" + p.getAge() + ",Contact:" + p.getContact() +
                     ",Type:" + p.getPatientType() + ",Doctor:" + p.getDoctorName() +
                     ",Date:" + p.getAppointmentDate() + "\n");
            fw.close();
            System.out.println("[File] Patient data saved to patients.txt");
        } catch (IOException e) {
            System.out.println("[File Error] Could not save patient data: " + e.getMessage());
        }
    }

    // Save appointment data to a file
    public static void saveAppointment(Appointment a) {
        try {
            FileWriter fw = new FileWriter("appointments.txt", true);
            fw.write("PatientID:" + a.getPatientId() +
                     ",PatientName:" + a.getPatientName() +
                     ",Doctor:" + a.getDoctorName() +
                     ",Date:" + a.getDate() + "\n");
            fw.close();
            System.out.println("[File] Appointment saved to appointments.txt");
        } catch (IOException e) {
            System.out.println("[File Error] Could not save appointment: " + e.getMessage());
        }
    }

    // Save treatment data to a file
    public static void saveTreatment(Patient p) {
        try {
            FileWriter fw = new FileWriter("treatments.txt", true);
            fw.write("PatientID:" + p.getId() +
                     ",Diagnosis:" + p.getDiagnosis() +
                     ",Treatment:" + p.getTreatment() +
                     ",Medicines:" + p.getMedicines() + "\n");
            fw.close();
            System.out.println("[File] Treatment data saved to treatments.txt");
        } catch (IOException e) {
            System.out.println("[File Error] Could not save treatment: " + e.getMessage());
        }
    }

    // Read and display all appointments from file
    public static void readAppointments() {
        try {
            FileReader fr = new FileReader("appointments.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            System.out.println("\n--- All Saved Appointments ---");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("[File] No appointment records found yet.");
        }
    }
}

// ---------------------------------------------------------------
// MAIN CLASS: HospitalManagementSystem
// This is where the program starts (main method is here).
// ---------------------------------------------------------------
public class HospitalManagementSystem {

    // Global list to store all registered patients
    static ArrayList<Patient> patientList = new ArrayList<>();

    // Global list to store all appointments
    static ArrayList<Appointment> appointmentList = new ArrayList<>();

    // Predefined doctors (not entered by user)
    static Doctor[] doctors = {
        new Doctor("D001", "Arjun Mehta",   "General Physician"),
        new Doctor("D002", "Priya Sharma",  "General Physician"),
        new Doctor("D003", "Ravi Kapoor",   "Specialist"),
        new Doctor("D004", "Sunita Rao",    "Specialist")
    };

    // Scanner for reading user input (one Scanner used throughout)
    static Scanner sc = new Scanner(System.in);

    // ============================================================
    // MAIN METHOD: Entry point of the program
    // ============================================================
    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("   WELCOME TO CITY HOSPITAL SYSTEM");
        System.out.println("==========================================");

        boolean running = true;

        // Keep showing main menu until user exits
        while (running) {
            System.out.println("\nAre you a Doctor or a Patient?");
            System.out.println("1. Doctor");
            System.out.println("2. Patient");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // if user types something that's not a number
                System.out.println("[Error] Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    doctorLogin(); // go to doctor side
                    break;
                case 2:
                    patientMenu(); // go to patient side
                    break;
                case 3:
                    System.out.println("Thank you for using Hospital Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // ============================================================
    // DOCTOR LOGIN & MENU
    // ============================================================
    static void doctorLogin() {
        System.out.println("\n--- DOCTOR LOGIN ---");
        System.out.print("Enter your Doctor ID: ");
        String inputId = sc.nextLine().trim();

        // Search for doctor in predefined list
        Doctor foundDoctor = null;
        for (Doctor d : doctors) {
            if (d.getId().equalsIgnoreCase(inputId)) {
                foundDoctor = d;
                break;
            }
        }

        // If doctor not found
        if (foundDoctor == null) {
            System.out.println("[Error] Doctor ID not found. Please check your ID.");
            return;
        }

        // Doctor found - welcome message
        System.out.println("\nWelcome, Dr. " + foundDoctor.getName() + "!");
        foundDoctor.displayInfo();

        // Doctor dashboard menu
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- DOCTOR DASHBOARD ---");
            System.out.println("1. View Today's Appointments");
            System.out.println("2. View Assigned Patients");
            System.out.println("3. View Patient Details & Add Treatment");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewDoctorAppointments(foundDoctor);
                    break;
                case 2:
                    foundDoctor.showAssignedPatients();
                    break;
                case 3:
                    addTreatment(foundDoctor);
                    break;
                case 4:
                    System.out.println("Logged out successfully.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Show all appointments for a specific doctor
    static void viewDoctorAppointments(Doctor doc) {
        System.out.println("\n--- Today's Appointments for Dr. " + doc.getName() + " ---");
        boolean found = false;

        for (Appointment a : appointmentList) {
            if (a.getDoctorName().equalsIgnoreCase(doc.getName())) {
                a.displayAppointment();
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments booked yet.");
        }
    }

    // Doctor enters diagnosis, treatment, medicines for a patient
    static void addTreatment(Doctor doc) {
        System.out.print("Enter Patient ID to treat: ");
        String pid = sc.nextLine().trim();

        // Find patient in list
        Patient found = null;
        for (Patient p : patientList) {
            if (p.getId().equalsIgnoreCase(pid)) {
                found = p;
                break;
            }
        }

        if (found == null) {
            System.out.println("[Error] Patient ID not found.");
            return;
        }

        // Show current patient info
        System.out.println("\nPatient Details:");
        found.displayInfo();

        // Enter treatment info
        System.out.print("\nEnter Diagnosis: ");
        String diag = sc.nextLine().trim();

        System.out.print("Enter Treatment: ");
        String treat = sc.nextLine().trim();

        System.out.print("Enter Medicines: ");
        String meds = sc.nextLine().trim();

        // Save to patient object
        found.setDiagnosis(diag);
        found.setTreatment(treat);
        found.setMedicines(meds);

        System.out.println("\n[Success] Treatment updated!");
        System.out.println("Diagnosis  : " + diag);
        System.out.println("Treatment  : " + treat);
        System.out.println("Medicines  : " + meds);

        // Save to file
        FileManager.saveTreatment(found);
    }

    // ============================================================
    // PATIENT MENU (Register / Login)
    // ============================================================
    static void patientMenu() {
        System.out.println("\n--- PATIENT MENU ---");
        System.out.println("1. Register (New Patient)");
        System.out.println("2. Login (Existing Patient)");
        System.out.print("Enter choice: ");

        int choice = -1;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Enter a valid number.");
            return;
        }

        switch (choice) {
            case 1:
                registerPatient();
                break;
            case 2:
                loginPatient();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // ============================================================
    // PATIENT REGISTRATION
    // ============================================================
    static void registerPatient() {
        System.out.println("\n--- PATIENT REGISTRATION ---");

        System.out.print("Enter Patient ID: ");
        String pid = sc.nextLine().trim();

        // Check if ID already exists
        for (Patient p : patientList) {
            if (p.getId().equalsIgnoreCase(pid)) {
                System.out.println("[Error] Patient ID already exists. Please login.");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        int age = 0;
        try {
            System.out.print("Enter Age: ");
            age = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid age. Please enter a number.");
            return;
        }

        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine().trim();

        // Ask patient type
        System.out.println("\nAre you an InPatient or OutPatient?");
        System.out.println("1. InPatient (Admitted)");
        System.out.println("2. OutPatient (Visiting)");
        System.out.print("Enter choice: ");

        int typeChoice = -1;
        try {
            typeChoice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid input.");
            return;
        }

        Patient newPatient = null;

        if (typeChoice == 1) {
            // InPatient - ask number of days
            int days = 0;
            try {
                System.out.print("Enter number of days admitted: ");
                days = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid number of days.");
                return;
            }
            // Create InPatient object (POLYMORPHISM - different type of Patient)
            newPatient = new InPatient(pid, name, age, contact, days);

        } else if (typeChoice == 2) {
            // OutPatient - ask problem
            System.out.print("Describe your problem/illness: ");
            String problem = sc.nextLine().trim();
            // Create OutPatient object
            newPatient = new OutPatient(pid, name, age, contact, problem);

        } else {
            System.out.println("Invalid choice.");
            return;
        }

        // Add to list
        patientList.add(newPatient);

        System.out.println("\n[Success] Patient registered successfully!");
        System.out.println("Welcome, " + name + "!");

        // Now let the patient book appointment
        bookAppointment(newPatient);

        // Save patient to file
        FileManager.savePatient(newPatient);
    }

    // ============================================================
    // PATIENT LOGIN
    // ============================================================
    static void loginPatient() {
        System.out.println("\n--- PATIENT LOGIN ---");
        System.out.print("Enter Patient ID: ");
        String pid = sc.nextLine().trim();

        Patient found = null;
        for (Patient p : patientList) {
            if (p.getId().equalsIgnoreCase(pid)) {
                found = p;
                break;
            }
        }

        if (found == null) {
            System.out.println("[Error] Patient not found. Please register first.");
            return;
        }

        System.out.println("\nWelcome back, " + found.getName() + "!");
        patientDashboard(found);
    }

    // ============================================================
    // PATIENT DASHBOARD (after login)
    // ============================================================
    static void patientDashboard(Patient p) {
        boolean active = true;

        while (active) {
            System.out.println("\n--- PATIENT DASHBOARD ---");
            System.out.println("1. View My Details");
            System.out.println("2. Book / Change Appointment");
            System.out.println("3. View Bill");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- MY DETAILS ---");
                    p.displayInfo();
                    break;
                case 2:
                    bookAppointment(p);
                    break;
                case 3:
                    showBill(p);
                    break;
                case 4:
                    System.out.println("Logged out.");
                    active = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // APPOINTMENT BOOKING
    // ============================================================
    static void bookAppointment(Patient p) {
        System.out.println("\n--- BOOK APPOINTMENT ---");
        System.out.println("Available Doctors:");
        System.out.println("--------------------------------------------------");

        // Show all predefined doctors
        for (int i = 0; i < doctors.length; i++) {
            System.out.println((i + 1) + ". Dr. " + doctors[i].getName() +
                               " | " + doctors[i].getSpecialization() +
                               " | ID: " + doctors[i].getId());
        }
        System.out.println("--------------------------------------------------");

        System.out.print("Select Doctor (enter number 1-4): ");
        int docChoice = -1;

        try {
            docChoice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid input.");
            return;
        }

        // Validate choice
        if (docChoice < 1 || docChoice > 4) {
            System.out.println("[Error] Invalid doctor selection.");
            return;
        }

        Doctor selectedDoctor = doctors[docChoice - 1];

        System.out.print("Enter Appointment Date (e.g., 15-July-2025): ");
        String date = sc.nextLine().trim();

        // Update patient with doctor info
        p.setDoctorName(selectedDoctor.getName());
        p.setAppointmentDate(date);
        p.setDoctorSpecialization(selectedDoctor.getSpecialization());

        // Add patient name to doctor's list
        selectedDoctor.addPatient(p.getName());

        // Create appointment object and add to list
        Appointment appt = new Appointment(p.getId(), p.getName(), selectedDoctor.getName(), date);
        appointmentList.add(appt);

        // Save appointment to file
        FileManager.saveAppointment(appt);

        // Confirmation message
        System.out.println("\n[Appointment Confirmed!]");
        System.out.println("Doctor      : Dr. " + selectedDoctor.getName());
        System.out.println("Specialization: " + selectedDoctor.getSpecialization());
        System.out.println("Date        : " + date);
    }

    // ============================================================
    // BILLING SYSTEM
    // Uses calculateBill() from Billable interface (POLYMORPHISM)
    // InPatient and OutPatient calculate differently
    // ============================================================
    static void showBill(Patient p) {
        System.out.println("\n==========================================");
        System.out.println("         CITY HOSPITAL - FINAL BILL");
        System.out.println("==========================================");
        System.out.println("Patient ID     : " + p.getId());
        System.out.println("Patient Name   : " + p.getName());
        System.out.println("Patient Type   : " + p.getPatientType());
        System.out.println("Doctor         : Dr. " + p.getDoctorName());
        System.out.println("Specialization : " + p.getDoctorSpecialization());
        System.out.println("Appointment    : " + p.getAppointmentDate());
        System.out.println("Diagnosis      : " + p.getDiagnosis());
        System.out.println("Treatment      : " + p.getTreatment());
        System.out.println("Medicines      : " + p.getMedicines());

        // Show type-specific info
        if (p instanceof InPatient) {
            InPatient ip = (InPatient) p; // type casting
            System.out.println("Days Admitted  : " + ip.getNumberOfDays());

            // Explain the charge
            if (p.getDoctorSpecialization().equalsIgnoreCase("Specialist")) {
                System.out.println("Per Day Charge : Rs. 2000 (Specialist)");
            } else {
                System.out.println("Per Day Charge : Rs. 1200 (General Physician)");
            }

        } else if (p instanceof OutPatient) {
            OutPatient op = (OutPatient) p;
            System.out.println("Problem        : " + op.getProblem());

            if (p.getDoctorSpecialization().equalsIgnoreCase("Specialist")) {
                System.out.println("Consultation   : Rs. 800 (Specialist)");
            } else {
                System.out.println("Consultation   : Rs. 400 (General Physician)");
            }
        }

        System.out.println("------------------------------------------");

        // Call calculateBill() - POLYMORPHISM in action!
        // Java calls the correct version (InPatient or OutPatient) automatically
        double totalBill = p.calculateBill();
        System.out.println("TOTAL BILL     : Rs. " + totalBill);
        System.out.println("==========================================");
        System.out.println("  Thank you for choosing City Hospital!");
        System.out.println("==========================================");
    }
}
