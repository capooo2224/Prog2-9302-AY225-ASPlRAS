const fs = require('fs');
const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// ─── Module: File Validation ───

function validateFilePath(path) {
    if (!fs.existsSync(path)) {
        return "Error: File not found. Please try again.";
    }
    try {
        fs.accessSync(path, fs.constants.R_OK);
    } catch (e) {
        return "Error: File is not readable. Please try again.";
    }
    if (!path.trim().toLowerCase().endsWith('.csv')) {
        return "Error: File is not a CSV file. Please try again.";
    }
    return null; // valid
}

// ─── Module: CSV Parsing ───

function parseCSV(filePath) {
    const content = fs.readFileSync(filePath, 'utf-8');
    const lines = content.split(/\r?\n/).filter(line => line.trim() !== '');

    if (lines.length < 2) {
        throw new Error("CSV file is empty or has no data rows.");
    }

    const headers = lines[0].split(',').map(h => h.trim());
    const records = [];

    for (let i = 1; i < lines.length; i++) {
        const values = lines[i].split(',').map(v => v.trim());
        const record = {};
        headers.forEach((header, idx) => {
            record[header] = idx < values.length ? values[idx] : '';
        });
        records.push(record);
    }

    return { headers, records };
}

// ─── Module: Monthly Grouping & Analysis ───

function detectColumns(headers) {
    // Try to auto-detect date and sales columns (case-insensitive partial match)
    const dateKeywords = ['date', 'month', 'order_date', 'transaction_date', 'sold_date'];
    const salesKeywords = ['sales', 'amount', 'revenue', 'total', 'price', 'income'];

    let dateCol = null;
    let salesCol = null;

    for (const h of headers) {
        const lower = h.toLowerCase();
        if (!dateCol && dateKeywords.some(k => lower.includes(k))) dateCol = h;
        if (!salesCol && salesKeywords.some(k => lower.includes(k))) salesCol = h;
    }

    return { dateCol, salesCol };
}

function extractMonth(dateStr) {
    // Try parsing common date formats
    const d = new Date(dateStr);
    if (!isNaN(d.getTime())) {
        const year = d.getFullYear();
        const month = d.getMonth() + 1;
        return `${year}-${String(month).padStart(2, '0')}`;
    }
    return null;
}

function groupByMonth(records, dateCol, salesCol) {
    const monthlyData = {};

    for (const record of records) {
        const dateVal = record[dateCol];
        const salesVal = parseFloat(record[salesCol]);

        if (!dateVal || isNaN(salesVal)) continue;

        const monthKey = extractMonth(dateVal);
        if (!monthKey) continue;

        if (!monthlyData[monthKey]) {
            monthlyData[monthKey] = { totalSales: 0, count: 0 };
        }
        monthlyData[monthKey].totalSales += salesVal;
        monthlyData[monthKey].count += 1;
    }

    return monthlyData;
}

function getMonthName(monthKey) {
    const [year, month] = monthKey.split('-');
    const names = ['', 'January', 'February', 'March', 'April', 'May', 'June',
                   'July', 'August', 'September', 'October', 'November', 'December'];
    return `${names[parseInt(month)]} ${year}`;
}

// ─── Module: Display ───

function displayMonthlySummary(monthlyData) {
    const sorted = Object.entries(monthlyData).sort((a, b) => a[0].localeCompare(b[0]));

    if (sorted.length === 0) {
        console.log("No valid monthly data found.");
        return;
    }

    // Find best month
    let bestMonth = sorted[0];
    for (const entry of sorted) {
        if (entry[1].totalSales > bestMonth[1].totalSales) {
            bestMonth = entry;
        }
    }

    // Column widths
    const monthWidth = 20;
    const salesWidth = 18;
    const countWidth = 14;

    const border = '+' + '-'.repeat(monthWidth + 2) + '+' + '-'.repeat(salesWidth + 2) + '+' + '-'.repeat(countWidth + 2) + '+';

    console.log("\n===== MONTHLY PERFORMANCE SUMMARY =====\n");
    console.log(border);
    console.log('| ' + 'Month'.padEnd(monthWidth) + ' | ' + 'Total Sales'.padEnd(salesWidth) + ' | ' + 'Transactions'.padEnd(countWidth) + ' |');
    console.log(border);

    for (const [monthKey, data] of sorted) {
        const name = getMonthName(monthKey).padEnd(monthWidth);
        const sales = data.totalSales.toFixed(2).padStart(salesWidth);
        const count = String(data.count).padStart(countWidth);
        console.log(`| ${name} | ${sales} | ${count} |`);
    }

    console.log(border);

    console.log(`\nTotal Months Analyzed: ${sorted.length}`);

    const grandTotal = sorted.reduce((sum, e) => sum + e[1].totalSales, 0);
    console.log(`Grand Total Sales   : ${grandTotal.toFixed(2)}`);

    console.log(`\n*** Best-Performing Month: ${getMonthName(bestMonth[0])} with total sales of ${bestMonth[1].totalSales.toFixed(2)} ***\n`);
}

function displayDetectedColumns(dateCol, salesCol) {
    console.log(`\nDetected date column  : ${dateCol}`);
    console.log(`Detected sales column : ${salesCol}`);
}

// ─── Main: File Path Prompt Loop ───

function askFilePath() {
    rl.question("Enter dataset file path: ", function(path) {
        path = path.trim();

        const error = validateFilePath(path);
        if (error) {
            console.log(error);
            askFilePath();
            return;
        }

        console.log("File found. Processing...\n");

        try {
            const { headers, records } = parseCSV(path);

            console.log("========================================");
            console.log("    Monthly Performance Analyzer");
            console.log("========================================");
            console.log(`\nDataset loaded: ${records.length} records`);
            console.log(`Columns: ${headers.join(', ')}`);

            const { dateCol, salesCol } = detectColumns(headers);

            if (!dateCol || !salesCol) {
                console.log("\nError: Could not auto-detect date or sales columns.");
                console.log("Available columns: " + headers.join(', '));
                console.log("Please ensure your CSV has a date column and a sales/amount column.");
                rl.close();
                return;
            }

            displayDetectedColumns(dateCol, salesCol);

            const monthlyData = groupByMonth(records, dateCol, salesCol);
            displayMonthlySummary(monthlyData);

            console.log("========================================");
            console.log("         Analysis Complete");
            console.log("========================================");

        } catch (e) {
            console.log("Error processing file: " + e.message);
        }

        rl.close();
    });
}

// ─── Entry Point ───
console.log("========================================");
console.log("    Monthly Performance Analyzer");
console.log("========================================\n");

askFilePath();
