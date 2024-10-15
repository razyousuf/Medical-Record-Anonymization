# Medical-Record-Anonymization

This project is a **Java-based anonymization tool** that reads medical notes from a text file, anonymizes personal information, and saves the anonymized data along with a log of original-to-anonymized mappings. 

It replaces personal information such as **names, dates of birth, addresses, phone numbers, and email addresses** with unique identifiers to maintain privacy. The tool ensures that even repeated occurrences of the same names receive distinct IDs to avoid cross-referencing.

---

## Table of Contents
- [Features](#features)
- [How It Works](#how-it-works)
- [Prerequisites](#prerequisites)
- [Setup and Usage](#setup-and-usage)
- [Input and Output Example](#input-and-output-example)
- [Limitations](#limitations)

---

## Features

- **Reads personal data** from a text file named `PatientNotes.txt`.  
- **Anonymizes the following types of information:**
  - Full names (with titles like Dr., Mr., Ms., etc.)
  - Dates of Birth (DoB)
  - Addresses with or without postal codes
  - Phone numbers
  - Email addresses
  - National Insurance Numbers (NINs)  
- **Outputs the results to two files:**
  - `AnonymizedData.txt`: The anonymized text.
  - `MappedData.txt`: A log of original data mapped to anonymized IDs.  
- **Generates unique IDs** for each occurrence of personal data, even if the same name or info appears multiple times.  
- **Java StringBuilder** is used for efficient logging and processing.

---

## How It Works

1. **Reading Input:** The program reads the content from `PatientNotes.txt`.  
2. **Anonymizing Data:** It identifies personal data using **regular expressions** and replaces them with unique IDs.  
   - Names are anonymized line by line, ensuring **each occurrence receives a unique ID**.
3. **Writing Output:** The anonymized text is saved in `AnonymizedData.txt` and the mapping log in `MappedData.txt`.

---

## Prerequisites

- **Java Development Kit (JDK)** 8 or higher installed.  
- Basic understanding of Java file I/O operations.  

---

## Setup and Usage

1. **Clone the repository:**
```bash
git clone https://github.com/your-username/anonymization-tool.git
cd anonymization-tool
```

2. **Compile the Java program:**

```bash
javac AnonymizationTool.java
```

3. **Create a `PatientNotes.txt` file** in the same directory with some sample medical notes.

4. **Run the program:**

```bash
java AnonymizationTool
```

5. **Check the output:**
- Anonymized text: `AnonymizedData.txt`  
- Mapped log: `MappedData.txt`   

---

## Input and Output Example

**Input (`PatientNotes.txt`):**
```bash
Ms. Emily Johnson, 34-year-old, lives at 123 Elm Street, New York, NY.
Dr. Elon Musk, aged 45, can be contacted at 555-123-4567 or elon.m@company.com.
Mr. William Turner visited on 05/12/2023.
Ms. Emily Johnson visited again on 05/15/2023.
```

**Sample Output (`AnonymizedData.txt`):**
```bash
1.1, 34-year-old, lives at 1.3.
2.1, aged 45, can be contacted at 1.6 or 1.7.
3.1 visited on 1.2.
4.1 visited again on 2.2.
```

**Sample Mapping (`MappedData.txt`):**
```bash
1.1: Ms. Emily Johnson
2.1: Dr. Elon Musk
3.1: Mr. William Turner
4.1: Ms. Emily Johnson
1.3: 123 Elm Street, New York, NY
1.6: 555-123-4567
1.7: elon.m@company.com
1.2: 05/12/2023
2.2: 05/15/2023
```

---

## Limitations

- **Sensitive information detection** depends on the accuracy of regular expressions, so the tool may miss non-standard data formats.
- **Anonymization is irreversible**, meaning the original data cannot be restored from the anonymized output.
- Only supports English-language text; might not work well with other languages.

---

## License

– Feel free to use and modify it.

---

## Feedback & Support

If you encounter any issues or have suggestions for improvements, please open an issue on the GitHub repository.

