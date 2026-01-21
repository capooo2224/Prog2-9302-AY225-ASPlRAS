// Default test accounts
const defaultAccounts = [
    { username: "admin", password: "password123" },
    { username: "user1", password: "test123" },
    { username: "user2", password: "test456" },
    { username: "guest", password: "guest" },
    { username: "Capo", password: "CAPO2224" },
];

// Load accounts from localStorage or use defaults
function loadAccounts() {
    const saved = localStorage.getItem('userAccounts');
    if (saved) {
        return JSON.parse(saved);
    }
    // Save defaults to localStorage
    localStorage.setItem('userAccounts', JSON.stringify(defaultAccounts));
    return defaultAccounts;
}

// Save accounts to localStorage
function saveAccounts() {
    localStorage.setItem('userAccounts', JSON.stringify(userAccounts));
}

// User accounts storage
let userAccounts = loadAccounts();

// Attendance records storage
let attendanceRecords = [];

// Get DOM elements
const loginForm = document.getElementById('loginForm');
const loginBox = document.getElementById('loginBox');
const welcomeBox = document.getElementById('welcomeBox');
const errorMessage = document.getElementById('errorMessage');
const welcomeMessage = document.getElementById('welcomeMessage');
const timestampDisplay = document.getElementById('timestamp');
const downloadBtn = document.getElementById('downloadBtn');
const logoutBtn = document.getElementById('logoutBtn');
const beepSound = document.getElementById('beepSound');
const showRegisterBtn = document.getElementById('showRegisterBtn');
const backToLoginBtn = document.getElementById('backToLoginBtn');
const registerBox = document.getElementById('registerBox');
const registerForm = document.getElementById('registerForm');
const registerMessage = document.getElementById('registerMessage');

// Format date and time as MM/DD/YYYY HH:MM:SS
function formatDateTime(date) {
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
}

// Play beep sound for incorrect password (2 seconds max, lower volume)
function playBeep() {
    beepSound.currentTime = 0;
    beepSound.volume = 0.3; // Lower volume (0.0 to 1.0)
    beepSound.play().catch(error => {
        console.log('Audio play failed:', error);
        // Fallback: use Web Audio API to generate beep
        const audioContext = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioContext.createOscillator();
        const gainNode = audioContext.createGain();
        
        oscillator.connect(gainNode);
        gainNode.connect(audioContext.destination);
        
        oscillator.frequency.value = 800;
        oscillator.type = 'square';
        gainNode.gain.value = 0.1; // Lower volume for fallback
        
        oscillator.start();
        setTimeout(() => oscillator.stop(), 300);
    });
    
    // Stop after 2 seconds
    setTimeout(() => {
        stopBeep();
    }, 2000);
}

// Stop beep sound
function stopBeep() {
    beepSound.pause();
    beepSound.currentTime = 0;
}

// Handle login form submission
loginForm.addEventListener('submit', function(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // Validate credentials against stored accounts
    const validUser = userAccounts.find(user => user.username === username && user.password === password);
    
    if (validUser) {
        // Stop any playing beep sound
        stopBeep();
        
        // Successful login
        const loginTime = new Date();
        const formattedTime = formatDateTime(loginTime);
        
        // Add to attendance records
        attendanceRecords.push({
            username: username,
            timestamp: formattedTime
        });
        
        // Show welcome message
        welcomeMessage.textContent = `Hello, ${username}!`;
        timestampDisplay.textContent = formattedTime;
        
        // Hide login form and show welcome box
        loginBox.style.display = 'none';
        welcomeBox.style.display = 'block';
        
        // Clear error message
        errorMessage.textContent = '';
    } else {
        // Failed login
        errorMessage.textContent = 'Invalid username or password!';
        playBeep();
    }
});

// Handle attendance download
downloadBtn.addEventListener('click', function() {
    // Generate attendance summary
    let summaryContent = "=== ATTENDANCE SUMMARY ===\n\n";
    
    attendanceRecords.forEach((record, index) => {
        summaryContent += `Record #${index + 1}\n`;
        summaryContent += `Username: ${record.username}\n`;
        summaryContent += `Timestamp: ${record.timestamp}\n`;
        summaryContent += "-------------------------\n\n";
    });
    
    summaryContent += `Total Records: ${attendanceRecords.length}\n`;
    summaryContent += `Generated on: ${formatDateTime(new Date())}`;
    
    // Create and download file using Blob API
    const blob = new Blob([summaryContent], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = 'attendance_summary.txt';
    link.click();
    
    // Clean up
    window.URL.revokeObjectURL(link.href);
});

// Handle logout
logoutBtn.addEventListener('click', function() {
    // Reset form
    loginForm.reset();
    
    // Show login box and hide welcome box
    loginBox.style.display = 'block';
    welcomeBox.style.display = 'none';
});

// Show registration form
showRegisterBtn.addEventListener('click', function() {
    loginBox.style.display = 'none';
    registerBox.style.display = 'block';
    registerMessage.textContent = '';
    errorMessage.textContent = '';
});

// Back to login form
backToLoginBtn.addEventListener('click', function() {
    registerBox.style.display = 'none';
    loginBox.style.display = 'block';
    registerForm.reset();
    registerMessage.textContent = '';
});

// Handle registration
registerForm.addEventListener('submit', function(event) {
    event.preventDefault();
    
    const newUsername = document.getElementById('newUsername').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    // Check if passwords match
    if (newPassword !== confirmPassword) {
        registerMessage.textContent = 'Passwords do not match!';
        registerMessage.className = 'error';
        playBeep();
        return;
    }
    
    // Check if username already exists
    const existingUser = userAccounts.find(user => user.username === newUsername);
    if (existingUser) {
        registerMessage.textContent = 'Username already exists!';
        registerMessage.className = 'error';
        playBeep();
        return;
    }
    
    // Add new user account
    userAccounts.push({
        username: newUsername,
        password: newPassword
    });
    
    // Save to localStorage
    saveAccounts();
    
    // Show success message and go back to login
    registerMessage.textContent = 'Account created successfully!';
    registerMessage.className = 'success-message';
    
    // Switch to login after 1.5 seconds
    setTimeout(function() {
        registerBox.style.display = 'none';
        loginBox.style.display = 'block';
        registerForm.reset();
        registerMessage.textContent = '';
    }, 1500);
});