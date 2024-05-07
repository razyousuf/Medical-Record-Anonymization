/****************************************************************************************************************
 * AnonymizationTool.java
 * 
 * This Java program anonymizes personal data in medical notes, replacing names, dates of birth, addresses,
 * phone numbers, and email addresses with unique identifiers. It reads input from a file named "PatientNotes.txt",
 * performs anonymization, and writes the anonymized data to a file named "AnonymizedData.txt". Additionally, it
 * generates a log file named "MappedData.txt" containing mappings of original data to anonymized IDs.
 * 
 * The program follows these steps:
 * 
 * Step 1: Reading the input file:
 * - The program reads the contents of the input file "PatientNotes.txt" into a string.
 * 
 * Step 2: Anonymizing personal data:
 * - Personal data such as names (with titles), dates of birth, addresses, phone numbers, and email addresses
 *   are anonymized using regular expressions.
 * - Names are anonymized with unique IDs, preserving titles, first names, and last names. If a name appears multiple
 *   times in the text, it is replaced with the same ID each time to maintain consistency.
 * 
 * Step 3: Writing anonymized data to output files:
 * - The anonymized data is written to a file named "AnonymizedData.txt".
 * - The mappings of original data to anonymized IDs are written to a log file named "MappedData.txt".
 * 
 * This program utilizes regular expressions to identify and replace personal data efficiently. It employs
 * maps to store names and their corresponding IDs, ensuring consistent anonymization across the text.
 * 
 * @authors Raz M. Yousufi
 * @version 1.0
 ****************************************************************************************************************/
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnonymizationTool {
    static StringBuilder mappedData = new StringBuilder(); // StringBuilder to store the mapped data and their IDs

    public static void main(String[] args) {
        // Input and output filenames
        String inputFilename = "PatientNotes.txt";
        String outputFilename = "AnonymizedData.txt";
        String mappedFilename = "MappedData.txt"; // Renamed to avoid confusion

        try {
            // Step 1: Reading the file
            System.out.println("Reading the file...");
            String sampleMedicalNotes = readFile(inputFilename);

            // Step 2: Anonymizing personal data
            System.out.println("Replacing Personal Data with respective IDs... \n");
            System.out.println("============================================");
            String anonymizedNotes = anonymizeData(sampleMedicalNotes);

            // Step 3: Writing anonymized data to a file
            writeToFile(anonymizedNotes, outputFilename);
            System.out.println("\n\nAnonymized data has been saved to the file named: " + outputFilename);
            System.out.println("                                                 ^------------------^\n\n");

            // Write mapped data to file
            writeToFile(mappedData.toString(), mappedFilename);
            System.out.println("Mapped data has been saved to the file named: " + mappedFilename);

        } catch (IOException e) {
            // Error handling for file I/O operations
            System.out.println("Error reading/writing file: " + e.getMessage());
        }
    }

    // Step 1: Read file contents into a string
    public static String readFile(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    // Step 3: Write text string to a file
    public static void writeToFile(String text, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(text);
        }
    }

	/**
	 * Anonymizes personal data in the given text by replacing sensitive information with anonymized IDs.
	 * This method utilizes regular expressions to identify and anonymize various types of personal data.
	 * @param text:  The text containing personal data to be anonymized.
	 * @return text: The text with anonymized personal data.
	 */
    public static String anonymizeData(String text) {
        // Define regular expression patterns to address different types of personal data
        final String namePattern = "\\b(Ms\\.|Mr\\.|Mrs\\.|Dr\\.)((\\s[A-Z][a-z]+)(\\s[A-Z][A-Za-z]+)|(\\s[A-Z][a-z]+)-([A-Z][a-z]+))(?=,)";
        final String dobPattern = "\\b((\\d{2,3})(?=-year-old)|(?<=aged\\s)(\\d{2,3})|(?<=D[Oo]B\\:\\s*)(\\d{2}[-/]\\d{2}[-/]\\d{4}))\\b";
        final String addressPattern = "(?<=\\b(at|in|of)\\s)(\\d+\\s*)?([A-Z][a-z]+[ ,]*)+(Apt(\\.|#)\\s\\d+,\\s*)?(PO Box\\s\\d+,\\s*)?([A-Z][a-z]+[ ,])+\\s[A-Z]{2}(?=,)";
        final String ninPattern = "\\b[A-Z]{2}\\s*\\d{6}\\s*[A-Z]\\b";
        final String phonePattern = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        final String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}";

        // Compile the regular expression patterns
        Pattern nameRegex = Pattern.compile(namePattern);
        Pattern dobRegex = Pattern.compile(dobPattern);
        Pattern addressRegex = Pattern.compile(addressPattern);
        Pattern ninRegex = Pattern.compile(ninPattern);
        Pattern phoneRegex = Pattern.compile(phonePattern);
        Pattern emailRegex = Pattern.compile(emailPattern);

        // Map to store first names and their corresponding IDs
        Map<String, String> nameIds = new HashMap<>();

        // Anonymize different types of personal data by calling their respective methods
        System.out.println("Anonymizing the full names (as x.1): ");
        mappedData.append("Anonymizing the full names (as x.1): \n");
        text = anonymizeFullNames(text, nameRegex, ".1", nameIds);
        System.out.println("\nAnonymizing the Ages/DoBs (as x.2): ");
        mappedData.append("\n\nAnonymizing the Ages/DoBs (as x.2): \n");
        text = anonymizeOtherInfo(text, dobRegex, ".2");
        System.out.println("\nAnonymizing the Addresses With- or Without Postal Codes(as x.3): ");
        mappedData.append("\n\nAnonymizing the Addresses With- or Without Postal Codes(as x.3): \n");
        text = anonymizeOtherInfo(text, addressRegex, ".3");
        System.out.println("\nAnonymizing the referenced Firstnames/Lastnames (as x.4): ");
        mappedData.append("\n\nAnonymizing the referenced Firstnames/Lastnames (as x.4): \n");
        text = anonymizeRefNames(text, nameIds);
        System.out.println("\nAnonymizing National Insurance Numbers (NIN) (as x.5): ");
        mappedData.append("\n\nAnonymizing National Insurance Numbers (NIN) (as x.5): \n");
        text = anonymizeOtherInfo(text, ninRegex, ".5");
        System.out.println("\nAnonymizing Phone Numbers (as x.6): ");
        mappedData.append("\n\nAnonymizing Phone Numbers (as x.6): \n");
        text = anonymizeOtherInfo(text, phoneRegex, ".6");
        System.out.println("\nAnonymizing Email Addresses (as x.7): ");
        mappedData.append("\n\nAnonymizing Email Addresses (as x.7): \n");
        text = anonymizeOtherInfo(text, emailRegex, ".7");

        return text;
    }


    /**
     * Step 2.1: Anonymizing full names (including titles, first names, and last names) in the text by replacing them with their respective IDs.
     *
     * @param text:    The text to be anonymized.
     * @param pattern: The regular expression pattern to match full names.
     * @param suffix:  The suffix to append to the anonymized IDs.
     * @param nameIds: A map containing full names and their corresponding IDs.
     * @return text:   The text with anonymized full names.
     */
    public static String anonymizeFullNames(String text, Pattern pattern, String suffix, Map<String, String> nameIds) {
        Matcher matcher = pattern.matcher(text);
        int idCounter = 1;
        while (matcher.find()) {
            String match = matcher.group();
            String[] names = match.split("\\s+|-");
            if (names.length >= 2) {
                String title = names[0]; // Extract the title
                String firstName = names[1]; // Extract the first name
                String lastName = names[names.length - 1]; // Extract the last name
                // Combine the title and first/last names to form the Title+First/Last-Name(e.g., Ms. Max or Mr. Alex)
                String fullFirstName = title + " " + firstName;
                String fullLastName = title + " " + lastName;
                // Map the reference first/last names with a unique ID based on their full name ID (e.g. Sarah: 1.4 or Mr. Alex: 2.4)
                if (!nameIds.containsKey(fullFirstName)) {
                    nameIds.put(fullFirstName, idCounter + ".4");
                }
                if (!nameIds.containsKey(fullLastName)) {
                    nameIds.put(fullLastName, idCounter + ".4");
                }
                if (!nameIds.containsKey(firstName)) {
                    nameIds.put(firstName, idCounter + ".4");
                }
                if (!nameIds.containsKey(lastName)) {
                    nameIds.put(lastName, idCounter + ".4");
                }
                // Iterate over the components of the full name (excluding the title) and replace each component with its corresponding ID
                // from the nameIds map, or keep the original component if no corresponding ID is found.
                for (int i = 1; i < names.length; i++) {
                    String name = names[i];
                    String replacement = nameIds.getOrDefault(name, name);
                    names[i] = replacement;
                }
            }
            // Assign a unique ID to the current match, replace the match with the ID in the text,
            // and print the match along with its assigned ID. Also, update the ID counter.
            String anonymizedMatch = idCounter + suffix;
            text = text.replace(match, anonymizedMatch);
            System.out.println(anonymizedMatch + ": " + match);
            mappedData.append(anonymizedMatch + ": ").append(match).append("\n");
            idCounter++;
        }
        return text;
    }


    /**
     * Step 2.2:  Anonymizing reference names (with or without titles) in the text by replacing them with their respective IDs.
     *
     * @param text:    The text to be anonymized.
     * @param nameIds: A map containing full names and their corresponding IDs.
     * @return text:   The text with anonymized full names.
     */
    public static String anonymizeRefNames(String text, Map<String, String> nameIds) {
        List<String> namesWithTitle = new ArrayList<>(); // List to store names with titles
        List<String> namesWithoutTitle = new ArrayList<>(); // List to store names without titles
        // Separate names with titles from names without titles
        for (String name : nameIds.keySet()) {
            if (name.contains("Mr.") || name.contains("Dr.") || name.contains("Ms.") || name.contains("Mrs.")) {
                namesWithTitle.add(name);
            } else {
                namesWithoutTitle.add(name);
            }
        }
        List<String> replacements = new ArrayList<>(); // List to store replacements
        // Replace names with titles first
        for (String name : namesWithTitle) {
            String replacement = text.replaceAll("\\b" + name + "\\b", nameIds.get(name));
            if (!replacement.equals(text)) {
                replacements.add(nameIds.get(name) + ": " + name);
                text = replacement;
            }
        }
        // Replace names without titles next
        for (String name : namesWithoutTitle) {
            String replacement = text.replaceAll("\\b" + name + "\\b", nameIds.get(name));
            if (!replacement.equals(text)) {
                replacements.add(nameIds.get(name) + ": " + name);
                text = replacement;
            }
        }
        // Sort the replacements based on the IDs
        Collections.sort(replacements, Comparator.comparing(s -> {
            String[] parts = s.split(":");
            return Integer.parseInt(parts[0].split("\\.")[0]);
        }));
        // Print the replacement and append it to the log
        for (String replacement : replacements) {
            System.out.println(replacement);
            mappedData.append(replacement).append("\n");

        }
        return text;
    }


    /**
     * Step 2.3: Anonymizing other information (e.g., ages, addresses, phone numbers, email addresses) in the text.
     *
     * @param text:    The text to be anonymized.
     * @param pattern: The regular expression pattern to match the information to be anonymized.
     * @param suffix:  The suffix to be appended to the anonymized IDs.
     * @return text:   The text with anonymized other information.
     */
    public static String anonymizeOtherInfo(String text, Pattern pattern, String suffix) {
        // Create a matcher object to find matches in the text based on the provided pattern
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder(); // StringBuilder to store the result
        int idCounter = 1;
        boolean matchFound = false;
        // Iterate over the matches found by the matcher
        while (matcher.find()) {
            matchFound = true;
            String match = matcher.group();
            String anonymizedMatch = idCounter + suffix; // Generate an anonymized ID for the match
            // Replace the matched string in the text with the anonymized ID
            matcher.appendReplacement(result, anonymizedMatch);
            System.out.println(anonymizedMatch + ": " + match);
            // Append the anonymized ID and the matched string to the mapped data log
            mappedData.append(anonymizedMatch).append(": ").append(match).append("\n");
            idCounter++; // Increment the ID counter for the next match
        }
        // If no match is found, print a message and append it to the mapped data log
        if (!matchFound) {
            System.out.println("No match found.");
            mappedData.append("No match found.\n");
        }
        matcher.appendTail(result); // Append the remaining text after the last match to the result
        return result.toString(); // Return the text with anonymized information
    }

}
