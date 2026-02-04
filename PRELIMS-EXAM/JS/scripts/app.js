// Weiam Aspiras 25-0430-348

// The hardcoded CSV content
const CSV_DATA = `StudentID,first_name,last_name,LAB WORK 1,LAB WORK 2,LAB WORK 3,PRELIM EXAM,ATTENDANCE GRADE
073900438,Osbourne,Wakenshaw,69,5,52,12,78
114924014,Albie,Gierardi,58,92,16,57,97
111901632,Eleen,Pentony,43,81,34,36,16
084000084,Arie,Okenden,31,5,14,39,99
272471551,Alica,Muckley,49,66,97,3,95
104900721,Jo,Burleton,98,94,33,13,29
111924392,Cam,Akram,44,84,17,16,24
292970744,Celine,Brosoli,3,15,71,83,45
107004352,Alan,Belfit,31,51,36,70,48`;

// Global array to store student data
let students = [];

// ============================================
// GRADE CALCULATION (from calculator.html)
// ============================================
function calculatePrelimGrade(lab1, lab2, lab3, prelimExam, attendanceGrade) {
    // Step 1: Calculate Lab Work Average
    const labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
    
    // Step 2: Calculate Class Standing
    // Class Standing = (Attendance x 0.40) + (Lab Work Average x 0.60)
    const classStanding = (attendanceGrade * 0.40) + (labWorkAverage * 0.60);
    
    // Step 3: Calculate Prelim Grade
    // Prelim Grade = (Prelim Exam x 0.30) + (Class Standing x 0.70)
    const prelimGrade = (prelimExam * 0.30) + (classStanding * 0.70);
    
    return prelimGrade.toFixed(2);
}

// Parse CSV string into Array of Objects
function parseCSV(csvString) {
    const lines = csvString.trim().split('\n');
    const headers = lines[0].split(',');
    const data = [];
    
    for (let i = 1; i < lines.length; i++) {
        const values = lines[i].split(',');
        const row = {};
        headers.forEach((header, index) => {
            row[header.trim()] = values[index] ? values[index].trim() : '';
        });
        data.push(row);
    }
    return data;
}

// Initialize: Parse hardcoded CSV into students array
function initializeData() {
    const data = parseCSV(CSV_DATA);
    students = data.map(row => {
        // Get values from CSV
        const lab1 = parseFloat(row['LAB WORK 1']) || 0;
        const lab2 = parseFloat(row['LAB WORK 2']) || 0;
        const lab3 = parseFloat(row['LAB WORK 3']) || 0;
        const prelimExam = parseFloat(row['PRELIM EXAM']) || 0;
        const attendanceGrade = parseFloat(row['ATTENDANCE GRADE']) || 0;
        
        // Calculate the Prelim Grade
        const grade = calculatePrelimGrade(lab1, lab2, lab3, prelimExam, attendanceGrade);
        
        return {
            id: row.StudentID || '',
            name: (row.first_name || '') + ' ' + (row.last_name || ''),
            grade: grade
        };
    });
    render();
}

// Read: Render function that clears and re-populates the table rows
function render() {
    const tableBody = document.getElementById('tableBody');
    tableBody.innerHTML = ''; // Clear the table

    students.forEach((student, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.grade}</td>
            <td>
                <button class="delete-btn" onclick="deleteRecord(${index})">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Create: Push a new object to the array and re-render
function createRecord() {
    const id = document.getElementById('idInput').value.trim();
    const name = document.getElementById('nameInput').value.trim();
    const grade = document.getElementById('gradeInput').value.trim();

    if (id && name && grade) {
        // Push new object to array
        students.push({ id, name, grade });

        // Clear input fields
        document.getElementById('idInput').value = '';
        document.getElementById('nameInput').value = '';
        document.getElementById('gradeInput').value = '';

        // Re-render the table
        render();
    } else {
        alert('Please fill in ID, Name, and Grade');
    }
}

// Delete: Remove entry from array at specified index
function deleteRecord(index) {
    if (confirm('Are you sure you want to delete this record?')) {
        students.splice(index, 1); // Remove from array
        render(); // Re-render the table
    }
}

// Initialize on page load
window.onload = initializeData;